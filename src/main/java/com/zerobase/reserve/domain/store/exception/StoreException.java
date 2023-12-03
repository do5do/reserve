package com.zerobase.reserve.domain.store.exception;

import com.zerobase.reserve.global.exception.CustomException;
import com.zerobase.reserve.global.exception.ErrorCode;

/**
 * 매장 도메인에서 발생하는 예외를 담습니다.
 */
public class StoreException extends CustomException {
    public StoreException(ErrorCode errorCode) {
        super(errorCode);
    }
}
