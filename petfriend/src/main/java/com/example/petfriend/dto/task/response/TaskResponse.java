package com.example.petfriend.dto.task.response;

import com.example.petfriend.common.enums.TaskPriority;
import com.example.petfriend.common.enums.TaskStatus;
import com.example.petfriend.common.utils.DateUtils;
import com.example.petfriend.entity.Task;
import com.example.petfriend.entity.TaskAssignees;

import java.time.LocalDateTime;
import java.util.Set;

public class TaskResponse {
    public record DetailTaskResponse(
            String title,
            String description,
            TaskStatus taskStatus,
            TaskPriority taskPriority,
            Set<TaskAssignees> assigneesId, // 담당자
            LocalDateTime created_at,
            LocalDateTime updated_at
    ){
        public static DetailTaskResponse from(Task task) {
            return new DetailTaskResponse(
                    task.getTitle(),
                    task.getDescription(),
                    task.getStatus(),
                    task.getPriority(),
                    task.getAssignees(),
                    task.getCreatedAt(),
                    task.getUpdatedAt()
            );
        }

    }
}