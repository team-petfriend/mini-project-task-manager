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
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("isAuthenticated()")
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
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseDto<NotificationResponseDto> createNotification(UserPrincipal userPrincipal, NotificationCreateRequestDto dto) {
        final Long loginId = userPrincipal.getId();
        User user = userRepository.findById(loginId)
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

    /** 본인 소유의 알림만 읽음 처리 가능 */
    @Override
    @Transactional
    @PreAuthorize("@authz.isNotificationOwner(#id, authentication)")
    public ResponseDto<NotificationResponseDto> markAsRead(Long id, NotificationReadUpdateRequestDto dto) {
        Notifications notifications = notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 알림"));

        if (dto.isRead()) {
            notifications.markAsRead();
        } else {
            notifications.setRead(false);
        }

        Notifications saved = notificationRepository.save(notifications);

        return ResponseDto.setSuccess("SUCCESS", NotificationResponseDto.from(saved));
    }
}
