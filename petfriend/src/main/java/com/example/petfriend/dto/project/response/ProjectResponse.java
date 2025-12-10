package com.example.petfriend.dto.project.response;

import com.example.petfriend.dto.tag.response.TagResponse;
import com.example.petfriend.dto.task.response.TaskResponse;
import com.example.petfriend.entity.Project;
import com.example.petfriend.entity.Tag;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectResponse {
    public record DetailResponse(
            Long id,
            String name,
            LocalDateTime CreatedAt,
            LocalDateTime UpdatedAt,
            List<TagResponse.DetailTag> tags,
            List<TaskResponse.DetailTaskResponse> tasks
    ) {

        public static DetailResponse from(Project project) {
            return new DetailResponse(
                    project.getId(),
                    project.getName(),
                    project.getCreatedAt(),
                    project.getUpdatedAt(),
                    project.getTags().stream().
                            map(TagResponse.DetailTag::from)
                            .toList(),
                    project.getTasks().stream()
                            .map(TaskResponse.DetailTaskResponse::from)
                            .toList()
            );
        }
    }

}
