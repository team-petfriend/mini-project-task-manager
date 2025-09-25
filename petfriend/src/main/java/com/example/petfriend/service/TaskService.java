package com.example.petfriend.service;

import com.example.petfriend.common.enums.TaskPriority;
import com.example.petfriend.common.enums.TaskStatus;
import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.task.request.TaskRequest;
import com.example.petfriend.dto.task.response.TaskResponse;
import com.example.petfriend.security.UserPrincipal;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;


public interface TaskService {
    // ==================== CRUD ==========================
    ResponseDto<TaskResponse.DetailTaskResponse> create(@Positive(message = "projectId는 1이상의 정수여야 합니다.") Long projectId, UserPrincipal userPrincipal, TaskRequest.@Valid TaskCreateRequest req);
    ResponseDto<TaskResponse.UpdateTaskResponse> update(UserPrincipal userPrincipal, Long taskId, TaskRequest.@Valid TaskUpdateRequest req);
    ResponseDto<Void> delete(UserPrincipal userPrincipal, Long taskId);


    // ==================== UPDATE ==========================
    ResponseDto<TaskResponse.ChangedTaskStatusResponse> statusUpdate(UserPrincipal userPrincipal, TaskStatus taskStatus, Long taskId);
    ResponseDto<TaskResponse.ChangedTaskPriorityResponse> priorityUpdate(UserPrincipal userPrincipal, TaskPriority taskPriority, Long taskId);





}
