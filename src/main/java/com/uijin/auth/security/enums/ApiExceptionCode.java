package com.uijin.auth.security.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ApiExceptionCode {
    ALREADY_REGISTERED_USER(HttpStatus.BAD_REQUEST, "400_100000", "Already registered user"),
    RETRY_LOGIN(HttpStatus.UNAUTHORIZED, "401_1000000", "Please login again"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "401_1000001", "Invalid Token"),
    NOT_EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "400_1000001", "Token is not expired yet");

    private HttpStatus status;
    private String code;
    private String message;
}
