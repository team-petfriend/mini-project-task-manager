package com.example.petfriend.dto.project.request;

public class ProjectRequest {
    // 현재 상태 : base 
    // 이후 어노테이션 보완 필요
    public record Create(
            String name
    ) {
    }

    public record Update(
            String name
    ) {
    }
}
