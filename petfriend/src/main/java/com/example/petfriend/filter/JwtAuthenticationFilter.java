package com.example.petfriend.filter;

import com.example.petfriend.entity.User;
import com.example.petfriend.provider.JwtProvider;
import com.example.petfriend.repository.UserRepository;
import com.example.petfriend.security.UserPrincipal;
import com.example.petfriend.security.UserPrincipalMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 2025-09-05 김승민
 JwtAuthenticationFilter 구현
*/

@Component
@RequiredArgsConstructor
/* == JwtAuthenticationFilter ==
   JWT 인증 필터 : 요청(request)에서 JWT 토큰을 추출해오는 기능
   Spring Security가 OncePerRequestFilter를 상속받아 매 요청마다 실행
*/
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = JwtProvider.BEARER_PREFIX;

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final UserPrincipalMapper principalMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                filterChain.doFilter(request, response);
                return;
            }

            if (HttpMethod.OPTIONS.matches(request.getMethod())) {
                filterChain.doFilter(request, response);
                return;
            }
            String authorization = request.getHeader(AUTH_HEADER);

            if (authorization == null || authorization.isBlank()) {
                filterChain.doFilter(request, response);
                return;
            }

            if (!authorization.startsWith(BEARER_PREFIX)) {
                unauthorized(response, "Authorization 헤더는 'Bearer <token>' 형식이어야 합니다.");
                return;
            }

            String token = jwtProvider.removeBearer(authorization);
            if (token.isBlank()) {
                unauthorized(response, "토큰이 존재하지 않습니다. 다시 확인해주세요.");
                return;
            }

            if (!jwtProvider.isValidToken(token)) {
                unauthorized(response, "토큰이 유효하지 않거나 유효기간이 만료되었습니다.");
                return;
            }

            String username = jwtProvider.getUsernameFromJwt(token);

            User user = userRepository.findByLoginId(username)
                    .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다."));

            UserPrincipal principal = principalMapper.map(user);

            setAuthenticationContext(request, principal);
        } catch (Exception e) {
            logger.warn("JWT FILTER ERROR", e);
            unauthorized(response, "인증 처리 중 오류가 발생하였습니다.");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthenticationContext(HttpServletRequest request, UserPrincipal principal) {
        AbstractAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContext context = SecurityContextHolder.createEmptyContext();

        context.setAuthentication(authenticationToken);

        SecurityContextHolder.setContext(context);
    }

    private List<GrantedAuthority> toAuthorities(Set<String> roles) {
        if (roles == null || roles.isEmpty()) return List.of();
        return roles.stream()
                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private void unauthorized(HttpServletResponse response, String message) throws  IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(
                """
                    {"result":"fail","message","%s"}
                """.formatted(message));
    }
}
