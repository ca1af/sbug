package com.sparta.sbug.security.exception;

public class ForbiddenException extends RuntimeException {

    /**
     * 권한 오류 커스텀 예외
     *
     * @param message 오류 메세지
     */
    public ForbiddenException(String message) {
        super(message);
    }
}
