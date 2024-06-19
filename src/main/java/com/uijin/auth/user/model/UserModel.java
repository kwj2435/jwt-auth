package com.uijin.auth.user.model;

import com.uijin.auth.user.entity.UserEntity;
import com.uijin.auth.security.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserModel {

    @Getter
    @AllArgsConstructor
    public static class UserRequest{
        private String userName;

        private String password;

        public UserEntity toEntity(BCryptPasswordEncoder passwordEncoder) {
            return new UserEntity(
                    null,
                    this.userName,
                    passwordEncoder.encode(this.password),
                    UserRole.USER
            );
        }
    }

    @Getter
    @AllArgsConstructor
    public static class UserResponse{
        private long userId;

        private String userName;

        private UserRole userRole;
    }
}
