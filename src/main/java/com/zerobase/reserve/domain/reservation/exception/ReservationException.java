package com.zerobase.reserve.domain.reservation.exception;

import com.zerobase.reserve.global.exception.CustomException;
import com.zerobase.reserve.global.exception.ErrorCode;

public class ReservationException extends CustomException {
    public ReservationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ReservationException(ErrorCode errorCode, String msg) {
        super(errorCode, msg);
    }
}
