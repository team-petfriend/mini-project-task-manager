package com.example.petfriend.dto.task.response;

import com.example.petfriend.common.enums.TaskPriority;
import com.example.petfriend.common.enums.TaskStatus;
import com.example.petfriend.entity.Task;

import java.time.LocalDateTime;


public class TaskResponse {
    public record DetailTaskResponse(
            String title,
            String description,
            TaskStatus taskStatus,
            TaskPriority taskPriority,
            LocalDateTime created_at,
            LocalDateTime updated_at
    ) {
        public static DetailTaskResponse from(Task task) {
            return new DetailTaskResponse(
                    task.getTitle(),
                    task.getDescription(),
                    task.getTaskStatus(),
                    task.getTaskPriority(),
                    task.getCreatedAt(),
                    task.getUpdatedAt()
            );
        }

    }

    public record TaskListResponse(
            Long id,
            String title
    ) {
    }

}
