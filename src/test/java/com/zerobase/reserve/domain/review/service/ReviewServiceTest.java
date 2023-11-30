package com.zerobase.reserve.domain.review.service;

import com.zerobase.reserve.domain.common.builder.MemberBuilder;
import com.zerobase.reserve.domain.common.builder.ReservationBuilder;
import com.zerobase.reserve.domain.common.builder.ReviewBuilder;
import com.zerobase.reserve.domain.common.builder.StoreBuilder;
import com.zerobase.reserve.domain.member.entity.Member;
import com.zerobase.reserve.domain.reservation.entity.Reservation;
import com.zerobase.reserve.domain.reservation.exception.ReservationException;
import com.zerobase.reserve.domain.reservation.service.ReservationService;
import com.zerobase.reserve.domain.reservation.type.ReservationType;
import com.zerobase.reserve.domain.review.dto.Update;
import com.zerobase.reserve.domain.review.dto.Write;
import com.zerobase.reserve.domain.review.dto.model.ReviewDto;
import com.zerobase.reserve.domain.review.entity.Review;
import com.zerobase.reserve.domain.review.exception.ReviewException;
import com.zerobase.reserve.domain.review.repository.ReviewRepository;
import com.zerobase.reserve.domain.store.entity.Store;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.zerobase.reserve.domain.common.constants.ReviewConstants.*;
import static com.zerobase.reserve.global.exception.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
    @Mock
    ReviewRepository reviewRepository;

    @Mock
    ReservationService reservationService;

    @InjectMocks
    ReviewService reviewService;

    @Test
    @DisplayName("리뷰 작성 성공")
    void write_success() {
        // given
        Member member = MemberBuilder.member();
        Store store = StoreBuilder.store();

        Reservation reservation = ReservationBuilder.reservation();
        reservation.addMemberAndStore(member, store);
        reservation.updateReservationType(ReservationType.CONFIRM);
        reservation.updateArrival();

        given(reservationService.getReservationFetchJoinOrThrow(any()))
                .willReturn(reservation);

        Review review = ReviewBuilder.review();
        review.addMemberAndStore(member, store);

        given(reviewRepository.save(any()))
                .willReturn(review);

        // when
        ReviewDto reviewDto = reviewService.write(
                Write.Request.builder().build(), MemberBuilder.member());

        // then
        assertEquals(CONTENTS, reviewDto.getContents());
        assertEquals(SCORE, reviewDto.getScore());
    }

    @Test
    @DisplayName("리뷰 작성 실패 - 존재하지 않는 예약")
    void write_reservation_not_found() {
        // given
        given(reservationService.getReservationFetchJoinOrThrow(any()))
                .willThrow(new ReservationException(RESERVATION_NOT_FOUND));

        // when
        ReservationException exception = assertThrows(ReservationException.class,
                () -> reviewService.write(Write.Request.builder().build(),
                        MemberBuilder.member()));

        // then
        assertEquals(RESERVATION_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("리뷰 작성 실패 - 확정된 예약이 아님")
    void write_not_confirm() {
        // given
        Member member = MemberBuilder.member();
        Store store = StoreBuilder.store();

        Reservation reservation = ReservationBuilder.reservation();
        reservation.addMemberAndStore(member, store);

        given(reservationService.getReservationFetchJoinOrThrow(any()))
                .willReturn(reservation);

        // when
        ReservationException exception = assertThrows(ReservationException.class,
                () -> reviewService.write(Write.Request.builder().build(),
                        MemberBuilder.member()));

        // then
        assertEquals(RESERVATION_NOT_VISITED, exception.getErrorCode());
    }

    @Test
    @DisplayName("리뷰 작성 실패 - 도착한 예약이 아님")
    void write_not_arrival() {
        // given
        Member member = MemberBuilder.member();
        Store store = StoreBuilder.store();

        Reservation reservation = ReservationBuilder.reservation();
        reservation.addMemberAndStore(member, store);
        reservation.updateReservationType(ReservationType.CONFIRM);

        given(reservationService.getReservationFetchJoinOrThrow(any()))
                .willReturn(reservation);

        // when
        ReservationException exception = assertThrows(ReservationException.class,
                () -> reviewService.write(Write.Request.builder().build(),
                        MemberBuilder.member()));

        // then
        assertEquals(RESERVATION_NOT_VISITED, exception.getErrorCode());
    }

    @Test
    @DisplayName("리뷰 작성 실패 - 예약한 회원이 아님")
    void write_owner_not_equal() {
        // given
        Member member = Member.builder()
                .email("do2@gmail.com")
                .build();
        Store store = StoreBuilder.store();

        Reservation reservation = ReservationBuilder.reservation();
        reservation.addMemberAndStore(member, store);
        reservation.updateReservationType(ReservationType.CONFIRM);
        reservation.updateArrival();

        given(reservationService.getReservationFetchJoinOrThrow(any()))
                .willReturn(reservation);

        // when
        ReservationException exception = assertThrows(ReservationException.class,
                () -> reviewService.write(Write.Request.builder().build(),
                        MemberBuilder.member()));

        // then
        assertEquals(RESERVATION_ACCESS_DENY, exception.getErrorCode());
    }

    @Test
    @DisplayName("리뷰 수정 성공")
    void update_success() {
        // given
        Member member = MemberBuilder.member();
        Store store = StoreBuilder.store();

        String contents = "맛있어요! 222";
        Review review = Review.builder()
                .contents(contents)
                .score(SCORE)
                .build();
        review.addMemberAndStore(member, store);

        given(reviewRepository.findByIdFetchJoin(any()))
                .willReturn(Optional.of(review));

        // when
        ReviewDto reviewDto = reviewService.update(
                Update.Request.builder()
                        .contents(contents)
                        .score(SCORE)
                        .build());

        // then
        assertEquals(contents, reviewDto.getContents());
        assertEquals(SCORE, reviewDto.getScore());
    }

    @Test
    @DisplayName("리뷰 수정 실패 - 존재하지 않는 리뷰")
    void update_review_not_found() {
        // given
        given(reviewRepository.findByIdFetchJoin(any()))
                .willReturn(Optional.empty());

        // when
        ReviewException exception = assertThrows(ReviewException.class, () ->
                reviewService.update(Update.Request.builder().build()));

        // then
        assertEquals(REVIEW_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("리뷰 삭제 성공")
    void delete_success() {
        // given
        Member member = MemberBuilder.member();
        Store store = StoreBuilder.store();
        Review review = ReviewBuilder.review();
        review.addMemberAndStore(member, store);

        given(reviewRepository.findByIdFetchJoin(any()))
                .willReturn(Optional.of(review));

        doNothing().when(reviewRepository).deleteByReviewId(any());

        // when
        ReviewDto reviewDto = reviewService.delete(ID);

        // then
        assertEquals(CONTENTS, reviewDto.getContents());
    }

    @Test
    @DisplayName("리뷰 삭제 실패 - 존재하지 않는 리뷰")
    void delete_review_not_found() {
        // given
        given(reviewRepository.findByIdFetchJoin(any()))
                .willReturn(Optional.empty());

        // when
        ReviewException exception = assertThrows(ReviewException.class, () ->
                reviewService.delete(ID));

        // then
        assertEquals(REVIEW_NOT_FOUND, exception.getErrorCode());
    }
}