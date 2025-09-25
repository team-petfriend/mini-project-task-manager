package com.example.petfriend.dto.task.request;

import com.example.petfriend.common.enums.TaskPriority;
import com.example.petfriend.common.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TaskRequest {

    public record TaskCreateRequest(
            @NotNull(message = "title은 필수 값 입니다.")
            String title,
            String description
    ) {
    }

    public record TaskUpdateRequest(
            @NotBlank(message = "title은 필수 값 입니다.")
            String title,
            String description
    ) {
    }
}