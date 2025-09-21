package com.example.petfriend.service.impl;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.notification.request.NotificationCreateRequestDto;
import com.example.petfriend.dto.notification.request.NotificationReadUpdateRequestDto;
import com.example.petfriend.dto.notification.response.NotificationResponseDto;
import com.example.petfriend.entity.Notifications;
import com.example.petfriend.entity.User;
import com.example.petfriend.repository.NotificationRepository;
import com.example.petfriend.repository.UserRepository;
import com.example.petfriend.security.UserPrincipal;
import com.example.petfriend.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public ResponseDto<List<NotificationResponseDto>> getNotifications(UserPrincipal userPrincipal, Boolean isRead) {
        List<Notifications> notifications;

        if (isRead != null) {
            notifications = notificationRepository.findByUserIdAndIsRead(userPrincipal.getId(), isRead);
        } else {
            notifications = notificationRepository.findByUserId(userPrincipal.getId());
        }

        List<NotificationResponseDto> rep = notifications.stream()
                .map(NotificationResponseDto::from)
                .toList();

        return ResponseDto.setSuccess("SUCCESS", rep);
    }

    @Override
    @Transactional
    public ResponseDto<NotificationResponseDto> createNotification(UserPrincipal userPrincipal, NotificationCreateRequestDto dto) {
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() ->new EntityNotFoundException("해당 ID 사용자를 찾을 수 없습니다."));

        Notifications notifications = Notifications.builder()
                .user(user)
                .type(dto.type())
                .refType(dto.refType())
                .refId(dto.refId())
                .message(dto.message())
                .isRead(false)
                .build();

        Notifications saved = notificationRepository.save(notifications);

        return ResponseDto.setSuccess("SUCCESS", NotificationResponseDto.from(saved));
    }

    @Override
    @Transactional
    public ResponseDto<NotificationResponseDto> markAsRead(Long id, NotificationReadUpdateRequestDto dto) {
        Notifications notifications = notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 알림"));

        notifications.setRead(dto.isRead());
        Notifications saved = notificationRepository.save(notifications);

        return ResponseDto.setSuccess("SUCCESS", NotificationResponseDto.from(saved));
    }
}
