package com.example.petfriend.service.impl;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.user.request.UserProfileUpdateRequest;
import com.example.petfriend.dto.user.response.UserProfileResponse;
import com.example.petfriend.entity.User;
import com.example.petfriend.repository.UserRepository;
import com.example.petfriend.security.UserPrincipal;
import com.example.petfriend.security.util.PrincipalUtils;
import com.example.petfriend.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @PreAuthorize("isAuthenticated()")
    @Override
    public ResponseDto<UserProfileResponse.MyPageResponse> getMyInfo(UserPrincipal principal)  {
        PrincipalUtils.requiredActive(principal);

        User user = userRepository.findByLoginId(principal.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 사용자가 없습니다. : " + principal.getUsername()));

        UserProfileResponse.MyPageResponse userInfo = new UserProfileResponse.MyPageResponse(
            user.getId(),
            user.getLoginId(),
            user.getEmail(),
            user.getNickname(),
            user.getGender()
        );

        return ResponseDto.setSuccess("SUCCESS", userInfo);
    }

    @PreAuthorize("isAuthenticated()")
    @Override
    @Transactional
    public ResponseDto<UserProfileResponse.MyPageResponse> updateMyInfo(UserPrincipal principal, UserProfileUpdateRequest request)  {
        PrincipalUtils.requiredActive(principal);

        User user = userRepository.findByLoginId(principal.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 해당하는 사용자가 없습니다. : " + principal.getUsername()));

        user.changeProfile(request.nickname(), request.gender());
        userRepository.flush();

        UserProfileResponse.MyPageResponse updateUser = new UserProfileResponse.MyPageResponse(
            user.getId(),
            user.getLoginId(),
            user.getEmail(),
            user.getNickname(),
            user.getGender()
        );
        return ResponseDto.setSuccess("SUCCESS", updateUser);
    }
}
