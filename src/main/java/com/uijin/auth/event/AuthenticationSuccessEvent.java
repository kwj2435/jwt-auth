package com.uijin.auth.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.security.core.Authentication;

@Getter
public class AuthenticationSuccessEvent extends ApplicationEvent {

  private final String refreshToken;
  private final Authentication authentication;

  public AuthenticationSuccessEvent(Object source, String refreshToken, Authentication authentication) {
    super(source);
    this.refreshToken = refreshToken;
    this.authentication = authentication;
  }
}
