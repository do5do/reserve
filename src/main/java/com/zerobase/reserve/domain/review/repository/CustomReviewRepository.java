package com.zerobase.reserve.domain.review.repository;

import com.zerobase.reserve.domain.review.entity.Review;

import java.util.Optional;

public interface CustomReviewRepository {
    Optional<Review> findByIdFetchJoin(Long id);

    void deleteByReviewId(Long id);
}
