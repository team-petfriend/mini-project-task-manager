package com.example.petfriend.dto.user.response;

import com.example.petfriend.common.enums.Gender;

public class UserProfileResponse {
    public record MyPageResponse(
            Long id,
            String loginId,
            String email,
            String nickname,
            Gender gender
    ){}
}
