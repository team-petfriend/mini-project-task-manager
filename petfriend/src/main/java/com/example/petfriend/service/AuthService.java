package com.example.petfriend.service;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.auth.request.SignInRequest;
import com.example.petfriend.dto.auth.request.SignUpRequest;
import com.example.petfriend.dto.auth.response.SignInResponse;
import jakarta.validation.Valid;

public interface AuthService {
    void signUp(@Valid SignUpRequest req);
    ResponseDto<SignInResponse> signIn(@Valid SignInRequest req);
}
