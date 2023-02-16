package com.sparta.sbug.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.sbug.security.dto.SecurityExceptionDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

// springframework stereotype
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private static final SecurityExceptionDto exceptionDto =
            new SecurityExceptionDto(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase());

    /**
     * 발생한 <code>AccessDeniedException</code>을 처리하기 위한 메서드
     *
     * @param request               <code>AccessDeniedException</code>이 발생한 요청
     * @param response              사용자에게 오류를 알릴 응답
     * @param accessDeniedException 처리기 호출의 원인이 된 예외
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());

        try (OutputStream os = response.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(os, exceptionDto);
            os.flush();
        }
    }
}