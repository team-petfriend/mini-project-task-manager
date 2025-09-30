package com.example.petfriend.controller;

import com.example.petfriend.common.contants.ApiMappingPattern;
import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.auth.request.SignInRequest;
import com.example.petfriend.dto.auth.request.SignUpRequest;
import com.example.petfriend.dto.auth.response.SignInResponse;
import com.example.petfriend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiMappingPattern.User.AUTH)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<ResponseDto<Void>> signUp( @Valid @RequestBody SignUpRequest req ) {
        authService.signUp(req);
        return ResponseEntity.ok(ResponseDto.setSuccess("회원가입이 성공적으로 완료되었습니다.", null));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<ResponseDto<SignInResponse>> signIn( @Valid @RequestBody SignInRequest req ) {
        ResponseDto<SignInResponse> response = authService.signIn(req);
        return ResponseEntity.ok().body(response);
    }

}
