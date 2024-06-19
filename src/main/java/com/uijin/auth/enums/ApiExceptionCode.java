package com.uijin.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ApiExceptionCode {
    ALREADY_REGISTERED_USER(HttpStatus.BAD_REQUEST, "400_100000", "");

    private HttpStatus status;
    private String code;
    private String message;
}
