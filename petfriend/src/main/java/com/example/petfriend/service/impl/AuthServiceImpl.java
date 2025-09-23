package com.example.petfriend.service.impl;

import com.example.petfriend.common.enums.RoleType;
import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.auth.request.SignInRequest;
import com.example.petfriend.dto.auth.request.SignUpRequest;
import com.example.petfriend.dto.auth.response.SignInResponse;
import com.example.petfriend.entity.Role;
import com.example.petfriend.entity.User;
import com.example.petfriend.provider.JwtProvider;
import com.example.petfriend.repository.RoleRepository;
import com.example.petfriend.repository.UserRepository;
import com.example.petfriend.service.AuthService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    // @Bean 메서드로 BCryptPasswordEncoder 객체를 리턴하면
    //      , 스프링 컨테이너에 등록될 때 PasswordEncoder 타입으로 인식 (주입 시 해당 타입으로 정의 권장)
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RoleRepository roleRepository;

    /** 회원가입 */
    @Override
    @Transactional
    public void signUp(SignUpRequest req) {
        if (userRepository.existsByLoginId(req.loginId())) {
            throw new IllegalArgumentException("이미 사용 중인 로그인 아이디입니다.");
        }
        if (userRepository.existsByEmail(req.email())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        if (userRepository.existsByNickname(req.nickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }
        String encoded = passwordEncoder.encode(req.password());

        User user = User.builder()
                .loginId(req.loginId())
                .password(encoded)
                .email(req.email())
                .nickname(req.nickname())
                .build();

        Role defaultRole = roleRepository.getReferenceById(RoleType.USER);
        user.grantRole(defaultRole);

        userRepository.save(user);
    }

    @Override
    public ResponseDto<SignInResponse> signIn(SignInRequest req) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.loginId(), req.password())
        );
        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        String accessToken = jwtProvider.generateJwtToken(req.loginId(), roles);
        Claims claims = jwtProvider.getClaims(accessToken);
        long expiresAt = claims.getExpiration().getTime();

        SignInResponse response = new SignInResponse(
          "Bearer",
          accessToken,
          expiresAt,
          req.loginId(),
          roles
        );
        return ResponseDto.setSuccess("로그인 성공", response);
    }
}
