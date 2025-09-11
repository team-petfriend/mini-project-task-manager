package com.example.petfriend.dto.task.response;

import com.example.petfriend.common.enums.TaskPriority;
import com.example.petfriend.common.enums.TaskStatus;
import com.example.petfriend.entity.TaskAssignees;

import java.time.LocalDateTime;
import java.util.Set;

public class TaskResponse {
    public record DetailTaskResponse(
            String title,
            String description,
            TaskStatus taskStatus,
            TaskPriority taskPriority,
            Set<Long> assigneesId, // 담당자
            LocalDateTime created_at,
            LocalDateTime updated_at
    ){}
}