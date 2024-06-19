package com.uijin.auth.service;

import com.uijin.auth.entity.UserEntity;
import com.uijin.auth.model.CustomUserDetails;
import com.uijin.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = userRepository.findByUserName(username);

        // UserDetails에 담아 넘기게 되면 AuthenticationManager가 검증한다.
        // UserDetails의 메서드 로직을 커스텀하여 사용해야 하기 때문에 UserDetails를 상속 받아 CustomUserDetails를 구현한다.
        if(userEntity.isEmpty()) {
            throw new UsernameNotFoundException("Can't find username!");
        }
        return new CustomUserDetails(userEntity.get());
    }
}
