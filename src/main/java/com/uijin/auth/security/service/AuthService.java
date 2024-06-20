package com.uijin.auth.security.service;

import com.uijin.auth.user.entity.UserTokenEntity;
import com.uijin.auth.security.enums.ApiExceptionCode;
import com.uijin.auth.security.exception.BaseApiException;
import com.uijin.auth.user.repository.UserTokenRepository;
import com.uijin.auth.security.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final JwtUtils jwtUtils;
  private final UserTokenRepository userTokenRepository;

  @Transactional
  public void regenerateToken(String accessToken, String refreshToken, HttpServletResponse response) {
    Claims userClaims = jwtUtils.getClaimsFromExpiredToken(accessToken);

    if(userClaims == null) {
      throw BaseApiException.of(ApiExceptionCode.NOT_EXPIRED_TOKEN);
    }
    if(jwtUtils.isExpired(refreshToken)) {
      throw BaseApiException.of(ApiExceptionCode.RETRY_LOGIN);
    }

    long userId = (int)userClaims.get("userId");
    String userName = userClaims.getSubject();
    String role = (String)userClaims.get("role");

    UserTokenEntity userTokenEntity = userTokenRepository.findById(userId)
            .orElseThrow(() -> BaseApiException.of(ApiExceptionCode.INVALID_TOKEN));

    if(!StringUtils.equals(refreshToken, userTokenEntity.getRefreshToken())) {
      throw BaseApiException.of(ApiExceptionCode.INVALID_TOKEN);
    }

    String newAccessToken = jwtUtils.createAccessToken(userId, userName, role);
    String newRefreshToken = jwtUtils.createRefreshToken();

    userTokenEntity.updateRefreshToken(newRefreshToken);

    response.setHeader("Authorization", newAccessToken);
    response.setHeader("Refresh-Token", newRefreshToken);
  }
}
