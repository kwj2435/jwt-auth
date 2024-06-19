package com.uijin.auth.security.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseErrorResponse{
    private String code;
    private String message;
}