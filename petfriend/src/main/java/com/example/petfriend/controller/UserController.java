package com.example.petfriend.controller;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.user.request.UserProfileUpdateRequest;
import com.example.petfriend.dto.user.response.UserProfileResponse;
import com.example.petfriend.security.UserPrincipal;
import com.example.petfriend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/me")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<ResponseDto<UserProfileResponse.MyPageResponse>> getMyInfo(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        ResponseDto<UserProfileResponse.MyPageResponse> response = userService.getMyInfo(principal);
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping
    public ResponseEntity<ResponseDto<UserProfileResponse.MyPageResponse>> updateMyInfo(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UserProfileUpdateRequest request
    ) {
        ResponseDto<UserProfileResponse.MyPageResponse> response = userService.updateMyInfo(principal, request);
        return ResponseEntity.ok().body(response);
    }
}
