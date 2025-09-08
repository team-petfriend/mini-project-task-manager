package com.example.petfriend.dto.auth.request;

import com.example.petfriend.common.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** 회원가입 요청 DTO
 *  record 사용 => toString, getter, setter등을 자동으로 만들어준다.
 * */
public record SignUpRequest(
        @NotBlank @Size(min = 4, max = 50)
        String loginId,

        @NotBlank @Size(min = 8, max = 100)
        String password,

        @NotBlank @Email @Size(max = 50)
        String email,

        @NotBlank @Size(max = 50)
        String nickname,

        /** 선택 값 ENUM 처리 (성별) */
        Gender gender
) {}
