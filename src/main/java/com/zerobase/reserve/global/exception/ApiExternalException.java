package com.zerobase.reserve.global.exception;

public class ApiExternalException extends CustomException {
    public ApiExternalException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ApiExternalException(ErrorCode errorCode, String msg) {
        super(errorCode, msg);
    }
}
