package com.example.petfriend.controller;

import com.example.petfriend.common.contants.ApiMappingPattern;
import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.notification.request.NotificationCreateRequestDto;
import com.example.petfriend.dto.notification.request.NotificationReadUpdateRequestDto;
import com.example.petfriend.dto.notification.response.NotificationResponseDto;
import com.example.petfriend.security.UserPrincipal;
import com.example.petfriend.service.NotificationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.Notifications.ROOT)
@RequiredArgsConstructor
@Validated
public class NotificationController {

    private final NotificationService notificationService;

    /** 알림 생성 */
    @PostMapping
    // http://localhost:8080/api/v1/notifications
    public ResponseEntity<ResponseDto<NotificationResponseDto>> createNotification(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody NotificationCreateRequestDto dto
    ) {
        ResponseDto<NotificationResponseDto> response =
                notificationService.createNotification(userPrincipal, dto);

        return ResponseEntity.ok().body(response);
    }

    /** 특정 유저의 알림 조회 */
    @GetMapping
    // http://localhost:8080/api/v1/notifications
    public ResponseEntity<ResponseDto<List<NotificationResponseDto>>> getNotifications(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(required = false) Boolean isRead
    ) {
        ResponseDto<List<NotificationResponseDto>> response =
                notificationService.getNotifications(userPrincipal, isRead);

        return ResponseEntity.ok().body(response);
    }

    /** 알림 읽음 처리 */
    @PatchMapping(ApiMappingPattern.Notifications.BY_READ)
    // http://localhost:8080/api/v1/notifications/:notificationId/read
    public ResponseEntity<ResponseDto<NotificationResponseDto>> markAsRead(
            @PathVariable("notificationId") @Positive(message = "알림 id는 1 이상의 정수여야 합니다.") Long notificationId,
            @Valid @RequestBody NotificationReadUpdateRequestDto dto
    ) {
        return ResponseEntity.ok().body(notificationService.markAsRead(notificationId, dto));
    }
}
