package com.example.petfriend.dto.task.request;

import com.example.petfriend.common.enums.TaskPriority;
import com.example.petfriend.common.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class TaskRequest {

    public record TaskCreateRequest(
            List<TaskLine> tasks
    ) {
    }

    public record TaskLine(
            @NotNull(message = "title은 필수 값 입니다.")
            String title,
            String description,
            @NotNull(message = "taskStatus은 필수 값 입니다.")
            TaskStatus taskStatus,
            @NotNull(message = "taskPriority은 필수 값 입니다.")
            TaskPriority taskPriority
    ) {
    }

    public record TaskUpdateRequest(
            @NotBlank(message = "title은 필수 값 입니다.")
            String title,
            String description,
            @NotNull(message = "taskStatus은 필수 값 입니다")
            TaskStatus taskStatus,
            @NotNull(message = "taskPriority은 필수 값 입니다.")
            TaskPriority taskPriority
    ) {
    }
}