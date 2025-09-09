package com.example.petfriend.controller;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.auth.request.SignInRequest;
import com.example.petfriend.dto.auth.request.SignUpRequest;
import com.example.petfriend.dto.auth.response.SignInResponse;
import com.example.petfriend.service.AuthService;
import com.example.petfriend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    /** 회원가입(sign-up)
     *  void 반환
     *  사용자가 입력한 값 => RequestBody에 담아서 온다.
     */
    @PostMapping("/sign-up")
    public ResponseEntity<ResponseDto<Void>> signUp( @Valid @RequestBody SignUpRequest req ) {
        /** userService에 객체를 담아준다. */
        authService.signUp(req);
        /** 반환 타입이 없으므로 null 을 넣어준다. */
        return ResponseEntity.ok(ResponseDto.setSuccess("회원가입이 성공적으로 완료되었습니다.", null));
    }


    /** 로그인(sign-in)
     *  SingInResponse
     *  사용자가 입력한 값 => RequestBody에 담아서 온다.
     * */
    @PostMapping("/sign-in")
    public ResponseEntity<ResponseDto<SignInResponse>> signIn( @Valid @RequestBody SignInRequest req ) {
        /** <ResponseDto<SignInResponse>> 반환타입이 존재하기 떼문에 객체를 생성해서 담아준다. */
        ResponseDto<SignInResponse> response = authService.signIn(req);
        return ResponseEntity.ok().body(response);
    }

}
