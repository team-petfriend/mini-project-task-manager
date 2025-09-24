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
import org.springframework.security.access.prepost.PreAuthorize;
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

    // 로그인한 사용자
    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
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

    // 작성자 또는 MANAGER / ADMIN
    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN') or @authz.isCommentAuthor(#commentId, authentication)")
    public ResponseDto<CommentResponseDto> updateComment(Long taskId, Long commentId, CommentUpdateRequestDto dto) {
        Comments comments = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id의 댓글을 찾을 수 없습니다."));

        if(!comments.getTask().getId().equals(taskId)) {
            throw new IllegalArgumentException("해당 댓글이 Task 내에 속해있지 않습니다.");
        }

        comments.changeContent(dto.content());

        return ResponseDto.setSuccess("SUCCESS", CommentResponseDto.from(comments));

    }

    // 작성자 또는 ADMIN만 삭제 가능
    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @authz.isCommentAuthor(#commentId, authentication)")
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
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public ResponseDto<List<CommentResponseDto>> getComments(Long taskId, Long commenterId, boolean latestFirst) {
        Sort sort = latestFirst
                ? Sort.by(Sort.Direction.DESC, "createdAt")
                : Sort.by(Sort.Direction.ASC, "createdAt");

        List<Comments> comments = commentRepository.findByTaskIdAndOptionalCommenterId(taskId, commenterId, sort);

        List<CommentResponseDto> rep = comments.stream()
                .map(CommentResponseDto::from)
                .map(dto -> dto.summarized(8))
                .toList();

        return ResponseDto.setSuccess("SUCCESS", rep);
    }
}
