package com.example.petfriend.service.impl;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.comment.request.CommentCreateRequestDto;
import com.example.petfriend.dto.comment.response.CommentResponseDto;
import com.example.petfriend.entity.Task;
import com.example.petfriend.repository.CommentRepository;
import com.example.petfriend.repository.TaskRepository;
import com.example.petfriend.service.CommentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;

    @Override
    public ResponseDto<CommentResponseDto> createComment(Long taskId, CommentCreateRequestDto dto) {
        

        return null;
    }

    @Override
    public ResponseDto<CommentResponseDto> deleteComment(Long taskId, Long commentId) {
        return null;
    }

    @Override
    public ResponseDto<CommentResponseDto> updateComment(Long taskId, Long commentId, CommentCreateRequestDto dto) {
        return null;
    }
}
