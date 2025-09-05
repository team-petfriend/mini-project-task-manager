package com.example.petfriend.config;
import com.example.petfriend.filter.JwtAuthenticationFilter;
import com.example.petfriend.handler.JsonAccessDeniedHandler;
import com.example.petfriend.handler.JsonAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
  2025-09-05 김승민
  SpringSecurity, Auth 기능구현
*/



@Configuration // Spring의 설정 클래스로 사용됨을 명시
@EnableWebSecurity // Spring Security의 웹 보안 활성화(스프링 시큐리티 기능을 적용)
@RequiredArgsConstructor // 생성자 주입이 필요한 생성자를 대신 작성해준다.
public class WebSecurityConfig {
        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final JsonAuthenticationEntryPoint authenticationEntryPoint;
        private final JsonAccessDeniedHandler accessDeniedHandler;

        /**springframework 프레임워크의 어노테이션 @Value를 사용하여야 한다.
         * @Configuration 어노테이션으로 properties 사용함을 명시해준다.
         * */

        @Value("${cors.allowed-origins:*}")
        private String allowedOrigins; // 설정에서 허용할 출처를 지정.

        @Value("${cors.allowed-headers:*}")
        private String allowedHeaders; // 헤더에 어떤 걸 허용할지 설정.

        @Value("${cors.allowed-methods:GET, POST, PUT, PATCH, DELETE, OPTIONS}")
        private String allowedMethods; // 클라이언트가 사용 할 수 있는 HTTP 메서드 지정.

        @Value("${cors.exposed:Authorization,Set-cookie}")
        private String exposedHeaders;

        /** 로컬 개발 시 개발용 H2 콘솔 접근 허용 여부 */
        @Value("${security.h2-console:true}")
        private boolean h2ConsoleEnabled;

        /** 비밀번호 인코더: BCrypt 설정 */
        @Bean // 메서드를 반환하기 위해서 객체를 스프링 빈으로 등록해준다.
        public BCryptPasswordEncoder passwordEncoder() {
            // 새로운 비밀번호 인코더를 반환
            return new BCryptPasswordEncoder();
        }

        /** == AuthenticationManager 기능 ==
         * 클라이언트가 로그인 시도 ->
         * Authentication 생성 ->
         * AuthenticationManager에 전달 ->
         * AuthenticationManager ->
         * 내부적으로 AuthenticationProvider가 실제 인증을 처리 */
        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
            return configuration.getAuthenticationManager();
        }

    /** CORS : 브라우저에서 다른 도멘인으로 리소스를 요청할 때 발생하는 보안
     *  REST API 사용 시 다른 도메인에서 API에 접근할 수 있도록 하용
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        /** 출처/헤더/메서드/쿠키 허용 등을 담는 CORS 정책 객체 */
        CorsConfiguration config = new CorsConfiguration();

        /** CORS 에서 자격증반을 동반한 교차 출처를 허용 */
        config.setAllowCredentials(true);

        /** 오리진(Origin 설정 ) */

        /** == 요청 헤더 ==
         * String allowedHeaders " Authorization,Content-Type,X-Requested-With " 형태로 들어온다 */
        config.setAllowedHeaders(splitToList(allowedHeaders));

        /** == 요청 허용 메서드 ==
            splitToList -> cors.allowed-methods:GET, POST, PUT, PATCH, DELETE, OPTIONS */
        config.setAllowedMethods(splitToList(allowedMethods));

        /** 클라이언트가 읽을 수 있는 헤더 */
        config.setExposedHeaders(splitToList(exposedHeaders));

        /** GET /api/**, /auth/**, /h2-console/**에 해당하는 모든 경로를 허용 */
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            /** CSRF 비활성화 */
            .csrf(AbstractHttpConfigurer::disable) 
            /** 세션 미사용 : 완전 무상태 모든 요청은 "토큰"으로만 인증/인가를 진행 */
            .sessionManagement(
            sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            /** CORS 활성화  */
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            /** 폼 로그인/HTTP Basic 비활성 ★ JWT 만을 사용 ★ */
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .exceptionHandling(ex -> ex
                /** JsonAuthenticationEntryPoint handler를 통해 AuthenticationEntryPoint로 변환해서 사용 */
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
            );

            /** 로컬 개발 시 개발용 H2 콘솔 접근이 true라면 */
            if (h2ConsoleEnabled) {
                http.headers(headers
                        -> headers.frameOptions(frame -> frame.sameOrigin()));
            }

            /** URL 인가 규칙 */
            http.authorizeHttpRequests(auth -> {
                /** H2 콘솔 접근 권한 열기 (개발 환경에서 DB를 직접 확인 가능) */
                if (h2ConsoleEnabled) auth.requestMatchers("/h2-console/**").permitAll();

                /** SecurityFilterChain URL 보안 규칙 설정 */
                auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                /** permitAll => "인증되지 않은 사용자도 접근 허용" */
                .requestMatchers("/api/v1/auth/**").permitAll()
                /** authenticated => "인증이 되어 있는 사용자만 접근 허용"*/
                .requestMatchers("/api/v1/users/me/**").authenticated()
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")

                .anyRequest().authenticated();
            });

            /** JWT 인증 필터를 usernamePasswordAuthenticationFilter 앞에 부여한다 */
            http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
            return http.build();
    }

        // 문자열 콤마를 구분하여 리스트로 변환
        private static List<String> splitToList(String csv) {
            return Arrays.stream(csv.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isBlank())
                    .toList();
        }
}
