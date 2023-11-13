package com.zerobase.reserve.domain.repository;

import com.zerobase.reserve.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
