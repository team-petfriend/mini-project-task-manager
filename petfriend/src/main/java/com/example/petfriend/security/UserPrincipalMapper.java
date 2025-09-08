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

        /** ROLE의 값을 가져오는 기능 */
        Collection<? extends GrantedAuthority> authorities
                = (user.getRoles() == null || user.getRoles().isEmpty()) ? List.of(new SimpleGrantedAuthority("ROLE_USER")) : user.getRoles().stream()
                .map(r-> {
                    String name = r.name();
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
                .build();
    }
}
