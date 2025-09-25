package com.example.petfriend.controller;

import com.example.petfriend.common.contants.ApiMappingPattern;
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

import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.Comments.ROOT)
@RequiredArgsConstructor
@Validated
public class CommentsController {
    private final CommentService commentService;

    @PostMapping
    // http://localhost:8080/api/v1/tasks/:taskId/comments
    public ResponseEntity<ResponseDto<CommentResponseDto>> createComment(
            @PathVariable("taskId") @Positive(message = "taskId는 1이상의 정수여야 합니다.") Long taskId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody CommentCreateRequestDto dto
    ) {
        ResponseDto<CommentResponseDto> response = commentService.createComment(taskId, userPrincipal, dto);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping(ApiMappingPattern.Comments.ID_ONLY)
    // http://localhost:8080/api/v1/tasks/:taskId/comments/{commentId}
    public ResponseEntity<ResponseDto<CommentResponseDto>> updateComment(
            @PathVariable("taskId") @Positive(message = "taskId는 1이상의 정수여야 합니다.") Long taskId,
            @PathVariable("commentId") @Positive(message = "commentId는 1이상의 정수여야 합니다.") Long commentId,
            @Valid @RequestBody CommentUpdateRequestDto dto
    ) {
        ResponseDto<CommentResponseDto> response = commentService.updateComment(taskId, commentId, dto);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping(ApiMappingPattern.Comments.ID_ONLY)
    // http://localhost:8080/api/v1/tasks/:taskId/comments/{commentId}
    public ResponseEntity<ResponseDto<CommentResponseDto>> deleteComment(
            @PathVariable("taskId") @Positive(message = "taskId는 1이상의 정수여야 합니다.") Long taskId,
            @PathVariable("commentId") @Positive(message = "commentId는 1이상의 정수여야 합니다.") Long commentId
    ) {
        ResponseDto<CommentResponseDto> response = commentService.deleteComment(taskId, commentId);
        return ResponseEntity.ok().body(response);
    }

    // 최신댓글 순 정렬 (true = 최신순, false = 오래된 댓글 순)
    @GetMapping(ApiMappingPattern.Comments.SORT)
    // http://localhost:8080/api/v1/tasks/:taskId/comments/sort?latestFirst=true
    public ResponseEntity<ResponseDto<List<CommentResponseDto>>> getComments(
            @PathVariable("taskId") @Positive Long taskId,
            @RequestParam(required = false) Long commenterId,
            @RequestParam(defaultValue = "true") boolean latestFirst
    ) {
        ResponseDto<List<CommentResponseDto>> response
                = commentService.getComments(taskId, commenterId, latestFirst);
        return ResponseEntity.ok().body(response);
    }
}
