package com.uijin.auth.event;

import com.uijin.auth.service.UserService;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

// Login Filter 에서 로그인 성공후 생성된 Refresh Token을 DB에 저장하기 위한 이벤트 리스너
@Component
public class AuthenticationSuccessListener {

    private final UserService userService;

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {

    }
}
