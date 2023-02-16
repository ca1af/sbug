package com.sparta.sbug.common.exceptions.dto;

import com.sparta.sbug.common.exceptions.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

/**
 * 예외 DTO
 */
// lombok
@Getter
public class ExceptionResponse {
    private int status;
    private String error;
    private int code;
    private String message;


    /**
     * 생성자
     *
     * @param message 오류 메세지
     * @param status  오류 상태 코드
     */
    @Builder
    public ExceptionResponse(int status, String error, int code, String message) {
        this.status = status;
        this.error = error;
        this.code = code;
        this.message = message;
    }

    public static ResponseEntity<ExceptionResponse> toResponseEntity(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getStatus())
                .body(ExceptionResponse.builder()
                        .status(errorCode.getStatus().value())
                        .error(errorCode.getStatus().name())
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage()).build());
    }
}
