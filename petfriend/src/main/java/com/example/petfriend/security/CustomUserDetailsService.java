package com.example.petfriend.security;

import com.example.petfriend.entity.User;
import com.example.petfriend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    private final UserPrincipalMapper principalMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String loginId = (username == null) ? "" : username.trim();

        if (loginId.isEmpty()) throw new UsernameNotFoundException("사용자를 찾을 수 없습니다. 다시확인해주세요" + username);

        User user = userRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다. 다시확인해주세요" + username));

        return principalMapper.map(user);
    }
}
