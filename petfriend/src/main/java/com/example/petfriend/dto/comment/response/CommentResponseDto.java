package com.example.petfriend.dto.comment.response;

import com.example.petfriend.entity.Comments;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CommentResponseDto(
        Long id,
        Long taskId,
        String content,
        String commenter
) {
    public static CommentResponseDto from (Comments comment) {
        if (comment == null) return null;
        return new CommentResponseDto(
          comment.getId(),
          comment.getTask() != null ? comment.getTask().getId() : null,
          comment.getContent(),
          comment.getCommenter()
        );
    }
}
