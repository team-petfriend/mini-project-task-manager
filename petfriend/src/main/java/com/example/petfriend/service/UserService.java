package com.example.petfriend.service;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.user.request.UserProfileUpdateRequest;
import com.example.petfriend.dto.user.response.UserProfileResponse;
import com.example.petfriend.security.UserPrincipal;
import jakarta.validation.Valid;

public interface UserService {
    ResponseDto<UserProfileResponse.MyPageResponse> getMyInfo(UserPrincipal principal);
    ResponseDto<UserProfileResponse.MyPageResponse> updateMyInfo(UserPrincipal principal, @Valid UserProfileUpdateRequest request);
}
