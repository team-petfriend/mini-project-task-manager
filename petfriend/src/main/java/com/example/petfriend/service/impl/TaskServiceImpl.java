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
        validateTitleAndDescription(req.title(), req.description());

        if (projectId == null) throw new IllegalArgumentException("PROJECT_ID는 필수입니다.");

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalStateException("TASK를 찾을 수 없습니다."));

        task.update(req.title(), req.description());

        taskRepository.flush();;

        TaskResponse.DetailTaskResponse data = TaskResponse.DetailTaskResponse.from(task);

        return ResponseDto.setSuccess("SUCCESS", data);
    }



    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public ResponseDto<Object> delete(Long projectId, Long taskId) {
        if (projectId == null) throw new IllegalArgumentException("PROJECT_ID는 필수입니다.");

        Task task = taskRepository.findById(projectId)
                .orElseThrow(() -> new IllegalStateException("TASK를 찾을 수 없습니다."));

        taskRepository.delete(task);

        return ResponseDto.setSuccess("SUCCESS", null);
    }

    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()" )
    public ResponseDto<TaskResponse.DetailTaskResponse> statusUpdate(UserPrincipal userPrincipal, Long taskId) {
        TaskResponse.DetailTaskResponse data = null;

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalStateException("TASK를 찾을 수 없습니다. id=" + taskId));

        if (task.getTaskStatus() != TaskStatus.TODO) {
            throw new IllegalArgumentException("TODO 상태만 변경할 수 있습니다.");
        }

        task.setTaskStatus(TaskStatus.DONE);

        return ResponseDto.setSuccess("상태 변경이 정삭적으로 변경되었습니다.", data);
    }


    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public ResponseDto<TaskResponse.DetailTaskResponse> priorityUpdate(UserPrincipal userPrincipal, Long taskId) {
        TaskResponse.DetailTaskResponse data = null;

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalStateException("TASK를 찾을 수 없습니다. id=" + taskId));

        if (task.getTaskPriority() != TaskPriority.MEDIUM) {
            throw new IllegalArgumentException("MEDIUM 상태만 변경할 수 있습니다.");
        }
            task.setTaskPriority(TaskPriority.MEDIUM);

            return ResponseDto.setSuccess("중요도 변경이 정상적으로 변경되었습니다.", data);

    }
    private void validateTitleAndDescription(@NotBlank(message = "title은 필수 값 입니다.") String title, String description) {

    }
}

