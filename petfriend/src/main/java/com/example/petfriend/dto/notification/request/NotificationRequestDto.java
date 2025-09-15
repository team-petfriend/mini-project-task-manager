package com.example.petfriend.dto.notification.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NotificationRequestDto {
    public record NotificationCreateRequestDto (
            @NotBlank(message = "알림내용은 필수 입력 값입니다.")
            @Size(max = 255, message = "내용은 최대 255자까지 입력 가능합니다.")
            String message
    ) {}
}
