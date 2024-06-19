package com.uijin.auth.security.service;

import com.uijin.auth.user.entity.UserTokenEntity;
import com.uijin.auth.security.enums.ApiExceptionCode;
import com.uijin.auth.security.exception.BaseApiException;
import com.uijin.auth.user.repository.UserTokenRepository;
import com.uijin.auth.security.utils.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final JwtUtils jwtUtils;
  private final UserTokenRepository userTokenRepository;

  @Transactional
  public void regenerateToken(String accessToken, String refreshToken, HttpServletResponse response) {
    if(!jwtUtils.isExpired(accessToken)) {
      throw BaseApiException.of(ApiExceptionCode.NOT_EXPIRED_TOKEN);
    }

    if(jwtUtils.isExpired(refreshToken)) {
      throw BaseApiException.of(ApiExceptionCode.RETRY_LOGIN);
    }

    long userId = Long.parseLong(jwtUtils.getClaim(accessToken, "userId"));
    String userName = jwtUtils.getClaim(accessToken, "username");
    String role = jwtUtils.getClaim(accessToken, "role");

    long rtUserId = Long.parseLong(jwtUtils.getClaim(refreshToken, "userId"));
    String rtUserName = jwtUtils.getClaim(refreshToken, "username");

    if(userId != rtUserId || userName != rtUserName) {
      throw BaseApiException.of(ApiExceptionCode.INVALID_TOKEN);
    }

    String newAccessToken = jwtUtils.createAccessToken(userId, userName, role);
    String newRefreshToken = jwtUtils.createRefreshToken(userId, userName);

    UserTokenEntity userTokenEntity = userTokenRepository.findById(userId).orElseThrow();
    userTokenEntity.updateRefreshToken(newRefreshToken);

    response.setHeader("Authorization", newAccessToken);
    response.setHeader("Refresh-Token", newRefreshToken);
  }
}
