package com.example.petfriend.dto.tagTask.response;

import com.example.petfriend.dto.task.response.TaskResponse;
import com.example.petfriend.entity.TaskTag;

import java.util.List;

public class TagTaskResponse {

    public record DetailTag(
            Long projectId,
            String name,
            String color,
            List<TaskResponse.DetailTaskResponse> tasks
    ) {
        public static TagTaskResponse.DetailTag from(TaskTag taskTag) {
            if (taskTag == null) return null;

            List<TaskResponse.DetailTaskResponse> taskList = List.of(
                    TaskResponse.DetailTaskResponse.from(taskTag.getTask())
            );

            return new TagTaskResponse.DetailTag(
                    taskTag.getTag().getProject().getId(),
                    taskTag.getTag().getName(),
                    taskTag.getTag().getColor(),
                    taskList
            );
        }
    }
}
