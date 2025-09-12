package com.example.petfriend.controller;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.comment.request.CommentCreateRequestDto;
import com.example.petfriend.dto.comment.request.CommentUpdateRequestDto;
import com.example.petfriend.dto.comment.response.CommentResponseDto;
import com.example.petfriend.security.UserPrincipal;
import com.example.petfriend.service.CommentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
@RequiredArgsConstructor
@Validated
public class CommentsController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ResponseDto<CommentResponseDto>> createComment(
            @PathVariable("taskId") @Positive(message = "taskId는 1이상의 정수여야 합니다.") Long taskId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody CommentCreateRequestDto dto
    ) {
        ResponseDto<CommentResponseDto> response = commentService.createComment(taskId, userPrincipal, dto);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("{commentId}")
    public ResponseEntity<ResponseDto<CommentResponseDto>> updateComment(
            @PathVariable("taskId") @Positive(message = "taskId는 1이상의 정수여야 합니다.") Long taskId,
            @PathVariable("commentId") @Positive(message = "commentId는 1이상의 정수여야 합니다.") Long commentId,
            @Valid @RequestBody CommentUpdateRequestDto dto
    ) {
        ResponseDto<CommentResponseDto> response = commentService.updateComment(taskId, commentId, dto);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("{commentId}")
    public ResponseEntity<ResponseDto<CommentResponseDto>> deleteComment(
            @PathVariable("taskId") @Positive(message = "taskId는 1이상의 정수여야 합니다.") Long taskId,
            @PathVariable("commentId") @Positive(message = "commentId는 1이상의 정수여야 합니다.") Long commentId
    ) {
        ResponseDto<CommentResponseDto> response = commentService.deleteComment(taskId, commentId);
        return ResponseEntity.ok().body(response);
    }
}
