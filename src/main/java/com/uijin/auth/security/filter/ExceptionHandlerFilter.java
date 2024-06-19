package com.uijin.auth.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uijin.auth.security.enums.FilterExceptionCode;
import com.uijin.auth.security.exception.BaseErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (SignatureException se) {
            setErrorResponse(response, FilterExceptionCode.INVALID_SIGNATUER);
        } catch (ExpiredJwtException ee) {
            setErrorResponse(response, FilterExceptionCode.EXPIRED_JWT);
        }
    }

    private void setErrorResponse(HttpServletResponse response, FilterExceptionCode errorCode) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        BaseErrorResponse errorResponse = new BaseErrorResponse(errorCode.getCode(), errorCode.getMessage());

        try{
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }catch (IOException e){
            log.error(e.getMessage());
            throw e;
        }
    }
}
