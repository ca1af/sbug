package com.sparta.sbug.security.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 보안 예외 처리 DTO
 */
// lombok
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SecurityExceptionDto {

    private int statusCode;
    private String msg;

    /**
     * 생성자
     */
    public SecurityExceptionDto(int statusCode, String msg) {
        this.statusCode = statusCode;
        this.msg = msg;
    }
}