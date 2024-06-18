package com.uijin.auth.entity;

import com.uijin.auth.enums.UserRole;
import com.uijin.auth.model.UserModel;
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

    private String userName;

    private String password;

    private UserRole userRole;

    public UserModel.UserResponse toUserDto() {
        return new UserModel.UserResponse(userId, userName, userRole);
    }
}
