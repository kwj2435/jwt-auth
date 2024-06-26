package com.uijin.auth.security.config;

import com.uijin.auth.security.filter.ExceptionHandlerFilter;
import com.uijin.auth.security.filter.JwtFilter;
import com.uijin.auth.security.filter.LoginFilter;
import com.uijin.auth.security.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtils jwtUtils;
    private final ApplicationEventPublisher eventPublisher;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, AuthenticationConfiguration authenticationConfiguration) throws Exception {
        httpSecurity
            .csrf(AbstractHttpConfigurer::disable) // CSRF disable
            .formLogin(AbstractHttpConfigurer::disable) // Form Login disable
            .httpBasic(AbstractHttpConfigurer::disable);    // Http basic 인증 방식 disable

        // 경로별 인가 여부
        httpSecurity
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/login", "/api/v1/user/join", "/error", "/", "/api/v1/auth/**").permitAll()
                .requestMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated());

        // 필터 추가
        httpSecurity
            .addFilterBefore(new ExceptionHandlerFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new JwtFilter(jwtUtils), LoginFilter.class)
            .addFilterAt(new LoginFilter(jwtUtils, authenticationManager(authenticationConfiguration), eventPublisher), UsernamePasswordAuthenticationFilter.class);

        // 세션 설정
        httpSecurity
            .sessionManagement((session) ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
