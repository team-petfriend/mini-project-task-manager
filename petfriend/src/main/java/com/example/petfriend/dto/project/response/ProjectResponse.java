package com.example.petfriend.dto.project.response;

import com.example.petfriend.entity.Project;
import java.time.LocalDateTime;

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
