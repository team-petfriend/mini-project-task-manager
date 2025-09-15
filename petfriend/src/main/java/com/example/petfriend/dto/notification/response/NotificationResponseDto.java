package com.example.petfriend.dto.notification.response;

import com.example.petfriend.common.enums.RefType;
import com.example.petfriend.common.enums.Type;

import java.time.LocalDateTime;

public class NotificationResponseDto {
    public record NotificationCreateResponseDto(
            Long notificationId,
            Long userId,
            Type type,
            RefType refType,
            Long refId,
            String message,
            LocalDateTime createdAt
    ) {}
}
