package com.example.petfriend.dto.project.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectRequest {
    public record Create(
            @NotBlank @Size(max = 100)
            String name
    ) {
    }

    public record Update(
            @NotBlank @Size(max = 100)
            String name
    ) {
    }
}
