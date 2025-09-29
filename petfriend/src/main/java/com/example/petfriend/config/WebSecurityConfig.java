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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JsonAuthenticationEntryPoint authenticationEntryPoint;
    private final JsonAccessDeniedHandler accessDeniedHandler;

    @Value("${cors.allowed-origins:*}")
    private String allowedOrigins; // 설정에서 허용할 출처를 지정.

    @Value("${cors.allowed-headers:*}")
    private String allowedHeaders; // 헤더에 어떤 걸 허용할지 설정.

    @Value("${cors.allowed-methods:GET, POST, PUT, PATCH, DELETE, OPTIONS}")
    private String allowedMethods; // 클라이언트가 사용 할 수 있는 HTTP 메서드 지정.

    @Value("${cors.exposed:Authorization,Set-cookie}")
    private String exposedHeaders;

    @Value("${security.h2-console:true}")
    private boolean h2ConsoleEnabled;

    @Bean // 메서드를 반환하기 위해서 객체를 스프링 빈으로 등록해준다.
    public BCryptPasswordEncoder passwordEncoder() {
        // 새로운 비밀번호 인코더를 반환
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);

        config.setAllowedHeaders(splitToList(allowedHeaders));

        config.setAllowedMethods(splitToList(allowedMethods));

        config.setExposedHeaders(splitToList(exposedHeaders));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(
                    sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .exceptionHandling(ex -> ex
            .authenticationEntryPoint(authenticationEntryPoint)
            .accessDeniedHandler(accessDeniedHandler)
             );

        if (h2ConsoleEnabled) {
            http.headers(headers
                    -> headers.frameOptions(frame -> frame.sameOrigin()));
        }

        http.authorizeHttpRequests(auth -> {
            if (h2ConsoleEnabled) auth.requestMatchers("/h2-console/**").permitAll();

            auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/users/me/**").authenticated()
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.POST, "/api/v1/project/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/project/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/project/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.POST, "/api/v1/tags/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/tags/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(HttpMethod.PUT, "/api/v1/tags/**").hasAnyRole("ADMIN", "MANAGER")

                .anyRequest().authenticated();
        });

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    private static List<String> splitToList(String csv) {
        return Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();
    }
}
