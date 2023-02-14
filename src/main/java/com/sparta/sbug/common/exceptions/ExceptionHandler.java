package com.sparta.sbug.common.exceptions;

import com.sparta.sbug.common.exceptions.dto.ExceptionDto;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

@RestControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ExceptionDto exceptionDto = new ExceptionDto(Objects.requireNonNull(ex.getFieldError()).getDefaultMessage(), 400);
        return new ResponseEntity<>(exceptionDto, HttpStatus.BAD_REQUEST);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    public ExceptionDto illegalHandler(IllegalArgumentException e){
        return new ExceptionDto(e.getMessage(), 400);
    }

    //ConstraintViolationException
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(ConstraintViolationException.class)
    public ExceptionDto sizeHandler(ConstraintViolationException e){
        return new ExceptionDto(e.getLocalizedMessage(), 400);
    }
}
