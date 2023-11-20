package com.zerobase.reserve.global.exception;

public class JsonToObjectException extends CustomException {
    public JsonToObjectException(ErrorCode errorCode) {
        super(errorCode);
    }
}
