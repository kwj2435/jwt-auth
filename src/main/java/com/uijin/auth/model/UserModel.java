package com.uijin.auth.model;

import com.uijin.auth.entity.UserEntity;
import com.uijin.auth.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class UserModel {

    @Getter
    @AllArgsConstructor
    public static class UserRequest{
        private String userName;

        private String password;

        public UserEntity toEntity() {
            return new UserEntity(null, this.userName, this.password, UserRole.USER);
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
