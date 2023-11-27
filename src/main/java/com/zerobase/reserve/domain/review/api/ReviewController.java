package com.zerobase.reserve.domain.review.api;

import com.zerobase.reserve.domain.review.dto.Write;
import com.zerobase.reserve.domain.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/review")
@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    /**
     * 리뷰 작성
     */
    @PostMapping
    public ResponseEntity<Write.Response> write(
            @RequestBody @Valid Write.Request request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(reviewService.write(request, userDetails));
    }

    // 수정, 삭제
}
