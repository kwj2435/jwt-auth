package com.uijin.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FilterErrorCode {
    INVALID_SIGNATUER(HttpStatus.UNAUTHORIZED, "f_402_10000", "Invalid JWT");

    private HttpStatus httpStatus;
    private String code;
    private String message;
}
