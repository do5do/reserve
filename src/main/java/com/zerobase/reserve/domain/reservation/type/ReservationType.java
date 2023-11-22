package com.zerobase.reserve.domain.reservation.type;

import com.zerobase.reserve.domain.reservation.exception.ReservationException;
import com.zerobase.reserve.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum ReservationType {
    CONFIRM("예약 확정"),
    CANCEL("예약 취소"),
    WAIT("예약 대기"),
    QUIT("예약 종료");

    private final String description;

    public static ReservationType fromName(String dbData) {
        return Arrays.stream(values())
                .filter(o -> o.name().equals(dbData))
                .findFirst()
                .orElseThrow(() -> new ReservationException(
                        ErrorCode.INVALID_REQUEST, "잘못된 예약 타입입니다."));
    }
}
