package com.example.petfriend.security.util;

import com.example.petfriend.security.UserPrincipal;
import org.springframework.security.access.AccessDeniedException;


public class PrincipalUtils {
    /** 생성자 막기위해서 사용 */
    private PrincipalUtils() {}

    /** UserPrincipal */
    public static void requiredActive(UserPrincipal principal) throws AccessDeniedException {
        if (principal == null) {
            throw new AccessDeniedException("인증이 필요합니다.");
        }

        if (principal.isAccountNonExpired()|| !principal.isEnabled() || !principal.isCredentialsNonExpired()) {
            throw new AccessDeniedException("비활성화 된 계정입니다. 다시 확인해주세요.");
        }
    }
}
