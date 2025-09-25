package com.example.petfriend.dto.tag.request;

import com.example.petfriend.common.enums.TagType;
import com.example.petfriend.entity.Project;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TagRequest {
    public record createTag (

            TagType name,

            @NotBlank(message = "색은 필수 입력 값 입니다.")
            @Size(max = 100, message = "색깔 내용은 최대 100자까지 입니다.")
            String color
    ) {}

    public record updateTag (

            TagType name,

            @NotBlank(message = "색은 필수 입력 값 입니다.")
            @Size(max = 100, message = "색깔 내용은 최대 100자까지 입니다.")
            String color
    ) {}
}
