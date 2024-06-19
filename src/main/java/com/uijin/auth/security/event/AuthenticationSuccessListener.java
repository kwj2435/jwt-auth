package com.uijin.auth.security.event;

import com.uijin.auth.user.entity.UserTokenEntity;
import com.uijin.auth.security.model.CustomUserDetails;
import com.uijin.auth.user.repository.UserTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

// Login Filter 에서 로그인 성공후 생성된 Refresh Token을 DB에 저장하기 위한 이벤트 리스너
@Component
@RequiredArgsConstructor
public class AuthenticationSuccessListener {

    private final UserTokenRepository userTokenRepository;

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        String refreshToken = event.getRefreshToken();
        CustomUserDetails principal = (CustomUserDetails)event.getAuthentication().getPrincipal();

        long userId = principal.getUserId();
        UserTokenEntity userTokenEntity = UserTokenEntity.builder().userId(userId)
            .refreshToken(refreshToken).build();

        userTokenRepository.save(userTokenEntity);
    }
}
