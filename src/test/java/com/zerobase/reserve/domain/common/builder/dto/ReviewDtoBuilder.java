package com.zerobase.reserve.domain.common.builder.dto;

import com.zerobase.reserve.domain.common.builder.MemberBuilder;
import com.zerobase.reserve.domain.review.dto.model.ReviewDto;

import static com.zerobase.reserve.domain.common.constants.ReviewConstants.*;

public class ReviewDtoBuilder {
    public static ReviewDto reviewDto() {
        return ReviewDto.builder()
                .reviewId(ID)
                .contents(CONTENTS)
                .score(SCORE)
                .member(MemberBuilder.member())
                .build();
    }
}
