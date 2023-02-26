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
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 1404, "해당 이메일의 유저를 찾을 수 없었습니다."),
    USER_PASSWORD_NOT_MATCH(HttpStatus.FORBIDDEN, 1403, "비밀번호가 일치하지 않습니다."),

    // 채널 오류 (2000)
    USER_CHANNEL_FORBIDDEN(HttpStatus.FORBIDDEN, 2403, "유저의 채널 접근 권한이 없습니다."),
    CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, 2404, "채널을 찾을 수 없습니다."),

    // 쓰레드, 쓰레드 이모지 오류 (3000)
    BAD_REQUEST_THREAD_CONTENT(HttpStatus.BAD_REQUEST, 4001, "쓰레드 내용에 공백을 입력할 수 없습니다😨"),
    USER_THREAD_FORBIDDEN(HttpStatus.FORBIDDEN, 3403, "게시글에 대한 권한이 없습니다."),
    THREAD_NOT_FOUND(HttpStatus.NOT_FOUND, 3404, "쓰레드를 찾을 수 없습니다."),


    // 코멘트, 코멘트 이모지 오류 (4000)
    BAD_REQUEST_COMMENT_CONTENT(HttpStatus.BAD_REQUEST, 4001, "댓글 내용에 공백을 입력할 수 없습니다😨"),
    USER_COMMENT_FORBIDDEN(HttpStatus.FORBIDDEN, 4403, "유저의 게시글 접근 권한이 없습니다."),

    // 스케쥴 오류 (5000)

    // 채팅, 채팅방 오류 (6000)

    // 인증, 인가, 보안 오류 (7000)
    CREDENTIAL_EXPIRATION(HttpStatus.BAD_REQUEST, 7001, "리프레쉬 토큰이 만료되었습니다."),

    // 관리자 오류 (8000)
    DUPLICATE_ADMIN(HttpStatus.BAD_REQUEST, 8001, "해당 이메일의 관리자가 이미 존재합니다."),
    ADMIN_NOT_FOUND(HttpStatus.NOT_FOUND, 8404, "해당 이메일의 관리자를 찾을 수 없었습니다."),
    ADMIN_PASSWORD_NOT_MATCH(HttpStatus.FORBIDDEN, 8403, "비밀번호가 일치하지 않습니다."),

    // AWS (9000)
    AWS_ERROR(HttpStatus.BAD_REQUEST, 9001, "aws client error");

    private HttpStatus status;
    private int code;
    private String message;

    ErrorCode(HttpStatus status, int code, String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

}