package com.uijin.auth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {

        return ResponseEntity.badRequest().body(new BaseErrorResponse("code101", e.getMessage()));
    }

    @Getter
    @AllArgsConstructor
    public static class BaseErrorResponse{
        private String code;
        private String message;
    }
}
