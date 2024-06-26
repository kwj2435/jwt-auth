package com.uijin.auth.security.exception;

import org.hibernate.PropertyValueException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(BaseApiException.class)
    public ResponseEntity<BaseErrorResponse> handleBaseApiException(BaseApiException e) {
        return new ResponseEntity<>(new BaseErrorResponse(e.getCode(), e.getMessage()), e.getStatus());
    }

    // 필드 유효성 체크
    @ExceptionHandler(PropertyValueException.class)
    public ResponseEntity<BaseErrorResponse> handlerPropertyValueException(PropertyValueException e) {
        return ResponseEntity.badRequest().body(new BaseErrorResponse("400_000000", e.getMessage()));
    }
}
