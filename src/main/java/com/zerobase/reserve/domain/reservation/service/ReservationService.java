package com.zerobase.reserve.domain.reservation.service;

import com.zerobase.reserve.domain.common.utils.KeyGenerator;
import com.zerobase.reserve.domain.member.entity.Member;
import com.zerobase.reserve.domain.member.service.MemberService;
import com.zerobase.reserve.domain.reservation.dto.Reserve;
import com.zerobase.reserve.domain.reservation.dto.model.ReservationDto;
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

import java.time.LocalDate;

import static com.zerobase.reserve.global.exception.ErrorCode.ALREADY_RESERVED;

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
        Member member = memberService.getMember(request.getMemberKey());
        Store store = storeService.getStore(request.getStoreKey());

        validateReservation(request, store);

        return ReservationDto.fromEntity(reservationRepository.save(
                request.toEntity(keyGenerator.generateKey(), member, store)));
    }

    private void validateReservation(Reserve.Request request, Store store) {
        if (reservationRepository
                .existsByStoreAndReservationDateAndReservationTimeAndReservationType(
                store, request.getReservationDate(), request.getReservationTime(),
                        ReservationType.CONFIRM)) {
            throw new ReservationException(ALREADY_RESERVED);
        }
    }

    public Page<ReservationDto> reservations(String storeKey,
                                             LocalDate reservationDate,
                                             Pageable pageable) {
        Store store = storeService.getStore(storeKey);

        return reservationRepository.findAllByStoreAndReservationDate(
                store, reservationDate, pageable)
                .map(ReservationDto::fromEntity);
    }
}
