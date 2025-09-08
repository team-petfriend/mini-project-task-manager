package com.example.petfriend.dto.auth.response;

import java.util.Set;

public record SignInResponse(
        /** "Bearer" 문자열 */
        String tokenType,
        /** JWT토큰 */
        String accessToken,
        /** 만료시각 */
        long expiresAt,
        /** loginId */
        String username,
        /** SET으로 저장된 {"ROLE_USER", "ROLE_ADMIN"} */
        Set<String> roles
) {
}
