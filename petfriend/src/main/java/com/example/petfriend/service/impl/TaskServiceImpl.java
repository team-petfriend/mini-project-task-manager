package com.example.petfriend.service.impl;

import com.example.petfriend.common.enums.TaskPriority;
import com.example.petfriend.common.enums.TaskStatus;
import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.task.request.TaskRequest;
import com.example.petfriend.dto.task.response.TaskResponse;
import com.example.petfriend.entity.Project;
import com.example.petfriend.entity.Task;
import com.example.petfriend.entity.TaskAssignees;
import com.example.petfriend.entity.User;
import com.example.petfriend.repository.ProjectRepository;
import com.example.petfriend.repository.TaskRepository;
import com.example.petfriend.repository.UserRepository;
import com.example.petfriend.security.UserPrincipal;
import com.example.petfriend.security.util.PrincipalUtils;
import com.example.petfriend.service.ProjectService;
import com.example.petfriend.service.TaskService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {
    private final EntityManager em;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN','OWNER')")
    public ResponseDto<TaskResponse.DetailTaskResponse> create(Long projectId, UserPrincipal userPrincipal, TaskRequest.TaskCreateRequest req) {
        TaskResponse.DetailTaskResponse data = null;

        Project project = projectRepository.getDetailById(projectId)
                .orElseThrow( () -> new EntityNotFoundException("해당하는 프로젝트 ID가 존재하지않습니다. : id = " +  projectId));



        Long authUserId = userPrincipal.getId();

        User userRef = em.getReference(User.class, authUserId);


        Task task = Task.builder()
                .title(req.title())
                .description(req.description())
                .status(TaskStatus.TODO)
                .priority(TaskPriority.MEDIUM)
                .assignees(req.assigneesId())
                .build();




        return null;
    }
}
