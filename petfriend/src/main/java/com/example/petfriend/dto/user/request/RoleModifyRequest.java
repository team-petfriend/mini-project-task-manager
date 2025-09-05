package com.example.petfriend.dto.user.request;

import com.example.petfriend.common.enums.RoleType;
import jakarta.validation.constraints.NotNull;

public record RoleModifyRequest(
        @NotNull RoleType role
) {
}
