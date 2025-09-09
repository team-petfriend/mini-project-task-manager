package com.example.petfriend.service;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.auth.request.SignInRequest;
import com.example.petfriend.dto.auth.request.SignUpRequest;
import com.example.petfriend.dto.auth.response.SignInResponse;
import com.example.petfriend.dto.user.request.UserProfileUpdateRequest;
import com.example.petfriend.dto.user.response.UserProfileResponse;
import com.example.petfriend.security.UserPrincipal;
import jakarta.validation.Valid;

import java.nio.file.AccessDeniedException;

public interface UserService {
    ResponseDto<UserProfileResponse.MyPageResponse> getMyInfo(UserPrincipal principal) throws AccessDeniedException;
    ResponseDto<UserProfileResponse.MyPageResponse> updateMyInfo(UserPrincipal principal, @Valid UserProfileUpdateRequest request) throws AccessDeniedException;
}
