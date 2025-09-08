package com.example.petfriend.service.impl;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.auth.request.SignInRequest;
import com.example.petfriend.dto.auth.request.SignUpRequest;
import com.example.petfriend.dto.auth.response.SignInResponse;
import com.example.petfriend.dto.user.request.UserProfileUpdateRequest;
import com.example.petfriend.dto.user.response.UserProfileResponse;
import com.example.petfriend.entity.User;
import com.example.petfriend.provider.JwtProvider;
import com.example.petfriend.repository.UserRepository;
import com.example.petfriend.security.UserPrincipal;
import com.example.petfriend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    @Override
    public ResponseDto<UserProfileResponse.MyPageResponse> getMyInfo(UserPrincipal principal) {
        return null;
    }

    @Override
    public ResponseDto<UserProfileResponse.MyPageResponse> updateMyInfo(UserPrincipal principal, UserProfileUpdateRequest request) {
        return null;
    }
}
