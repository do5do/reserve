package com.zerobase.reserve.domain.review.api;

import com.zerobase.reserve.domain.review.dto.DeleteResponse;
import com.zerobase.reserve.domain.review.dto.Update;
import com.zerobase.reserve.domain.review.dto.Write;
import com.zerobase.reserve.domain.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/reviews")
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
        return ResponseEntity.ok(Write.Response.from(
                reviewService.write(request, userDetails)));
    }

    /**
     * 리뷰 수정
     */
    @PatchMapping
    public ResponseEntity<Update.Response> update(
            @RequestBody @Valid Update.Request request) {
        return ResponseEntity.ok(Update.Response.from(
                reviewService.update(request)));
    }

    /**
     * 리뷰 삭제
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<DeleteResponse> delete(@PathVariable Long reviewId) {
        return ResponseEntity.ok(DeleteResponse.from(
                reviewService.delete(reviewId)));
    }
}
