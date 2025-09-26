package com.example.petfriend.dto.task.response;

import com.example.petfriend.common.enums.TaskPriority;
import com.example.petfriend.common.enums.TaskStatus;
import com.example.petfriend.entity.Task;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class TaskResponse {
    public record DetailTaskResponse(
            Long id,
            String title,
            String description,
            TaskStatus taskStatus,
            TaskPriority taskPriority,
            LocalDateTime created_at,
            LocalDateTime updated_at,
            LocalDate dueDate
    ) {
        public static DetailTaskResponse from(Task task) {
            return new DetailTaskResponse(
                    task.getId(),
                    task.getTitle(),
                    task.getDescription(),
                    task.getTaskStatus(),
                    task.getTaskPriority(),
                    task.getCreatedAt(),
                    task.getUpdatedAt(),
                    task.getDueDate()
            );
        }

    }

    public record UpdateTaskResponse(
            String title,
            String description,
            TaskStatus taskStatus,
            TaskPriority taskPriority,
            LocalDateTime updated_at,
            LocalDate dueDate
    ) {
        public static UpdateTaskResponse from(Task task) {
            return new UpdateTaskResponse(
                    task.getTitle(),
                    task.getDescription(),
                    task.getTaskStatus(),
                    task.getTaskPriority(),
                    task.getUpdatedAt(),
                    task.getDueDate()
            );
        }
    }

    public record ChangedTaskStatusResponse(
            TaskStatus taskStatus,
            LocalDate dueDate
    ){
        public static ChangedTaskStatusResponse from(Task task) {
            return new ChangedTaskStatusResponse(
                    task.getTaskStatus(),
                    task.getDueDate()
            );
        }
    }

    public record ChangedTaskPriorityResponse(
            TaskPriority taskPriority
    ){
        public static ChangedTaskPriorityResponse from(Task task) {
            return new ChangedTaskPriorityResponse(
                    task.getTaskPriority()
            );
        }
    }

}