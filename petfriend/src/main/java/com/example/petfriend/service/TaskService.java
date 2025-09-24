package com.example.petfriend.service;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.task.request.TaskRequest;
import com.example.petfriend.dto.task.response.TaskResponse;
import com.example.petfriend.entity.Project;
import com.example.petfriend.security.UserPrincipal;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import java.util.List;


public interface TaskService {
    ResponseDto<List<TaskResponse.TaskListResponse>> getAll(Long projectId);
    ResponseDto<TaskResponse.DetailTaskResponse> getById(Long projectId, Long taskId);
    ResponseDto<TaskResponse.DetailTaskResponse> statusUpdate(UserPrincipal userPrincipal, Long taskId);
    ResponseDto<TaskResponse.DetailTaskResponse> priorityUpdate(UserPrincipal userPrincipal, Long taskId);


    // ==================== CRUD ==========================
    ResponseDto<TaskResponse.DetailTaskResponse> create(@Positive(message = "projectId는 1이상의 정수여야 합니다.") Long projectId, UserPrincipal userPrincipal, TaskRequest.@Valid TaskCreateRequest req);

    ResponseDto<TaskResponse.DetailTaskResponse> update(Long taskId, TaskRequest.@Valid TaskUpdateRequest req);

    ResponseDto<Void> delete(UserPrincipal userPrincipal, Long taskId);
}
