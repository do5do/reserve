package com.zerobase.reserve.domain.review.service;

import com.zerobase.reserve.domain.reservation.entity.Reservation;
import com.zerobase.reserve.domain.reservation.exception.ReservationException;
import com.zerobase.reserve.domain.reservation.service.ReservationService;
import com.zerobase.reserve.domain.reservation.type.ReservationType;
import com.zerobase.reserve.domain.review.dto.Write;
import com.zerobase.reserve.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.zerobase.reserve.global.exception.ErrorCode.NOT_QUIT_RESERVATION;
import static com.zerobase.reserve.global.exception.ErrorCode.RESERVATION_ACCESS_DENY;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReservationService reservationService;

    @Transactional
    public Write.Response write(Write.Request request, UserDetails userDetails) {
        Reservation reservation = reservationService
                .getReservationFetchJoinOrThrow(request.getReservationKey());

        validateReservation(reservation, userDetails);

        reviewRepository.save(request.toEntity(reservation));
        return new Write.Response(reservation.getId());
    }

    private static void validateReservation(Reservation reservation,
                                            UserDetails userDetails) {
        if (reservation.getReservationType() != ReservationType.QUIT) {
            throw new ReservationException(NOT_QUIT_RESERVATION);
        }

        if (!reservation.getMember().getEmail().equals(userDetails.getUsername())) {
            throw new ReservationException(RESERVATION_ACCESS_DENY);
        }
    }
}
