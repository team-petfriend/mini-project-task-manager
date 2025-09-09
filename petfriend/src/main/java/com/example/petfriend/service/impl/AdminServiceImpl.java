package com.example.petfriend.service.impl;

import com.example.petfriend.common.enums.RoleType;
import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.admin.request.RoleRequest;
import com.example.petfriend.dto.admin.response.RoleResponse;
import com.example.petfriend.entity.User;
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

    /** 권한추가 */
    @Override
    @Transactional
    public ResponseDto<RoleResponse.AddRoleResponse> addRole(UserPrincipal userPrincipal, RoleRequest.@Valid AddRoleRequest req) {
        User user  = userRepository.findWithRolesById(req.userId())
                .orElseThrow( () -> new EntityNotFoundException("해당 ID의 사용자가 존재하지 않습니다. 다시 확인해주세요."));

        RoleType added = req.role();
        user.addRole(added);
        userRepository.flush();

        RoleResponse.AddRoleResponse addResponse = new RoleResponse.AddRoleResponse(
                user.getId(),
                user.getLoginId(),
                added,
                Set.copyOf(user.getRoles()),
                user.getUpdatedAt()
        );

        return ResponseDto.setSuccess("SUCCESS", addResponse);
    }

    @Override
    @Transactional
    public ResponseDto<RoleResponse.ReplaceResponse> replaceRole(UserPrincipal userPrincipal, RoleRequest.@Valid ReplaceRequest req) {
        User user  = userRepository.findWithRolesById(req.userId())
                .orElseThrow( () -> new EntityNotFoundException("해당 ID의 사용자가 존재하지 않습니다. 다시 확인해주세요."));

        user.getRoles().clear();
        req.roles().forEach(user::addRole);


        RoleResponse.ReplaceResponse replaceResponse = new RoleResponse.ReplaceResponse(
                user.getId(),
                user.getLoginId(),
                Set.copyOf(user.getRoles()),
                user.getUpdatedAt()
        );

        return ResponseDto.setSuccess("SUCCESS", replaceResponse);
    }

    @Override
    @Transactional
    public ResponseDto<RoleResponse.DeleteResponse> DeleteRole(UserPrincipal userPrincipal, RoleRequest.@Valid DeleteRequest req) {
        User user  = userRepository.findWithRolesById(req.userId())
                .orElseThrow( () -> new EntityNotFoundException("해당 ID의 사용자가 존재하지 않습니다. 다시 확인해주세요."));

        RoleType deleted = req.role();
        user.removeRole(deleted);

        userRepository.flush();
        RoleResponse.DeleteResponse deleteResponse = new RoleResponse.DeleteResponse(
                user.getId(),
                user.getLoginId(),
                deleted,
                Set.copyOf(user.getRoles()),
                user.getUpdatedAt()
        );

        return ResponseDto.setSuccess("SUCCESS", deleteResponse);
    }
}
