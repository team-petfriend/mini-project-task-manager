package com.example.petfriend.dto.user.request;

import jakarta.validation.constraints.NotNull;

public record RoleModifyRequest(
        @NotNull RoleType role
) {
}
