package com.example.petfriend.service;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.task.request.TaskRequest;
import com.example.petfriend.dto.task.response.TaskResponse;
import com.example.petfriend.security.UserPrincipal;
import jakarta.validation.Valid;

import java.util.List;


public interface TaskService {
    ResponseDto<TaskResponse.DetailTaskResponse> create(Long projectId, UserPrincipal userPrincipal, TaskRequest.@Valid TaskCreateRequest req);
    ResponseDto<List<TaskResponse.TaskListResponse>> getAll(Long projectId);
    ResponseDto<TaskResponse.DetailTaskResponse> getById(Long projectId, Long taskId);
    ResponseDto<TaskResponse.DetailTaskResponse> update(Long projectId, Long taskId, TaskRequest.@Valid TaskUpdateRequest req);
    ResponseDto<Object> delete(Long projectId, Long taskId);
    ResponseDto<TaskResponse.DetailTaskResponse> statusUpdate(UserPrincipal userPrincipal, Long taskId);
    ResponseDto<TaskResponse.DetailTaskResponse> priorityUpdate(UserPrincipal userPrincipal, Long taskId);
}
