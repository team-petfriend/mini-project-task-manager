package com.example.petfriend.dto.user.request;

import com.example.petfriend.common.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserProfileUpdateRequest(
        @NotBlank @Size(max = 50)
        String nickname,
        Gender gender
) {
}
