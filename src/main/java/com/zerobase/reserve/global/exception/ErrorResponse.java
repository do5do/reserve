package com.zerobase.reserve.global.exception;

public record ErrorResponse(
        ErrorCode errorCode,
        String message
) {

}
