package com.sparta.sbug.common.exceptions.dto;

import lombok.Getter;

/**
 * 예외 DTO
 */
// lombok
@Getter
public class ExceptionDto {
    private String message;
    private int status;

    /**
     * 생성자
     *
     * @param message 오류 메세지
     * @param status  오류 상태 코드
     */
    public ExceptionDto(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
