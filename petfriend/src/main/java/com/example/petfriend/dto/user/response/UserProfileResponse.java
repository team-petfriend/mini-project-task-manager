package com.example.petfriend.dto.user.response;

public class UserProfileResponse {
    public record MyPageResponse(
            Long id,
            String loginId,
            String email,
            String nickname,
            Gender gender
    ){}
}
