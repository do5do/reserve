package com.zerobase.reserve.domain.store.exception;

import com.zerobase.reserve.global.exception.CustomException;
import com.zerobase.reserve.global.exception.ErrorCode;

public class StoreException extends CustomException {
    public StoreException(ErrorCode errorCode) {
        super(errorCode);
    }
}
