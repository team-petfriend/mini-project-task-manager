package com.example.petfriend.dto.comment.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CommentCreateRequestDto(
        @NotBlank(message = "내용은 필수 입력 값입니다.")
        @Size(max = 500, message = "내용은 최대 500자까지 입력 가능합니다.")
        String content
) {}