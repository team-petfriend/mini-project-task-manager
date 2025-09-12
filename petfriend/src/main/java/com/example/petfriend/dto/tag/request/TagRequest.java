package com.example.petfriend.dto.tag.request;

import com.example.petfriend.entity.Project;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TagRequest {
    public record createTag (

            @NotBlank(message = "제목은 필수 입력 값 입니다.")
            @Size(max = 50, message = "제목의 길이값은 최대 50자까지 입니다.")
            String name,

            @NotBlank(message = "색은 필수 입력 값 입니다.")
            @Size(max = 100, message = "색깔 내용은 최대 100자까지 입니다.")
            String color,

            @NotNull(message = "프로젝트의 ID값은 필수 입니다.")
            Long projectId
    ) {}

    public record updateTag (

            @NotBlank(message = "제목은 필수 입력 값 입니다.")
            @Size(max = 50, message = "제목의 길이값은 최대 50자까지 입니다.")
            String name,

            @NotBlank(message = "색은 필수 입력 값 입니다.")
            @Size(max = 100, message = "색깔 내용은 최대 100자까지 입니다.")
            String color
    ) {}
}
