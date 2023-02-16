package com.sparta.sbug.common.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCodeEnum implements EnumModel {

    // COMMON
    INVALID_CODE(HttpStatus.BAD_REQUEST, "C001", "Invalid Code"),
    RESOURCE_NOT_FOUND(HttpStatus.NO_CONTENT, "C002", "Resource not found"),
    EXPIRED_CODE(HttpStatus.BAD_REQUEST, "C003", "Expired Code"),

    // AWS
    AWS_ERROR(HttpStatus.BAD_REQUEST, "A001", "aws client error");

    private HttpStatus httpStatus;
    private String code;
    private String message;

    ErrorCodeEnum(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.code = code;
    }

    @Override
    public String getKey() {
        return this.code;
    }

    @Override
    public String getValue() {
        return this.message;
    }
}