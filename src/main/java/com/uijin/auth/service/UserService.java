package com.uijin.auth.service;

import com.uijin.auth.entity.UserEntity;
import com.uijin.auth.model.UserModel;
import com.uijin.auth.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserModel.UserResponse addUser(UserModel.UserRequest userRequest) {
        UserEntity userRegistEntity = userRequest.toEntity();

        return userRepository.save(userRegistEntity).toUserDto();
    }
}
