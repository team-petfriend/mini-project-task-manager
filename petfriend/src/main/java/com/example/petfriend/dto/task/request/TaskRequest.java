package com.example.petfriend.dto.task.request;


import com.example.petfriend.entity.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class TaskRequest {
    public record TaskCreateRequest(
            @NotNull(message = "title은 필수 값 입니다.")
            String title,
            String description,
            List<Long> tagIds
    ) {
    }

    public record TaskUpdateRequest(
            @NotBlank(message = "title은 필수 값 입니다.")
            String title,
            String description
    ) {
    }
}