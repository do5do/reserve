package com.zerobase.reserve.domain.member.exception;

import com.zerobase.reserve.global.exception.CustomException;
import com.zerobase.reserve.global.exception.ErrorCode;

public class MemberException extends CustomException {
    public MemberException(ErrorCode errorCode) {
        super(errorCode);
    }
}
