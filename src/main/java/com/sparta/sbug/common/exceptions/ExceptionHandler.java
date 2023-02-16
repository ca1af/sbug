package com.sparta.sbug.common.exceptions;

import com.sparta.sbug.common.exceptions.dto.ExceptionDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;
import java.util.Objects;

// springframework web bind
@RestControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Valid 어노테이션이 붙은 필드에서 발생한 <code>MethodArgumentNotValidException</code>을 처리하기 위한 메서드
     *
     * @param ex      처리 대상 예외
     * @param headers 응답에 담아 보낼 헤더
     * @param status  선택된 응답 상태
     * @param request 현재 요청
     * @return ResponseEntity
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ExceptionDto exceptionDto = new ExceptionDto(Objects.requireNonNull(ex.getFieldError()).getDefaultMessage(), 400);
        return new ResponseEntity<>(exceptionDto, HttpStatus.BAD_REQUEST);
    }

    /**
     * 발생한 <code>IllegalArgumentException</code>을 처리하기 위한 메서드
     *
     * @param e 처리 대상 예외
     * @return ExceptionDto
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    public ExceptionDto handleIllegalArgumentException(IllegalArgumentException e) {
        return new ExceptionDto(e.getMessage(), 400);
    }


    /**
     * 발생한 <code>NoSuchElementException</code>을 처리하기 위한 메서드
     *
     * @param e 처리 대상 예외
     * @return ExceptionDto
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @org.springframework.web.bind.annotation.ExceptionHandler(NoSuchElementException.class)
    public ExceptionDto handleNoSuchElementException(NoSuchElementException e) {
        return new ExceptionDto(e.getMessage(), 404);
    }
}
