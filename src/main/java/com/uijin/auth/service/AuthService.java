package com.uijin.auth.service;

import com.uijin.auth.enums.ApiExceptionCode;
import com.uijin.auth.exception.BaseApiException;
import com.uijin.auth.repository.UserTokenRepository;
import com.uijin.auth.utils.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final JwtUtils jwtUtils;
  private final UserTokenRepository userTokenRepository;

  public void regenerateToken(String accessToken, String refreshToken, HttpServletResponse response) {
    if(!jwtUtils.isExpired(accessToken)) {
      // AccessToken 유효기간 남아 있음
    }

    if(jwtUtils.isExpired(refreshToken)) {
      throw BaseApiException.of(ApiExceptionCode.RETRY_LOGIN);
    }


    // at,rt 재발급 및 rt DB 저장
    // response at, rt 세팅
  }
}
