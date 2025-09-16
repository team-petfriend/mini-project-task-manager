package com.example.petfriend.service.impl;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.task.request.TaskRequest;
import com.example.petfriend.dto.task.response.TaskResponse;
import com.example.petfriend.repository.ProjectRepository;
import com.example.petfriend.repository.TaskRepository;
import com.example.petfriend.repository.UserRepository;
import com.example.petfriend.security.UserPrincipal;
import com.example.petfriend.service.TaskService;;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseDto<TaskResponse.DetailTaskResponse> create(Long projectId, UserPrincipal userPrincipal, TaskRequest.@Valid TaskCreateRequest req) {
        return null;
    }

    @Override
    public ResponseDto<List<TaskResponse.TaskListResponse>> getAll(Long projectId) {
        return null;
    }

    @Override
    public ResponseDto<TaskResponse.DetailTaskResponse> getById(Long projectId, Long taskId) {
        return null;
    }

    @Override
    public ResponseDto<TaskResponse.DetailTaskResponse> update(Long projectId, Long taskId, TaskRequest.@Valid TaskUpdateRequest req) {
        return null;
    }

    @Override
    public void delete(Long projectId, Long taskId) {

    }
}
