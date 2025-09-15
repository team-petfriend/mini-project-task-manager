package com.example.petfriend.service;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.notification.request.NotificationCreateRequestDto;
import com.example.petfriend.dto.notification.request.NotificationReadUpdateRequestDto;
import com.example.petfriend.dto.notification.response.NotificationResponseDto;
import com.example.petfriend.security.UserPrincipal;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import java.util.List;

public interface NotificationService {
    ResponseDto<List<NotificationResponseDto>> getNotifications(UserPrincipal userPrincipal, Boolean isRead);

    ResponseDto<NotificationResponseDto> createNotification(UserPrincipal userPrincipal, @Valid NotificationCreateRequestDto dto);

    ResponseDto<NotificationResponseDto> markAsRead(@Positive(message = "알림 id는 1 이상의 정수여야 합니다.") Long id, @Valid NotificationReadUpdateRequestDto dto);
}
