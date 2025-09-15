package com.example.petfriend.service.impl;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.notification.request.NotificationCreateRequestDto;
import com.example.petfriend.dto.notification.request.NotificationReadUpdateRequestDto;
import com.example.petfriend.dto.notification.response.NotificationResponseDto;
import com.example.petfriend.security.UserPrincipal;
import com.example.petfriend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    @Override
    public ResponseDto<List<NotificationResponseDto>> getNotifications(UserPrincipal userPrincipal, Boolean isRead) {
        return null;
    }

    @Override
    public ResponseDto<NotificationResponseDto> createNotification(UserPrincipal userPrincipal, NotificationCreateRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<NotificationResponseDto> markAsRead(Long id, NotificationReadUpdateRequestDto dto) {
        return null;
    }
}
