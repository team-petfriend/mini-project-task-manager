package com.example.petfriend.dto.comment.response;

import com.example.petfriend.entity.Comments;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CommentResponseDto(
        Long id,
        Long taskId,
        String content,
        String commenter,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CommentResponseDto from (Comments comment) {
        if (comment == null) return null;
        return new CommentResponseDto(
                comment.getId(),
                comment.getTask() != null ? comment.getTask().getId() : null,
                comment.getContent(),
                comment.getCommenter() != null ? comment.getCommenter().getNickname() : null,
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
    public CommentResponseDto summarized(int maxLen){
        String summarized = content == null ? null :
                (content.length() <= maxLen ? content : content.substring(0, maxLen) + "...");

        return new CommentResponseDto(id, taskId, summarized, commenter, createdAt, updatedAt);
    }
}