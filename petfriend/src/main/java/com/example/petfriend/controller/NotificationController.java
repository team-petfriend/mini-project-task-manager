package com.example.petfriend.controller;

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
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Validated
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * 특정 유저의 알림 조회
     */
    @GetMapping
    public ResponseEntity<ResponseDto<List<NotificationResponseDto>>> getNotifications(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(required = false) Boolean isRead
    ) {
        ResponseDto<List<NotificationResponseDto>> response =
                notificationService.getNotifications(userPrincipal, isRead);

        return ResponseEntity.ok().body(response);
    }

    /**
     * 알림 생성
     */
    @PostMapping
    public ResponseEntity<ResponseDto<NotificationResponseDto>> createNotification(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody NotificationCreateRequestDto dto
    ) {
        ResponseDto<NotificationResponseDto> response =
                notificationService.createNotification(userPrincipal, dto);

        return ResponseEntity.ok().body(response);
    }

    /**
     * 알림 읽음 처리
     */
    @PatchMapping("/{id}/read")
    public ResponseEntity<ResponseDto<NotificationResponseDto>> markAsRead(
            @PathVariable("id") @Positive(message = "알림 id는 1 이상의 정수여야 합니다.") Long id,
            @Valid @RequestBody NotificationReadUpdateRequestDto dto
    ) {
        ResponseDto<NotificationResponseDto> response =
                notificationService.markAsRead(id, dto);

        return ResponseEntity.ok().body(response);
    }
}
