package com.example.petfriend.service.impl;

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
import jakarta.validation.Valid;
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

    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public ResponseDto<TaskResponse.DetailTaskResponse> create(Long projectId, UserPrincipal userPrincipal, TaskRequest.@Valid TaskCreateRequest req) {
        TaskResponse.DetailTaskResponse data =null;

        if (req.tasks() == null || req.tasks().isEmpty())
            throw new IllegalArgumentException("TASK 항목이 비어있습니다.");

        Long authProject = userPrincipal.getId();

        Project projectRef = em.getReference(Project.class, authProject);

        Task task = Task.builder()
                .project(projectRef)
                .taskStatus(TaskStatus.TODO)
                .build();

        for (TaskRequest.TaskLine line: req.tasks()) {
            if (line.taskStatus() == null) throw new IllegalArgumentException("작업 상태는 필수입니다.");
            if (line.description() == null) throw new IllegalArgumentException("우선 순위는 필수입니다.");
        }
        Task saved = taskRepository.save(task);

        return ResponseDto.setSuccess("TASK가  성공적으로 등록되었습니다.", data);
    }

    @Override
    public ResponseDto<List<TaskResponse.TaskListResponse>> getAll(Long projectId) {
        List<TaskResponse.TaskListResponse> data = null;

        data = taskRepository.findAll().stream()
                .map(TaskResponse.TaskListResponse::from)
                .toList();
        return ResponseDto.setSuccess("SUCCESS", data);
    }

    @Override
    public ResponseDto<TaskResponse.DetailTaskResponse> getById(Long projectId, Long taskId) {
        TaskResponse.DetailTaskResponse data = null;

        if (projectId == null) throw new IllegalArgumentException("PROJECT_ID는 필수입니다.");

        Task task = taskRepository.findById(projectId)
                .orElseThrow(() -> new IllegalStateException("TASK를 찾을수가 없습니다."));
        data = TaskResponse.DetailTaskResponse.from(task);
        return ResponseDto.setSuccess("SUCCESS",data);
    }

    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public ResponseDto<TaskResponse.DetailTaskResponse> update(Long projectId, Long taskId, TaskRequest.@Valid TaskUpdateRequest req) {
        validateTitleAndDescription(request)
        return null;
    }

    @Override
    public void delete(Long projectId, Long taskId) {

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
