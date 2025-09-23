package com.example.petfriend.dto.auth.response;

import java.util.Set;

public record SignInResponse(
        String tokenType,
        String accessToken,
        long expiresAt,
        String username,
        Set<String> roles
) {
}
