package com.zerobase.reserve.domain.reservation.exception;

import com.zerobase.reserve.global.exception.CustomException;
import com.zerobase.reserve.global.exception.ErrorCode;

/**
 * 예약 도메인에서 발생하는 예외를 담습니다.
 */
public class ReservationException extends CustomException {
    public ReservationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ReservationException(ErrorCode errorCode, String msg) {
        super(errorCode, msg);
    }
}
