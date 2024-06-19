package com.uijin.auth.filter;

import com.uijin.auth.entity.UserEntity;
import com.uijin.auth.model.CustomUserDetails;
import com.uijin.auth.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.stream.Collectors;

/**
 * LoginFilter의 경우 UsernamePasswordAuthenticationFilter를 상속 받아 만들었기 때문에
 * 기본적으로 설정된 POST '/login' 경로에만 동작한다.
 */
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    // UserDetailsService Bean 사용
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    // UsernamePasswordAuthenticationToken 클래스의 객체는 두번 생성되는데 다음과 같은 경우이다.
    // 1. 로그인 인증 전 - 로그인 시 입력받은 정보를 담아서 전달할 때 사용
    // 2. JWT 검증 완료시 - JWT 검증이 완료된 유저의 정보를 SecurityContext에 저장할때 사용
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 클라이언트 요청에서 username, password 추출
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        // Spring Security 스펙
        // username, password 검증을 위해 token에 담는다.
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(username, password, null);

        // token 검증을 위해 authenticationManager로 전달
        return authenticationManager.authenticate(authToken);
    }

    // 로그인 성공시 호출
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomUserDetails principal = (CustomUserDetails) authResult.getPrincipal();
        long userId = principal.getUserId();
        String username = principal.getUsername();
        String role = principal.getAuthorities().stream().toList().get(0).getAuthority();

        String accessToken = jwtUtils.createAccessToken(userId, username, role);
        String refreshToken = jwtUtils.createRefreshToken(userId, username);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("authorization", "Bearer " + accessToken);
        response.setHeader("refresh-token", refreshToken);

        // 해당 부분을 주석처리 하지 않으면 기존 formlogin 동작 로직이 실행되어, 로그인후 기본 경로 '/' 로 리다이렉션된다.
        // RestApi 방식으로 사용하기 위해서는 주석 필요
//        super.successfulAuthentication(request, response, chain, authResult);
    }

    // 로그인 실패시 호출
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(401);
    }
}
