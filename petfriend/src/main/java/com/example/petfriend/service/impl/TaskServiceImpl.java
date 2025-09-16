package com.example.petfriend.service.impl;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.task.request.TaskRequest;
import com.example.petfriend.dto.task.response.TaskListResponse;
import com.example.petfriend.dto.task.response.TaskResponse;
import com.example.petfriend.entity.Project;
import com.example.petfriend.entity.Task;
import com.example.petfriend.repository.ProjectRepository;
import com.example.petfriend.repository.TaskRepository;
import com.example.petfriend.repository.UserRepository;
import com.example.petfriend.security.UserPrincipal;
import com.example.petfriend.service.TaskService;;
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
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN','OWNER')")
    public ResponseDto<TaskResponse.DetailTaskResponse> create(UserPrincipal userPrincipal, TaskRequest.@Valid TaskCreateRequest req) {

        return null;
    }

    @Override
    @Transactional
    public ResponseDto<TaskResponse.DetailTaskResponse> update(Long projectId, Long taskId, TaskRequest.@Valid TaskUpdateRequest dto){

        return null;
    }

    @Override
    public ResponseDto<Void> delete(Long projectId, Long taskId) {
        return null;
    }

    @Override
    public ResponseDto<TaskResponse.DetailTaskResponse> getBuId(Long projectId, Long taskId) {
        return null;
    }

    @Override
    public ResponseDto<List<TaskResponse.TaskListResponse>> getAll() {
        return null;
    }

}
