package com.zerobase.reserve.domain.review.api;

import com.zerobase.reserve.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/review")
@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

}
