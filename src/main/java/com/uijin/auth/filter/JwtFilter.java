package com.uijin.auth.filter;

import com.uijin.auth.entity.UserEntity;
import com.uijin.auth.enums.UserRole;
import com.uijin.auth.model.CustomUserDetails;
import com.uijin.auth.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Filter는 SpringFilterChain에서 추가한 LoginFilter 앞에 위치시킨다.
 * JWT는 요청마다 매번 검증되어야 하기 때문에 OncePerRequestFilter를 상속받는다.
 */
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    public JwtFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    // Request의 Authorization 헤더에 포함된 토큰을 추출하여 검증을 진행한다.
    // 인증된 사용자 정보를 SecurityContextHolder에 저장되며, 이 후 로직에서 해당 요청이 인증 완료된 요청으로 간주되어 통과한다.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if(authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.split(" ")[1];

        if (jwtUtils.isExpired(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtUtils.getUsername(token);
        String role = jwtUtils.getRole(token);

        //userEntity를 생성하여 값 set
        UserEntity userEntity = UserEntity.builder()
                .userName(username)
                .userRole(UserRole.valueOf(role))
                .build();

        //UserDetails에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

        //토큰에서 추출한 사용자 정보를 담은 Authentication 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
