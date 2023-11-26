package com.zerobase.reserve.domain.reservation.service;

import com.zerobase.reserve.domain.common.utils.KeyGenerator;
import com.zerobase.reserve.domain.member.entity.Member;
import com.zerobase.reserve.domain.member.service.MemberService;
import com.zerobase.reserve.domain.reservation.dto.Confirm;
import com.zerobase.reserve.domain.reservation.dto.ReservationsResponse;
import com.zerobase.reserve.domain.reservation.dto.Reserve;
import com.zerobase.reserve.domain.reservation.dto.Visit;
import com.zerobase.reserve.domain.reservation.dto.model.ReservationDto;
import com.zerobase.reserve.domain.reservation.entity.Reservation;
import com.zerobase.reserve.domain.reservation.exception.ReservationException;
import com.zerobase.reserve.domain.reservation.repository.ReservationRepository;
import com.zerobase.reserve.domain.reservation.type.ReservationType;
import com.zerobase.reserve.domain.store.entity.Store;
import com.zerobase.reserve.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import static com.zerobase.reserve.global.exception.ErrorCode.*;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final MemberService memberService;
    private final StoreService storeService;
    private final KeyGenerator keyGenerator;

    @Transactional
    public ReservationDto reserve(Reserve.Request request) {
        Store store = storeService.getStoreOrThrow(request.getStoreKey());

        existsReservation(request.getReservationDate(),
                request.getReservationTime(), store);

        Member member = memberService.getMemberOrThrow(request.getMemberKey());

        return ReservationDto.fromEntity(reservationRepository.save(
                request.toEntity(keyGenerator.generateKey(), member, store)));
    }

    private void existsReservation(LocalDate reservationDate,
                                   LocalTime reservationTime,
                                   Store store) {
        if (reservationRepository.existsReservation(store, reservationDate,
                reservationTime, ReservationType.WAIT)) {
            throw new ReservationException(ALREADY_RESERVED);
        }
    }

    public Page<ReservationsResponse> reservations(String storeKey,
                                                   LocalDate reservationDate,
                                                   Pageable pageable) {
        Store store = storeService.getStoreOrThrow(storeKey);

        return reservationRepository.findReservationsFetchJoin(
                store, reservationDate, pageable)
                .map(ReservationsResponse::fromEntity);
    }

    @Transactional
    public Confirm.Response confirm(Confirm.Request request) {
        Reservation reservation =
                reservationRepository.findByReservationKey(request.reservationKey())
                        .orElseThrow(() ->
                                new ReservationException(RESERVATION_NOT_FOUND));

        reservation.updateReservationType(request.reservationType());
        return new Confirm.Response(reservation.getReservationKey());
    }

    @Transactional
    public Visit.Response visit(Visit.Request request) {
        Member member = memberService.getMemberByPhoneNumberOrThrow(
                request.getPhoneNumber());

        Store store = storeService.getStoreOrThrow(request.getStoreKey());

        Reservation reservation =
                reservationRepository.findReservation(member, store,
                                request.getReservationDate(),
                                request.getReservationTime(),
                                ReservationType.CONFIRM)
                .orElseThrow(() -> new ReservationException(RESERVATION_NOT_FOUND));

        validateArrivalTime(reservation);

        reservation.updateArrival();
        return new Visit.Response(reservation.getReservationKey());
    }

    private static void validateArrivalTime(Reservation reservation) {
        if (LocalTime.now().isAfter(reservation.getReservationTime()
                .minus(Duration.ofMinutes(10)))) {
            throw new ReservationException(ARRIVAL_TIME_EXCEED);
        }
    }
}
