package com.uijin.auth.service;

import com.uijin.auth.entity.UserEntity;
import com.uijin.auth.model.UserModel;
import com.uijin.auth.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    // Spring Security spec - 패스워드는 BCrypt Encoded 되어야 한다.
    public UserModel.UserResponse addUser(UserModel.UserRequest userRequest) {
        Optional<UserEntity> user = userRepository.findByUserName(userRequest.getUserName());

        if(user.isPresent()) {
            throw new InvalidParameterException("Already Registered Username");
        }

        UserEntity userRegistEntity = userRequest.toEntity(passwordEncoder);

        return userRepository.save(userRegistEntity).toUserDto();
    }

    public UserModel.UserResponse getUserInfo(long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                        new InvalidParameterException("userId can be null!!")).toUserDto();
    }
}
