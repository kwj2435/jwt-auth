package com.uijin.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTokenEntity {

  @Id
  private Long userId;

  @Column(name = "REFRESH_TOKEN")
  private String refreshToken;
}
