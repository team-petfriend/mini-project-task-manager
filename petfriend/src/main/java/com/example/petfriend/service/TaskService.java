package com.example.petfriend.service;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.task.request.TaskRequest;
import com.example.petfriend.dto.task.response.TaskResponse;
import com.example.petfriend.security.UserPrincipal;
import jakarta.validation.Valid;

import java.util.List;


public interface TaskService {
    ResponseDto<List<TaskResponse.TaskListResponse>> getAll(Long projectId);
    ResponseDto<TaskResponse.DetailTaskResponse> getById(Long projectId, Long taskId);
    ResponseDto<TaskResponse.DetailTaskResponse> statusUpdate(UserPrincipal userPrincipal, Long taskId);
    ResponseDto<TaskResponse.DetailTaskResponse> priorityUpdate(UserPrincipal userPrincipal, Long taskId);


    // ==================== CRUD ==========================
    ResponseDto<TaskResponse.DetailTaskResponse> create(UserPrincipal userPrincipal, TaskRequest.@Valid TaskCreateRequest req);

    ResponseDto<TaskResponse.DetailTaskResponse> update(Long taskId, TaskRequest.@Valid TaskUpdateRequest req);

    ResponseDto<Void> delete(UserPrincipal userPrincipal, Long taskId);
}
