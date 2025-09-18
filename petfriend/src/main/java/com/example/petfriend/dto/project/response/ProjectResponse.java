package com.example.petfriend.dto.project.response;

import com.example.petfriend.entity.Project;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectResponse {
    public record DetailResponse(
            Long id,
            String name,
            LocalDateTime CreatedAt,
            LocalDateTime UpdatedAt

    ) {

        public static DetailResponse from(Project project) {
            return new DetailResponse(
                    project.getId(),
                    project.getName(),
                    project.getCreatedAt(),
                    project.getUpdatedAt()
            );
        }
    }

}
