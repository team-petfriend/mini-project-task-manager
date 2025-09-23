package com.example.petfriend.service.impl;

import com.example.petfriend.common.enums.RoleType;
import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.admin.request.RoleRequest;
import com.example.petfriend.dto.admin.response.RoleResponse;
import com.example.petfriend.entity.Role;
import com.example.petfriend.entity.User;
import com.example.petfriend.repository.RoleRepository;
import com.example.petfriend.repository.UserRepository;
import com.example.petfriend.security.UserPrincipal;
import com.example.petfriend.service.AdminService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    /** 권한추가 */
    @Override
    @Transactional
    public ResponseDto<RoleResponse.AddRoleResponse> addRole(UserPrincipal userPrincipal, RoleRequest.@Valid AddRoleRequest req) {
        User user  = userRepository.findWithRolesById(req.userId())
                .orElseThrow( () -> new EntityNotFoundException("해당 ID의 사용자가 존재하지 않습니다. 다시 확인해주세요."));

        Role role = roleRepository.findById(req.role())
                .orElseThrow(() -> new EntityNotFoundException("해당 권한을 찾을 수 없습니다."));


        user.grantRole(role);
        userRepository.flush();

        RoleResponse.AddRoleResponse addResponse = new RoleResponse.AddRoleResponse(
                user.getId(),
                user.getLoginId(),
                req.role(),
                Set.copyOf(user.getRoleTypes()),
                user.getUpdatedAt()
        );

        return ResponseDto.setSuccess("SUCCESS", addResponse);
    }

    @Override
    @Transactional
    public ResponseDto<RoleResponse.ReplaceResponse> replaceRole(UserPrincipal userPrincipal, RoleRequest.@Valid ReplaceRequest req) {
        User user  = userRepository.findWithRolesById(req.userId())
                .orElseThrow( () -> new EntityNotFoundException("해당 ID의 사용자가 존재하지 않습니다. 다시 확인해주세요."));

        user.getUserRoles().clear();

        userRepository.flush();

        RoleResponse.ReplaceResponse replaceResponse = new RoleResponse.ReplaceResponse(
                user.getId(),
                user.getLoginId(),
                Set.copyOf(user.getRoleTypes()),
                user.getUpdatedAt()
        );

        return ResponseDto.setSuccess("SUCCESS", replaceResponse);
    }

    @Override
    @Transactional
    public ResponseDto<RoleResponse.DeleteResponse> DeleteRole(UserPrincipal userPrincipal, RoleRequest.@Valid DeleteRequest req) {
        User user  = userRepository.findWithRolesById(req.userId())
                .orElseThrow( () -> new EntityNotFoundException("해당 ID의 사용자가 존재하지 않습니다. 다시 확인해주세요."));

        Role role = roleRepository.findById(req.role())
                        .orElseThrow(() -> new EntityNotFoundException("해당 권한을 찾을 수 없습니다."));

        user.revokeRole(role);
        userRepository.flush();

        if (user.getUserRoles().isEmpty()) {
            user.grantRole(roleRepository.getReferenceById(RoleType.USER));
        }

        RoleResponse.DeleteResponse deleteResponse = new RoleResponse.DeleteResponse(
                user.getId(),
                user.getLoginId(),
                req.role(),
                Set.copyOf(user.getRoleTypes()),
                user.getUpdatedAt()
        );

        return ResponseDto.setSuccess("SUCCESS", deleteResponse);
    }
}
