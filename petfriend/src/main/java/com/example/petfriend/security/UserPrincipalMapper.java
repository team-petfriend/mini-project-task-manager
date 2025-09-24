package com.example.petfriend.security;

import com.example.petfriend.entity.User;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class UserPrincipalMapper {

    @NonNull
    public UserPrincipal map(@NonNull User user) {

        Collection<? extends  GrantedAuthority> authorities =
                // 사용자 정보 내부의 권한이 비어져 있거나 없는 경우
                (user.getUserRoles() == null || user.getUserRoles().isEmpty())
                        // 기본 권한 "ROLE_USER" 부여
                        ? List.of(new SimpleGrantedAuthority("ROLE_USER"))
                        // 해당 권한(들)을 GrantedAuthority 타입으로 변환하여 반환
                        : user.getUserRoles().stream()
                        .map(r -> {
                            String name = r.getRole().getName().name();
                            String role = name.startsWith("ROLE_") ? name : "ROLE_" + name;
                            return new SimpleGrantedAuthority(role);
                        })
                        .toList();

        /** 새로운 생성자(builder)를 만들어준다. */
        return UserPrincipal.builder()
                .id(user.getId())
                .username(user.getLoginId())
                .password(user.getPassword())
                .authorities(authorities)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();
    }
}
