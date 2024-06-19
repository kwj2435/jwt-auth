package com.uijin.auth.security.controller;

import com.uijin.auth.security.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/refresh")
  public void regenerateRefreshToken(HttpServletRequest request, HttpServletResponse response) {
    String accessToken = request.getHeader("Authorization").split(" ")[1];
    String refreshToken = request.getHeader("Refresh-Token");

    authService.regenerateToken(accessToken, refreshToken, response);
  }
}
