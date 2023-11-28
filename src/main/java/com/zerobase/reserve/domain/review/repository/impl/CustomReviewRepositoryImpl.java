package com.zerobase.reserve.domain.review.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.reserve.domain.review.entity.Review;
import com.zerobase.reserve.domain.review.repository.CustomReviewRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.zerobase.reserve.domain.member.entity.QMember.member;
import static com.zerobase.reserve.domain.review.entity.QReview.review;

@RequiredArgsConstructor
public class CustomReviewRepositoryImpl implements CustomReviewRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Review> findByIdFetchJoin(Long id) {
        return Optional.ofNullable(queryFactory
                .selectFrom(review)
                .join(review.member, member).fetchJoin()
                .where(review.id.eq(id))
                .fetchOne());
    }

    @Override
    public void deleteByReviewId(Long id) {
        queryFactory
                .delete(review)
                .where(review.id.eq(id))
                .execute();
    }
}
