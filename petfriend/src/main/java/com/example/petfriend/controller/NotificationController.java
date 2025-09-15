package com.example.petfriend.controller;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Validated
public class NotificationController {
    private final NotificationService notificationsService;

    @PostMapping
    public ResponseEntity<ResponseDto<>>
}
