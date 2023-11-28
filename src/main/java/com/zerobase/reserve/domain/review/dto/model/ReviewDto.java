package com.zerobase.reserve.domain.review.dto.model;

import com.zerobase.reserve.domain.member.entity.Member;
import com.zerobase.reserve.domain.review.entity.Review;
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
    private Integer score;
    private Member member;

    public static ReviewDto fromEntity(Review review) {
        return ReviewDto.builder()
                .reviewId(review.getId())
                .contents(review.getContents())
                .score(review.getScore())
                .member(review.getMember())
                .build();
    }
}
