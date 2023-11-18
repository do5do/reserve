package com.zerobase.reserve.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    MEMBER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 회원입니다."),

    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "매장을 찾을 수 없습니다."),

    ;

    private final HttpStatus status;
    private final String message;
}
