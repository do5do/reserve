package com.zerobase.reserve.domain.member.exception;

import com.zerobase.reserve.global.exception.ErrorCode;

public class MemberException extends RuntimeException {
    private final ErrorCode errorCode;

    public MemberException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
