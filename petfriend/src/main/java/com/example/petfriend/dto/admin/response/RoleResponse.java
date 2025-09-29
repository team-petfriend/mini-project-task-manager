package com.example.petfriend.dto.admin.response;

import com.example.petfriend.common.enums.RoleType;

import java.time.LocalDateTime;
import java.util.Set;

public final class RoleResponse {

    public record AddRoleResponse(
            Long userId,
            String loginId,
            RoleType added,
            Set<RoleType> roles,
            LocalDateTime updatedAt
    ) {}


    public record ReplaceResponse(
            Long userId,
            String loginId,
            Set<RoleType> roles,
            LocalDateTime updatedAt
    ) {}

    public record DeleteResponse(
            Long userId,
            String loginId,
            RoleType deleted,
            Set<RoleType> roles,
            LocalDateTime updatedAt
    ) { }
}
