package com.example.petfriend.dto.notification.request;

import com.example.petfriend.common.enums.RefType;
import com.example.petfriend.common.enums.Type;

public record NotificationCreateRequestDto(
        Long userId,
        Type type,
        RefType refType,
        Long refId,
        String message
) {

}
