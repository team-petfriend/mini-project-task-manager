package com.example.petfriend.dto.task.request;

import com.example.petfriend.common.enums.TaskPriority;
import com.example.petfriend.common.enums.TaskStatus;
import com.example.petfriend.entity.TaskAssignees;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Set;

public class TaskRequest {

    public record TaskCreateRequest(
            @NotNull(message = "title은 필수 값 입니다.")
            String title,
            String description,
            Set<Long> assigneesId, // 담당자
            @NotNull(message = "taskStatus은 필수 값 입니다.")
            TaskStatus taskStatus,
            @NotNull(message = "taskStatus은 필수 값 입니다.")
            TaskPriority taskPriority
    ) {}
}
