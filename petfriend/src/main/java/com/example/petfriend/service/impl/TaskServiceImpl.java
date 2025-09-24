package com.example.petfriend.service.impl;

import com.example.petfriend.common.enums.TaskPriority;
import com.example.petfriend.common.enums.TaskStatus;
import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.task.request.TaskRequest;
import com.example.petfriend.dto.task.response.TaskResponse;
import com.example.petfriend.entity.Project;
import com.example.petfriend.entity.Task;
import com.example.petfriend.repository.ProjectRepository;
import com.example.petfriend.repository.TaskRepository;
import com.example.petfriend.repository.UserRepository;
import com.example.petfriend.security.UserPrincipal;
import com.example.petfriend.security.util.AuthorizationChecker;
import com.example.petfriend.service.TaskService;;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {
    private final EntityManager em;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final AuthorizationChecker authorizationChecker;

// ==================== CRUD ========================================

    @Override
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public ResponseDto<TaskResponse.DetailTaskResponse> create(Long projectId, UserPrincipal userPrincipal, TaskRequest.@Valid TaskCreateRequest req) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalStateException("해당 id가 존재하지않습니다."));


        Task task = Task.builder()
                .project(project)
                .title(req.title())
                .description(req.description())
                .taskStatus(TaskStatus.TODO)
                .priority(TaskPriority.MEDIUM)
                .build();

        Task saved = taskRepository.save(task);

        TaskResponse.DetailTaskResponse data = TaskResponse.DetailTaskResponse.from(saved);

        return ResponseDto.setSuccess("SUCCESS", data);
    }

    @Override
    public ResponseDto<TaskResponse.DetailTaskResponse> update(Long taskId, TaskRequest.@Valid TaskUpdateRequest req) {
        return null;
    }

    @Override
    public ResponseDto<Void> delete(UserPrincipal userPrincipal, Long taskId) {
        return null;
    }

// ==================== CRUD ========================================

    @Override
    public ResponseDto<List<TaskResponse.TaskListResponse>> getAll(Long projectId) {
        return null;
    }

    @Override
    public ResponseDto<TaskResponse.DetailTaskResponse> getById(Long projectId, Long taskId) {
        return null;
    }

    @Override
    public ResponseDto<TaskResponse.DetailTaskResponse> statusUpdate(UserPrincipal userPrincipal, Long taskId) {
        return null;
    }

    @Override
    public ResponseDto<TaskResponse.DetailTaskResponse> priorityUpdate(UserPrincipal userPrincipal, Long taskId) {
        return null;
    }


}

