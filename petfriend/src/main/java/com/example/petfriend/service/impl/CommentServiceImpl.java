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
import com.example.petfriend.security.UserPrincipal;
import com.example.petfriend.service.CommentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ResponseDto<CommentResponseDto> createComment(Long taskId, UserPrincipal userPrincipal, CommentCreateRequestDto dto) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id의 Task를 찾을 수 없습니다."));

        User commenter = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new EntityNotFoundException("해당 Task의 사용자를 찾을 수 없습니다."));

        Comments comments = Comments.create(dto.content(), commenter);
        task.addComment(comments);
        Comments saved = commentRepository.save(comments);

        return ResponseDto.setSuccess("SUCCESS", CommentResponseDto.from(saved));
    }

    @Override
    @Transactional
    public ResponseDto<CommentResponseDto> updateComment(Long taskId, Long commentId, CommentUpdateRequestDto dto) {
        Comments comments = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id의 댓글을 찾을 수 없습니다."));

        if(!comments.getTask().getId().equals(taskId)) {
            throw new IllegalArgumentException("해당 댓글이 Task 내에 속해있지 않습니다.");
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
            throw new IllegalArgumentException("해당 댓글이 Task 내에 속해있지 않습니다.");
        }

        Task task = comment.getTask();
        task.removeComment(comment);

        return ResponseDto.setSuccess("SUCCESS", null);
    }

    @Override
    public ResponseDto<List<CommentResponseDto>> getComments(Long taskId, Long commenterId, boolean latestFirst) {
        Sort sort = latestFirst
                ? Sort.by(Sort.Direction.DESC, "createdAt")
                : Sort.by(Sort.Direction.ASC, "createdAt");

        List<Comments> comments = commentRepository.findByTaskIdAndOptionalCommenterId(taskId, commenterId, sort);

        List<CommentResponseDto> rep = comments.stream()
                .map(CommentResponseDto::from)
                .toList();

        return ResponseDto.setSuccess("SUCCESS", rep);
    }
}
