package com.zerobase.reserve.domain.reservation.service;

import com.zerobase.reserve.domain.common.builder.MemberBuilder;
import com.zerobase.reserve.domain.common.builder.ReservationBuilder;
import com.zerobase.reserve.domain.common.builder.StoreBuilder;
import com.zerobase.reserve.domain.common.utils.KeyGenerator;
import com.zerobase.reserve.domain.member.entity.Member;
import com.zerobase.reserve.domain.member.exception.MemberException;
import com.zerobase.reserve.domain.member.service.MemberService;
import com.zerobase.reserve.domain.reservation.dto.ReservationsResponse;
import com.zerobase.reserve.domain.reservation.dto.Reserve;
import com.zerobase.reserve.domain.reservation.dto.model.ReservationDto;
import com.zerobase.reserve.domain.reservation.entity.Reservation;
import com.zerobase.reserve.domain.reservation.exception.ReservationException;
import com.zerobase.reserve.domain.reservation.repository.ReservationRepository;
import com.zerobase.reserve.domain.store.entity.Store;
import com.zerobase.reserve.domain.store.exception.StoreException;
import com.zerobase.reserve.domain.store.service.StoreService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;

import static com.zerobase.reserve.domain.common.constants.ReservationConstants.*;
import static com.zerobase.reserve.global.exception.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {
    @Mock
    ReservationRepository reservationRepository;

    @Mock
    MemberService memberService;

    @Mock
    StoreService storeService;

    @Mock
    KeyGenerator keyGenerator;

    @InjectMocks
    ReservationService reservationService;

    @Test
    @DisplayName("예약 성공")
    void reserve_success() {
        // given
        Store store = StoreBuilder.store();
        Member member = MemberBuilder.member();
        Reservation reservation = ReservationBuilder.reservation();
        reservation.addMemberAndStore(member, store);

        given(storeService.getStoreOrThrow(any()))
                .willReturn(store);

        given(reservationRepository.existsReservation(any(), any(), any(), any()))
                .willReturn(false);

        given(memberService.getMemberOrThrow(any()))
                .willReturn(member);

        given(keyGenerator.generateKey())
                .willReturn(RESERVATION_KEY);

        given(reservationRepository.save(any()))
                .willReturn(reservation);

        // when
        ReservationDto reservationDto = reservationService.reserve(
                Reserve.Request.builder().build());

        // then
        assertEquals(RESERVATION_KEY, reservationDto.getReservationKey());
        assertEquals(RESERVATION_DATE, reservationDto.getReservationDate());
        assertEquals(RESERVATION_TIME, reservationDto.getReservationTime());
        assertEquals(PERSONS, reservationDto.getPersons());
        assertEquals(RESERVATION_TYPE, reservationDto.getReservationType());
    }

    @Test
    @DisplayName("예약 실패 - 존재하지 않는 매장")
    void reserve_not_found_store() {
        // given
        given(storeService.getStoreOrThrow(any()))
                .willThrow(new StoreException(STORE_NOT_FOUND));

        // when
        StoreException exception = assertThrows(
                StoreException.class, () -> reservationService.reserve(
                        Reserve.Request.builder().build()));

        // then
        assertEquals(STORE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("예약 실패 - 이미 있는 예약")
    void reserve_already_reserved() {
        // given
        given(storeService.getStoreOrThrow(any()))
                .willReturn(StoreBuilder.store());

        given(reservationRepository.existsReservation(any(), any(), any(), any()))
                .willReturn(true);

        // when
        ReservationException exception = assertThrows(
                ReservationException.class, () -> reservationService.reserve(
                        Reserve.Request.builder().build()));

        // then
        assertEquals(ALREADY_RESERVED, exception.getErrorCode());
    }

    @Test
    @DisplayName("예약 실패 - 존재하지 않는 회원")
    void reserve_not_found_member() {
        // given
        given(storeService.getStoreOrThrow(any()))
                .willReturn(StoreBuilder.store());

        given(reservationRepository.existsReservation(any(), any(), any(), any()))
                .willReturn(false);

        given(memberService.getMemberOrThrow(any()))
                .willThrow(new MemberException(MEMBER_NOT_FOUND));

        // when
        MemberException exception = assertThrows(
                MemberException.class, () -> reservationService.reserve(
                        Reserve.Request.builder().build()));

        // then
        assertEquals(MEMBER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("예약 조회 성공")
    void reservations_success() {
        // given
        Store store = StoreBuilder.store();
        Member member = MemberBuilder.member();
        Reservation reservation = ReservationBuilder.reservation();
        reservation.addMemberAndStore(member, store);

        given(storeService.getStoreOrThrow(any()))
                .willReturn(store);

        given(reservationRepository.findReservationsFetchJoin(
                any(), any(), any()
        )).willReturn(new PageImpl<>(List.of(reservation)));

        // when
        Page<ReservationsResponse> reservations =
                reservationService.reservations("", LocalDate.now(),
                PageRequest.of(1, 1));

        // then
        assertEquals(1, reservations.getSize());
        assertEquals(RESERVATION_KEY,
                reservations.getContent().get(0).getReservationKey());
    }

    @Test
    @DisplayName("예약 조회 실패 - 존재하지 않는 매장")
    void reservations_not_found_store() {
        // given
        given(storeService.getStoreOrThrow(any()))
                .willThrow(new StoreException(STORE_NOT_FOUND));

        // when
        StoreException exception = assertThrows(
                StoreException.class, () -> reservationService.reservations(
                        "", LocalDate.now(),
                        PageRequest.of(1, 1)));

        // then
        assertEquals(STORE_NOT_FOUND, exception.getErrorCode());
    }
}