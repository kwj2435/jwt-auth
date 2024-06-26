package com.uijin.auth.security.exception;

import com.uijin.auth.security.enums.ApiExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class BaseApiException extends RuntimeException{
    private HttpStatus status;
    private String code;
    private String message;

    public static BaseApiException of(ApiExceptionCode exceptionCode) {
        return new BaseApiException(
                exceptionCode.getStatus(),
                exceptionCode.getCode(),
                exceptionCode.getMessage()
        );
    }
}
