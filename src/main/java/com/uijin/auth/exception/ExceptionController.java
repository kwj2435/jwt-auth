package com.uijin.auth.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(BaseApiException.class)
    public ResponseEntity<BaseErrorResponse> handleBaseApiException(BaseApiException e) {
        return new ResponseEntity<>(new BaseErrorResponse(e.getCode(), e.getMessage()), e.getStatus());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(new BaseErrorResponse("code101", e.getMessage()));
    }
}
