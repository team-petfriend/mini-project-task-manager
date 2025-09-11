package com.example.petfriend.service.impl;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.comment.request.CommentCreateRequestDto;
import com.example.petfriend.dto.comment.request.CommentUpdateRequestDto;
import com.example.petfriend.dto.comment.response.CommentResponseDto;
import com.example.petfriend.entity.Comments;
import com.example.petfriend.entity.Task;
import com.example.petfriend.entity.User;
import com.example.petfriend.repository.CommentRepository;
import com.example.petfriend.repository.TaskRepository;
import com.example.petfriend.repository.UserRepository;
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
    private final UserRepository userRepository;

    @Override
    public ResponseDto<CommentResponseDto> createComment(Long taskId, CommentCreateRequestDto dto) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id의 Task를 찾을 수 없습니다."));

        User commenter = userRepository.findById(Long.valueOf(dto.commenter()))
                .orElseThrow(() -> new EntityNotFoundException("해당 Task의 사용자를 찾을 수 없습니다."));

        Comments comment = Comments.create(dto.content(), commenter);

        task.addComment(comment);

        commentRepository.save(comment);

        return ResponseDto.setSuccess("SUCCESS", CommentResponseDto.from(comment));
    }

    @Override
    @Transactional
    public ResponseDto<CommentResponseDto> updateComment(Long taskId, Long commentId, CommentUpdateRequestDto dto) {
        Comments comments = taskRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id의 댓글을 찾을 수 없습니다."));

        if(!comments.getTask().getId().equals(taskId)) {
            throw new IllegalAccessException("해당 댓글이 Task 내에 속해있지 않습니다.");
        }

        comments.changeContent(dto.content());

        return ResponseDto.setSuccess("SUCCESS", CommentResponseDto.from(comments));
    }

    @Override
    @Transactional
    public ResponseDto<CommentResponseDto> deleteComment(Long taskId, Long commentId) {
        Comments comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("해당 Task의 댓글을 찾을 수 없습니다."));

        if (!comment.getTask().getId().equals(taskId)) {
            throw new IllegalAccessException("해당 댓글이 Task 내에 속해있지 않습니다.")
        }

        Task task = comment.getTask();
        task.removeComment(comment); // Task 엔티티에 removeComment 메서드 필요

        return ResponseDto.setSuccess("SUCCESS", null);
    }
}
