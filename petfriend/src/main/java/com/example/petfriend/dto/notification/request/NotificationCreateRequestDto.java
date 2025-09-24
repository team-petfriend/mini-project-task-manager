package com.example.petfriend.dto.notification.request;

import com.example.petfriend.common.enums.RefType;
import com.example.petfriend.common.enums.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NotificationCreateRequestDto(

        @NotNull(message = "알림 타입은 필수 값입니다.")
        Type type,

        @NotNull(message = "refType은 필수 값입니다.")
        RefType refType,

        @NotNull(message = "refId는 필수 값입니다.")
        Long refId,

        @NotBlank(message = "메시지는 필수 값입니다.")
        String message
) {}