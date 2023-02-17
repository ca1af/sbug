package com.sparta.sbug.common.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    // 일반 오류 (100)
    INVALID_CODE(HttpStatus.BAD_REQUEST, 101, "Invalid Code"),
    RESOURCE_NOT_FOUND(HttpStatus.NO_CONTENT, 102, "Resource not found"),
    EXPIRED_CODE(HttpStatus.BAD_REQUEST, 103, "Expired Code"),

    // 유저 오류 (1000)

    // 채널 오류 (2000)
    USER_CHANNEL_FORBIDDEN(HttpStatus.FORBIDDEN, 2403, "유저의 채널 접근 권한이 없습니다."),

    // 쓰레드, 쓰레드 이모지 오류 (3000)
    USER_THREAD_FORBIDDEN(HttpStatus.FORBIDDEN, 3403, "유저의 게시글 접근 권한이 없습니다."),

    // 코멘트, 코멘트 이모지 오류 (4000)

    // 스케쥴 오류 (5000)

    // 채팅, 채팅방 오류 (6000)

    // 인증, 인가, 보안 오류 (7000)
    CREDENTIAL_EXPIRATION(HttpStatus.BAD_REQUEST, 7001, "인증 정보가 만료되었습니다."),

    // AWS (8000)
    AWS_ERROR(HttpStatus.BAD_REQUEST, 8001, "aws client error");

    private HttpStatus status;
    private int code;
    private String message;

    ErrorCode(HttpStatus status, int code, String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

}