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

    @PostAuthorize("isAuthenticated() " +
            "and returnObject.member.email == principal.username")
    @Transactional
    public ReviewDto update(Update.Request request) {
        Review review = findByIdOrThrow(request.getReviewId());
        review.updateReview(request);
        return ReviewDto.fromEntity(review);
    }

    @Transactional
    public ReviewDto delete(Long reviewId, UserDetails userDetails) {
        Review review = findByIdOrThrow(reviewId);

        validateMember(userDetails, review);

        reviewRepository.deleteByReviewId(reviewId);
        return ReviewDto.fromEntity(review);
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
