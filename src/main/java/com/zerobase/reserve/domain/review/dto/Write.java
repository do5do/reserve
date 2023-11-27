package com.zerobase.reserve.domain.review.dto;

import com.zerobase.reserve.domain.reservation.entity.Reservation;
import com.zerobase.reserve.domain.review.entity.Review;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class Write {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank
        private String reservationKey;

        @NotBlank
        private String contents;

        @NotNull
        private Integer score;

        public Review toEntity(Reservation reservation) {
            Review review = Review.builder()
                    .contents(contents)
                    .score(score)
                    .build();

            review.addMemberAndStore(reservation.getMember(),
                    reservation.getStore());
            return review;
        }
    }

    public record Response(Long reviewId) {
    }
}
