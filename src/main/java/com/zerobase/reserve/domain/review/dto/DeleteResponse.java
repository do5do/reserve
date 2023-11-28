package com.zerobase.reserve.domain.review.dto;

import com.zerobase.reserve.domain.review.dto.model.ReviewDto;

public record DeleteResponse(Long reviewId) {
    public static DeleteResponse from(ReviewDto reviewDto) {
        return new DeleteResponse(reviewDto.getReviewId());
    }
}
