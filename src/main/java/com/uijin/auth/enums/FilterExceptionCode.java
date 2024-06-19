package com.uijin.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FilterExceptionCode {
    INVALID_SIGNATUER(HttpStatus.UNAUTHORIZED, "F_401_10000", "Invalid JWT"),
    EXPIRED_JWT(HttpStatus.UNAUTHORIZED, "F_401_10001", "JWT Expired");

    private HttpStatus httpStatus;
    private String code;
    private String message;
}
