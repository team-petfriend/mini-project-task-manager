package com.example.petfriend.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 2025-09-05 김승민
 JsonAuthenticationEntryPoint 구현
* */

@Component
public class JsonAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /** == AuthenticationEntryPoint 인터페이스 ==
     * 인증되지 않은 사용자가 인증이 필요한 엔드포인트로 접근할 때 발생
     * 401 전용 ERROR (UNAUTHORIZED) 핸들러
     * */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write("{\"result\":\"fail\",\"message\":\"인증이 필요합니다.\"}");
    }
}
