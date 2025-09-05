package com.example.petfriend.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 2025-09-05 김승민
 JsonAccessDeniedHandler 구현
**/

@Component
public class JsonAccessDeniedHandler implements AccessDeniedHandler {

    /** == AccessDeniedHandler 인터페이스 ==
     * 인증은 완료되었는데 요청에 대한 권한을 가지고 있지 않은 사용자가 엔드포인트에 접근하면 발생
     * 403 ERROR (FORBIDDEN) 전용 핸들러
     * */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write("{\"result\":fail\",\"message\":\"요청에 대한 권한을 가지고 있지않습니다.\"}");
    }
}
