package com.example.petfriend.dto.admin.request;

import com.example.petfriend.common.enums.RoleType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Set;

public final class RoleRequest {

    public record AddRoleRequest(
            @NotNull(message = "userId는 필수입니다.")
            @Positive(message = "userId는 양수여야 합니다.")
            Long userId,
            @NotNull(message = "role의 값은 필수입니다.")
            RoleType role
    ) {}

    public record ReplaceRequest(
        @NotNull(message = "userId는 필수입니다.")
        @Positive(message = "userId는 양수여야 합니다.")
        Long userId,
        @NotEmpty(message = "roles는 비어있을 수 없습니다.")
        Set<@NotNull(message = "roles 항목은 null일 수 없습니다.") RoleType> roles
    ) {}

    public record DeleteRequest(
            @NotNull(message = "userId는 필수입니다.")
            @Positive(message = "userId는 양수여야 합니다.")
            Long userId,
            @NotNull(message = "role은 필수입니다.")
            RoleType role
    ) {}
}
