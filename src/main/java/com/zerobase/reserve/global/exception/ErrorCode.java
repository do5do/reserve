package com.zerobase.reserve.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // member
    MEMBER_NOT_FOUND(NOT_FOUND, "회원을 찾을 수 없습니다."),
    MEMBER_ALREADY_EXISTS(BAD_REQUEST, "이미 존재하는 회원입니다."),

    // store
    STORE_NOT_FOUND(NOT_FOUND, "매장을 찾을 수 없습니다."),
    STORE_CAN_NOT_UPDATE(CONFLICT, "매장 정보를 수정할 수 없습니다."),
    STORE_ALREADY_EXISTS(NOT_FOUND, "이미 존재하는 매장입니다."),

    // reservation
    ALREADY_RESERVED(NOT_FOUND, "이미 예약된 일시입니다."),

    // global
    INVALID_REQUEST(BAD_REQUEST, "올바르지 않은 요청입니다."),
    RESOURCE_NOT_FOUND(NOT_FOUND, "요청한 자원을 찾을 수 없습니다."),
    INTERNAL_ERROR(INTERNAL_SERVER_ERROR, "예상치 못한 에러가 발생했습니다.")
    ;

    private final HttpStatus status;
    private final String message;
}
