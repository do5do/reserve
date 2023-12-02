package com.zerobase.reserve.domain.review.dto.model;

import com.zerobase.reserve.domain.member.entity.Member;
import com.zerobase.reserve.domain.review.entity.Review;
import com.zerobase.reserve.domain.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private Long reviewId;
    private String contents;
    private Double score;
    private Member member;
    private Store store;

    public static ReviewDto fromEntity(Review review) {
        return ReviewDto.builder()
                .reviewId(review.getId())
                .contents(review.getContents())
                .score(review.getScore())
                .member(review.getMember())
                .store(review.getStore())
                .build();
    }
}
