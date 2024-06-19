package com.uijin.auth.user.entity;

import com.uijin.auth.security.enums.UserRole;
import com.uijin.auth.user.model.UserModel.UserResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "USER_NAME", nullable = false)
    private String userName;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "USER_ROLE", nullable = false)
    private UserRole userRole;

    public UserResponse toUserDto() {
        return new UserResponse(userId, userName, userRole);
    }
}
