package com.example.petfriend.dto.project.request;

public class ProjectRequest {
    public record Create(
            String name
    ) {
    }

    public record Update(
            String name
    ) {
    }
}
