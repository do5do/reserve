package com.zerobase.reserve.domain.review.exception;

import com.zerobase.reserve.global.exception.CustomException;
import com.zerobase.reserve.global.exception.ErrorCode;

/**
 * 리뷰 도메인에서 발생하는 예외를 담습니다.
 */
public class ReviewException extends CustomException {
    public ReviewException(ErrorCode errorCode) {
        super(errorCode);
    }
}
