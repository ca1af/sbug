package com.sparta.sbug.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.sbug.common.exceptions.dto.ExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

// springframework stereotype
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final ExceptionResponse EXCEPTION_RESPONSE =
            new ExceptionResponse(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.name(), 401, HttpStatus.UNAUTHORIZED.getReasonPhrase());

    /**
     * 발생한 <code>AuthenticationException</code>을 처리하기 위한 메서드
     *
     * @param request                 <code>AuthenticationException</code>이 발생한 요청
     * @param response                사용자가 인증을 시작할 수 있도록 하는 반환
     * @param authenticationException 호출의 원인이 된 예외
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authenticationException) throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        try (OutputStream os = response.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(os, EXCEPTION_RESPONSE);
            os.flush();
        }
    }
}