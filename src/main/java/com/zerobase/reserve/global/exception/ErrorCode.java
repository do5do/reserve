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

    // reservation
    ALREADY_RESERVED(NOT_FOUND, "이미 예약된 일시입니다."),
    RESERVATION_NOT_FOUND(NOT_FOUND, "예약을 찾을 수 없습니다."),
    ARRIVAL_TIME_EXCEED(NOT_FOUND, "도착 시간이 지났습니다."),
    NOT_QUIT_RESERVATION(NOT_FOUND, "종료된 예약이 아닙니다."),
    RESERVATION_ACCESS_DENY(FORBIDDEN, "해당 예약에 접근할 수 없습니다."),

    // global
    INVALID_REQUEST(BAD_REQUEST, "올바르지 않은 요청입니다."),
    RESOURCE_NOT_FOUND(NOT_FOUND, "요청한 자원을 찾을 수 없습니다."),
    INTERNAL_ERROR(INTERNAL_SERVER_ERROR, "예상치 못한 에러가 발생했습니다.")
    ;

    private final HttpStatus status;
    private final String message;
}
