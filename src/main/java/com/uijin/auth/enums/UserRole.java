package com.uijin.auth.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {

    USER("USER", "일반사용자"),
    ADMIN("ADMIN", "관리자");

    private final String role;
    private final String description;
}
