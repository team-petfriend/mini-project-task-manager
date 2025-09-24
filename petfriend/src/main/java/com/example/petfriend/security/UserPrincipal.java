package com.example.petfriend.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@ToString(exclude = "password")
public class UserPrincipal implements UserDetails {

    /** == UserPrincipal 사용 이유 ==
     * User 라는 엔티티 자체를 지니지 않고, 인증이 필요한 값만 안전하게 전달/보관을 위해 사용
     * ※ 보안 관점에서 사용자 표현 ※ 을 담당하는 객체
     *
     * */

    // 필드
    private final Long id;
    private final String username;
    /** 해시비밀번호 저장 */
    @JsonIgnore
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;


    /** 추후 사용자 제약 조건기능 추가 */


    // 생성자 + Builder
    @Builder
    private UserPrincipal(
            Long id,
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities,
            boolean accountNonExpired,
            boolean accountNonLocked,
            boolean credentialsNonExpired,
            boolean enabled
    ){
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    };

    // 메서드
    /** 계정의 권한 목록 리턴 */
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    /** 계정의 비밀번호 리턴 (해시값) */
    @Override public String getPassword() { return password;}
    /** ★ 계정의 고유한 값을 리턴 (PK값)★ */
    @Override public String getUsername() {return username;}
    @Override public boolean isAccountNonExpired() { return accountNonExpired; }
    @Override public boolean isAccountNonLocked() { return accountNonLocked; }
    @Override public boolean isCredentialsNonExpired() { return credentialsNonExpired; }
    @Override public boolean isEnabled() { return enabled; }

}
