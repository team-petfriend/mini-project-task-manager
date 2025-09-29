package com.example.petfriend.service;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.comment.request.CommentCreateRequestDto;
import com.example.petfriend.dto.comment.request.CommentUpdateRequestDto;
import com.example.petfriend.dto.comment.response.CommentResponseDto;
import com.example.petfriend.security.UserPrincipal;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import java.util.List;

public interface CommentService {
    ResponseDto<CommentResponseDto> createComment(@Positive(message = "taskId는 1이상의 정수여야 합니다.") Long taskId, UserPrincipal userPrincipal, @Valid CommentCreateRequestDto dto);
    ResponseDto<CommentResponseDto> updateComment(@Positive(message = "taskId는 1이상의 정수여야 합니다.") Long taskId, @Positive(message = "commentId는 1이상의 정수여야 합니다.") Long commentId, @Valid CommentUpdateRequestDto dto);
    ResponseDto<CommentResponseDto> deleteComment(@Positive(message = "taskId는 1이상의 정수여야 합니다.") Long taskId, @Positive(message = "commentId는 1이상의 정수여야 합니다.") Long commentId);
    ResponseDto<List<CommentResponseDto>> getComments(@Positive Long taskId, Long commenterId, boolean latestFirst);
}
