package com.zerobase.reserve.domain.reservation.type;

import com.zerobase.reserve.domain.reservation.exception.ReservationException;
import com.zerobase.reserve.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ReservationType {
    CONFIRM("예약확정"),
    CANCEL("예약취소"),
    WAIT("예약대기"),
    QUIT("예약종료");

    private final String description;

    public static ReservationType fromDescription(String dbData) {
        return Arrays.stream(values())
                .filter(o -> o.getDescription().equals(dbData))
                .findFirst()
                .orElseThrow(() -> new ReservationException(
                        ErrorCode.INVALID_REQUEST, "잘못된 예약 타입입니다."));
    }
}
