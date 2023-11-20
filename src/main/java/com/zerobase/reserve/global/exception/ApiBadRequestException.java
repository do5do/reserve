package com.zerobase.reserve.global.exception;

public class ApiBadRequestException extends CustomException {
    public ApiBadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
