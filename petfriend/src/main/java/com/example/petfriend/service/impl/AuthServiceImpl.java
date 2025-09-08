package com.example.petfriend.service.impl;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.auth.request.SignInRequest;
import com.example.petfriend.dto.auth.request.SignUpRequest;
import com.example.petfriend.dto.auth.response.SignInResponse;
import com.example.petfriend.entity.User;
import com.example.petfriend.provider.JwtProvider;
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

    /** 회원가입 */
    @Override
    @Transactional
    public void signUp(SignUpRequest req) {
        /** @NotBlank를 사용했기 때문에 중복성 검사를 해준다. */
        if (userRepository.existsByLoginId(req.loginId())) {
            throw new IllegalArgumentException("이미 사용 중인 로그인 아이디입니다.");
        }
        /** @NotBlank를 사용했기 때문에 중복성 검사를 해준다. */
        if (userRepository.existsByEmail(req.email())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        /** @NotBlank를 사용했기 때문에 중복성 검사를 해준다. */
        if (userRepository.existsByNickname(req.nickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }
        /** 비밀번호 해시코드 저장 */
        String encoded = passwordEncoder.encode(req.password());

        /** 엔티티 생성 */
        User user = User.builder()
                .loginId(req.loginId())
                .password(encoded)
                .email(req.email())
                .nickname(req.nickname())
//                .gender(req.gender()) - null 허용
                .build();
        /** 생성된 엔티티를 저장한다. */
        userRepository.save(user);
    }

    @Override
    public ResponseDto<SignInResponse> signIn(SignInRequest req) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.loginId(), req.password())
        );
        /** GrantedAuthority가 담고있는 ("ROLE_USER")의 값을 "ROLE_USER"로 추출한다. */
        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        /** JWT 발급에 핗요한 인자 => req.loginId()와 roles가 필요 */
        String accessToken = jwtProvider.generateJwtToken(req.loginId(), roles);
        /** 만료 시각을 추출*/
        Claims claims = jwtProvider.getClaims(accessToken);
        long expiresAt = claims.getExpiration().getTime();
        /** 추출한 값을 가지고 SignInResponse을 생성해준다. */
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
