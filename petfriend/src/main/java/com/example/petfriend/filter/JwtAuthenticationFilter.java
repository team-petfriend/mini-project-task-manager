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

    /** Authorization : HTTP 요청에서 인증 토큰(또는 자격증명)을 담는 표준 헤더 이름인 "Authorization"을 상수로 정의 */
    private static final String AUTH_HEADER = "Authorization";
    /** JwtProvider를 통해서 "Bearer"값을 가져온다 */
    private static final String BEARER_PREFIX = JwtProvider.BEARER_PREFIX;

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final UserPrincipalMapper principalMapper;

    /**
     * OncePerRequestFilter - 반드시 구현
     *  >> 스프링 시큐리티 필터가 매 요청마다 호출하는 핵심 메서드
     *  필요한 @Param (request, response, filterChain)이 존재
     * */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            /** .getContext()안에 이미 인증된 사용자 컨텍스트가 존재한다면 그대로 진행 */
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                /** doFilter => 현재 필터의 작업을 마치고, 체인의 다음 필터로 요청을 넘김 */
                filterChain.doFilter(request, response);
                return;
            }

            /** HttpMethod.OPTIONS는 그냥 통과 -> CROS 를 통해 사전 요청을 진행했음 */
            if (HttpMethod.OPTIONS.matches(request.getMethod())) {
                filterChain.doFilter(request, response);
                return;
            }

            /** Authorization 헤더에서 JWT 토큰 추출 */
            String authorization = request.getHeader(AUTH_HEADER);

            /** 추출한 JWT 토큰의 값이 존재하는지 검증*/
            if (authorization == null || authorization.isBlank()) {
                filterChain.doFilter(request, response);
                return;
            }

            /** Authorization 헤더의 "Bearer" 접두사가 존재하는지 여부 검증 */
            if (!authorization.startsWith(BEARER_PREFIX)) {
                unauthorized(response, "Authorization 헤더는 'Bearer <token>' 형식이어야 합니다.");
                return;
            }

            /** "Bearer"의 접두사 제거 -> 순수 토큰을 추출 */
            String token = jwtProvider.removeBearer(authorization);
            if (token.isBlank()) {
                unauthorized(response, "토큰이 존재하지 않습니다. 다시 확인해주세요.");
                return;
            }

            /** 토큰의 유효성을 검증 */
            if (!jwtProvider.isValidToken(token)) {
                unauthorized(response, "토큰이 유효하지 않거나 유효기간이 만료되었습니다.");
                return;
            }

            /** jwtProvider.getUsernameFromJwt 토큰을 통해 사용자의 값을 추출 */
            String username = jwtProvider.getUsernameFromJwt(token);

            /** == UserPrincipal Filter == */

            /** DB를 재조회해서 UserPrincipal 구성에 최신 권한/상태를 반영한다. */
            User user = userRepository.findByLoginId(username)
                    .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다."));

            /** user 데이터의 최신 권한을 반영한다. */
            UserPrincipal principal = principalMapper.map(user);

            /** SecurityContext에 인증 객체를 담아서 저장한다. */
            setAuthenticationContext(request, principal);

        } catch (Exception e) {
            logger.warn("JWT FILTER ERROR", e);
            unauthorized(response, "인증 처리 중 오류가 발생하였습니다.");
            return;
        }

        /** 정상적이라면 다음 필터로 request, response의 값을 넘겨준다.*/
        filterChain.doFilter(request, response);
    }

    private void setAuthenticationContext(HttpServletRequest request, UserPrincipal principal) {
        /** 사용자 아이디를 바탕으로 인증 토큰을 생성
         *  - 첫 번째 인자(Principal)
         *  - 두 번째 인자 (Credentials)
         *  - 세 번째 인자 (권한 목록)
         *  >>> username으로 사용자가 authorities 권한으로 인증이 완료가된 상태가 된다.
         * */


        /** 3가지의 인자를 담아서 새로운 토큰을 만들어준다. */
        AbstractAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

        /** 생성된 인증 토큰에 요청의 세부 사항 설정 */
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        /** 빈 SecurityContext 객체를 생성 */
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        /** 생성된 객체에 인증 토큰을 주입한다. */
        context.setAuthentication(authenticationToken);

        /** SecurityContextHolder에 인증토큰을 주입한 객체로 생성한다.
         *  컨트롤러, 서비스에서 사용자의 정보를 꺼내쓰기 위해서 사용
         * */
        SecurityContextHolder.setContext(context);
    }



    /** USER/ADMIN 라는 것을 -> "ROLE_"을 붙여 ROLE_USER / ROLE_ADMIN 으로 매핑  */
    private List<GrantedAuthority> toAuthorities(Set<String> roles) {
        if (roles == null || roles.isEmpty()) return List.of();
        return roles.stream()
                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    /** 401 응답 헬퍼(JSON 형태)
     *  추후 utils 파일에 클래스를 만들어 따로 보관
     * */
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
