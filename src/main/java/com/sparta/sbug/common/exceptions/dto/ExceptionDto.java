package com.sparta.sbug.common.exceptions.dto;

import lombok.Getter;

@Getter
public class ExceptionDto {
    private String message;
    private int status;

    public ExceptionDto(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
