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
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 1001, "해당 이메일의 유저를 찾을 수 없었습니다."),
    USER_PASSWORD_NOT_MATCH(HttpStatus.FORBIDDEN, 1002, "비밀번호가 일치하지 않습니다."),

    // 채널 오류 (2000)
    BAD_REQUEST_CHANNEL_NAME(HttpStatus.BAD_REQUEST, 2001, "채널 이름에 공백을 입력할 수 없습니다😨"),
    USER_CHANNEL_FORBIDDEN(HttpStatus.FORBIDDEN, 2002, "유저의 채널 접근 권한이 없습니다."),
    CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, 2003, "채널을 찾을 수 없습니다."),
    DUPLICATE_USER_CHANNEL(HttpStatus.BAD_REQUEST, 2004, "중복된 유저-채널 리소스가 존재합니다."),
    USER_CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, 2005, "유저가 채널의 구성원이 아닙니다."),
    USER_CHANNEL_DUPLICATED(HttpStatus.BAD_REQUEST, 2006, "이미 초대했습니다."),

    // 쓰레드, 쓰레드 이모지 오류 (3000)
    BAD_REQUEST_THREAD_CONTENT(HttpStatus.BAD_REQUEST, 3001, "쓰레드 내용에 공백을 입력할 수 없습니다😨"),
    USER_THREAD_FORBIDDEN(HttpStatus.FORBIDDEN, 3002, "쓰레드에 대한 권한이 없습니다."),
    THREAD_NOT_FOUND(HttpStatus.NOT_FOUND, 3003, "쓰레드를 찾을 수 없습니다."),
    THREAD_EMOJI_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 3004, "쓰레드에 이모지 반응을 할 수 없었습니다!"),

    // 코멘트, 코멘트 이모지 오류 (4000)
    BAD_REQUEST_COMMENT_CONTENT(HttpStatus.BAD_REQUEST, 4001, "코멘트 내용에 공백을 입력할 수 없습니다😨"),
    USER_COMMENT_FORBIDDEN(HttpStatus.FORBIDDEN, 4002, "코멘트에 대한 권한이 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, 4003, "코멘트를 찾을 수 없습니다."),
    COMMENT_EMOJI_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 4004, "코멘트에 이모지 반응을 할 수 없었습니다!"),

    // 스케쥴 오류 (5000)

    // 채팅, 채팅방 오류 (6000)

    // 인증, 인가, 보안 오류 (7000)
    CREDENTIAL_EXPIRATION(HttpStatus.BAD_REQUEST, 7001, "리프레쉬 토큰이 만료되었습니다."),

    // 관리자 오류 (8000)
    DUPLICATE_ADMIN(HttpStatus.BAD_REQUEST, 8001, "해당 이메일의 관리자가 이미 존재합니다."),
    ADMIN_NOT_FOUND(HttpStatus.NOT_FOUND, 8002, "해당 이메일의 관리자를 찾을 수 없었습니다."),
    ADMIN_PASSWORD_NOT_MATCH(HttpStatus.FORBIDDEN, 8003, "비밀번호가 일치하지 않습니다."),

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