package com.example.petfriend.dto.notification.response;

import com.example.petfriend.entity.Notifications;

import java.time.LocalDateTime;

public record NotificationResponseDto(
        Long id,
        Long userId,
        String type,
        String refType,
        Long refId,
        String message,
        boolean isRead,
        LocalDateTime createdAt
) {
    public static NotificationResponseDto from(Notifications notifications) {
        return new NotificationResponseDto(
                notifications.getId(),
                notifications.getUser().getId(),
                notifications.getType().name(),
                notifications.getRefType().name(),
                notifications.getRefId(),
                notifications.getMessage(),
                notifications.isRead(),
                notifications.getCreatedAt()
        );
    }
}
