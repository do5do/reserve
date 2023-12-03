package com.zerobase.reserve.domain.review.service;

import com.zerobase.reserve.domain.member.entity.Member;
import com.zerobase.reserve.domain.member.entity.Role;
import com.zerobase.reserve.domain.member.service.MemberService;
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
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.zerobase.reserve.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReservationService reservationService;
    private final MemberService memberService;

    /**
     * 리뷰 작성
     * 리뷰 작성을 요청한 예약에 대해 방문 완료한 예약인지 확인하고,
     * 예약자와 리뷰 요청 자가 같은지 검증 후 리뷰를 작성합니다.
     *
     * @param request     리뷰 작성 요청 정보
     * @param userDetails 인증된 유저 정보 (이메일, 권한)
     * @return 작성된 리뷰 정보
     */
    @Transactional
    public ReviewDto write(Write.Request request, UserDetails userDetails) {
        Reservation reservation = reservationService
                .getReservationFetchJoinOrThrow(request.getReservationKey());

        validateReservation(reservation, userDetails);

        return ReviewDto.fromEntity(
                reviewRepository.save(request.toEntity(reservation)));
    }

    private static void validateReservation(Reservation reservation,
                                            UserDetails userDetails) {
        if (reservation.getReservationType() != ReservationType.CONFIRM) {
            throw new ReservationException(RESERVATION_NOT_VISITED);
        }

        if (!reservation.isArrival()) {
            throw new ReservationException(RESERVATION_NOT_VISITED);
        }

        if (!reservation.getMember().getEmail().equals(userDetails.getUsername())) {
            throw new ReservationException(RESERVATION_ACCESS_DENY);
        }
    }

    /**
     * 리뷰 수정
     * 수정을 요청한 유저와 리뷰를 작성한 유저가 다를 경우 리뷰 수정에 실패합니다.
     * 회원을 조회하는 쿼리를 대신하여 인증된 사용자의 정보로 검증합니다.
     *
     * @param request 리뷰 수정 요청 정보
     * @return 수정된 리뷰 정보
     */
    @PostAuthorize("isAuthenticated() " +
            "and returnObject.member.email == principal.username")
    @Transactional
    public ReviewDto update(Update.Request request) {
        Review review = findByIdOrThrow(request.getReviewId());
        review.updateReview(request);
        return ReviewDto.fromEntity(review);
    }

    /**
     * 리뷰 삭제
     * 로그인한 사용자가 일반 사용자(USER)일 경우 리뷰 작성자와 같은지 확인합니다.
     * 로그인한 사용자가 매니저일 경우 리뷰가 등록된 매장의 점주와 같은지 확인합니다.
     *
     * @param reviewId    리뷰 식별 id
     * @param userDetails 인증된 유저 정보
     * @return 삭제된 리뷰 id
     */
    @Transactional
    public Long delete(Long reviewId, UserDetails userDetails) {
        Review review = findByIdOrThrow(reviewId);

        validateMember(userDetails, review);

        reviewRepository.deleteByReviewId(reviewId);
        return reviewId;
    }

    private void validateMember(UserDetails userDetails, Review review) {
        Member principal =
                memberService.findByEmailOrThrow(userDetails.getUsername());

        if (principal.getRole() == Role.MANAGER) {
            if (!review.getStore().getMember().getEmail()
                    .equals(principal.getEmail())) {
                throw new ReviewException(REVIEW_ACCESS_DENY);
            }
        } else {
            if (!review.getMember().getEmail().equals(principal.getEmail())) {
                throw new ReviewException(REVIEW_ACCESS_DENY);
            }
        }
    }

    private Review findByIdOrThrow(Long reviewId) {
        return reviewRepository.findByIdFetchJoin(reviewId)
                .orElseThrow(() -> new ReviewException(REVIEW_NOT_FOUND));
    }
}
