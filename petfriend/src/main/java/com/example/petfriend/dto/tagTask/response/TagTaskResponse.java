package com.example.petfriend.dto.tagTask.response;

import com.example.petfriend.dto.tag.response.TagResponse;
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

            List<String> tagNames = List.of(
                    taskTag.getTag().getName()
            );

            return new TagTaskResponse.DetailTag(
                    taskTag.getTag().getProject().getId(),
                    taskTag.getTag().getName(),
                    taskTag.getTag().getColor(),
                    taskList
            );
        }
    }

    public record TagToTaskProject(
            Long projectId,
            List<TagResponse.DetailTag> tags,
            List<TaskResponse.DetailTaskResponse> tasks
    ) {

        public static TagTaskResponse.TagToTaskProject from(List<TaskTag> taskTag) {
            List<TagResponse.DetailTag> tagList = taskTag.stream()
                    .map(tt -> TagResponse.DetailTag.from(tt.getTag()))
                    .distinct()
                    .toList();

            List<TaskResponse.DetailTaskResponse> taskList = taskTag.stream()
                    .map(tt -> TaskResponse.DetailTaskResponse.from(tt.getTask()))
                    .distinct()
                    .toList();

            return new TagToTaskProject(
                    taskTag.get(0).getTag().getProject().getId(),
                    tagList,
                    taskList
            );
        }

    }
}
