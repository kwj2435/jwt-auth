package com.uijin.auth.service;

import com.uijin.auth.entity.UserEntity;
import com.uijin.auth.model.UserModel;
import com.uijin.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserModel.UserResponse addUser(UserModel.UserRequest userRequest) {
        UserEntity userRegistEntity = userRequest.toEntity();

        return userRepository.save(userRegistEntity).toUserDto();
    }
}
