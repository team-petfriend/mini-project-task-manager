package com.example.petfriend.service.impl;

import com.example.petfriend.common.enums.Field;
import com.example.petfriend.common.enums.TaskPriority;
import com.example.petfriend.common.enums.TaskStatus;
import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.task.request.TaskRequest;
import com.example.petfriend.dto.task.response.TaskResponse;
import com.example.petfriend.entity.Project;
import com.example.petfriend.entity.Task;
import com.example.petfriend.entity.TaskHistory;
import com.example.petfriend.entity.User;
import com.example.petfriend.repository.ProjectRepository;
import com.example.petfriend.repository.TaskHistoryRepository;
import com.example.petfriend.repository.TaskRepository;
import com.example.petfriend.repository.UserRepository;
import com.example.petfriend.security.UserPrincipal;
import com.example.petfriend.security.util.AuthorizationChecker;
import com.example.petfriend.service.TaskService;;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final TaskHistoryRepository taskHistoryRepository;
    private final UserRepository userRepository;
// ==================== CRUD ========================================

    @Override
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public ResponseDto<TaskResponse.DetailTaskResponse> create(Long projectId, UserPrincipal userPrincipal, TaskRequest.@Valid TaskCreateRequest req) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalStateException("해당 id가 존재하지않습니다."));

        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 id의 사용자가 존재하지 않습니다."));

        Task task = Task.builder()
                .project(project)
                .title(req.title())
                .description(req.description())
                .taskStatus(TaskStatus.TODO)
                .priority(TaskPriority.MEDIUM)
                .build();

        Task saved = taskRepository.save(task);
        TaskResponse.DetailTaskResponse data = TaskResponse.DetailTaskResponse.from(saved);

        taskHistory(saved, user, Field.STATUS, null, saved.getTaskStatus().name());
        taskHistory(saved, user, Field.PRIORITY, null, saved.getTaskPriority().name());

        return ResponseDto.setSuccess("SUCCESS", data);
    }

    @Override
    @Transactional
    public ResponseDto<TaskResponse.UpdateTaskResponse> update(UserPrincipal userPrincipal, Long taskId, TaskRequest.@Valid TaskUpdateRequest req) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalStateException("해당 ID가 존재하지않습니다."));
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 id의 사용자가 존재하지 않습니다."));
        String old_Title = task.getTitle();
        task.update(
                req.title(),
                req.description()
        );

        if(req.title() != null &&!old_Title.equals(req.title())){
            taskHistory(task, user, Field.TITLE, old_Title, req.title());
        }

        taskRepository.flush();

        TaskResponse.UpdateTaskResponse data = TaskResponse.UpdateTaskResponse.from(task);
        return ResponseDto.setSuccess("SUCCESS", data);
    }

    @Override
    @Transactional
    public ResponseDto<Void> delete(UserPrincipal userPrincipal, Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalStateException("해당 ID가 존재하지않습니다."));
        taskRepository.delete(task);
        return ResponseDto.setSuccess("SUCCESS", null);
    }

// ==================== 상태 변경 Patch ========================================

    @Override
    @Transactional
    public ResponseDto<TaskResponse.ChangedTaskStatusResponse> statusUpdate(UserPrincipal userPrincipal, TaskStatus taskStatus, Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalStateException("해당 ID가 존재하지않습니다."));

        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 id의 사용자가 존재하지 않습니다."));

        if (task.getTaskStatus() == TaskStatus.DONE) {
            throw new IllegalArgumentException("완료된 Task는 더이상 수정할 수 없습니다.");
        }

        if (task.getTaskStatus().equals(taskStatus)) {
            throw new IllegalArgumentException("이미 동일한 상태입니다. : " + taskStatus);
        }

        String old_Status = task.getTaskStatus().name();
        task.changeTaskStatus(taskStatus);
        TaskResponse.ChangedTaskStatusResponse data = TaskResponse.ChangedTaskStatusResponse.from(task);

        taskHistory(task, user, Field.STATUS, old_Status, task.getTaskStatus().name());

        return ResponseDto.setSuccess("SUCCESS", data);
    }

    @Override
    @Transactional
    public ResponseDto<TaskResponse.ChangedTaskPriorityResponse> priorityUpdate(UserPrincipal userPrincipal, TaskPriority taskPriority, Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalStateException("해당 ID가 존재하지않습니다."));
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 id의 사용자가 존재하지 않습니다."));

        if (task.getTaskPriority().equals(taskPriority)) {
            throw new IllegalArgumentException("이미 동일한 순위입니다. : " + taskPriority);
        }

        String old_Priority = task.getTaskPriority().name();

        task.changeTaskPriority(taskPriority);
        TaskResponse.ChangedTaskPriorityResponse data = TaskResponse.ChangedTaskPriorityResponse.from(task);

        taskHistory(task, user, Field.PRIORITY, old_Priority, task.getTaskPriority().name());

        return ResponseDto.setSuccess("SUCCESS", data);
    }

    private void taskHistory(Task task, User user, Field field, String old_value, String new_value) {
        TaskHistory taskHistory = TaskHistory.builder()
                .task(task)
                .user(user)
                .field(field)
                .old_value(old_value)
                .new_value(new_value)
                .build();
        taskHistoryRepository.save(taskHistory);
    }


}

