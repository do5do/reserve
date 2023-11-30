package com.zerobase.reserve.domain.common.builder;

import com.zerobase.reserve.domain.review.entity.Review;

import static com.zerobase.reserve.domain.common.constants.ReviewConstants.CONTENTS;
import static com.zerobase.reserve.domain.common.constants.ReviewConstants.SCORE;

public class ReviewBuilder {
    public static Review review() {
        return Review.builder()
                .contents(CONTENTS)
                .score(SCORE)
                .build();
    }
}
