package com.zerobase.reserve.domain.review.repository;

import com.zerobase.reserve.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long>, CustomReviewRepository {
}
