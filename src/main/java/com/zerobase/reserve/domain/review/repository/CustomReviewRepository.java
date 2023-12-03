package com.zerobase.reserve.domain.review.repository;

import com.zerobase.reserve.domain.review.entity.Review;

import java.util.Optional;

public interface CustomReviewRepository {
    Optional<Review> findByIdFetchJoin(Long id);

    /**
     * 리뷰 id로 리뷰를 삭제하는 쿼리입니다.
     * JPA repository에 등록된 delete 메서드 수행 시 select 후 delete로 쿼리가 두번 실행되기 때문에
     * delete만 수행하는 쿼리를 따로 작성하였습니다.
     */
    void deleteByReviewId(Long id);
}
