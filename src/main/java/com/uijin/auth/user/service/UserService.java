package com.uijin.auth.user.service;

import com.uijin.auth.user.repository.UserRepository;
import com.uijin.auth.user.entity.UserEntity;
import com.uijin.auth.security.enums.ApiExceptionCode;
import com.uijin.auth.security.exception.BaseApiException;
import com.uijin.auth.user.model.UserModel;
import com.uijin.auth.user.repository.UserTokenRepository;
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
    private final UserTokenRepository userTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    // Spring Security spec - 패스워드는 BCrypt Encoded 되어야 한다.
    public UserModel.UserResponse addUser(UserModel.UserRequest userRequest) {
        Optional<UserEntity> user = userRepository.findByUserName(userRequest.getUserName());

        if(user.isPresent()) {
            throw BaseApiException.of(ApiExceptionCode.ALREADY_REGISTERED_USER);
        }
        UserEntity userRegistEntity = userRequest.toEntity(passwordEncoder);

        return userRepository.save(userRegistEntity).toUserDto();
    }

    public UserModel.UserResponse getUserInfo(long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                        new InvalidParameterException("userId can be null!!")).toUserDto();
    }
}
