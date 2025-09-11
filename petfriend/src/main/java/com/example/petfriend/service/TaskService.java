package com.example.petfriend.service;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.task.request.TaskRequest;
import com.example.petfriend.dto.task.response.TaskResponse;
import com.example.petfriend.security.UserPrincipal;

public interface TaskService {
    ResponseDto<TaskResponse.DetailTaskResponse> create(Long projectId, UserPrincipal userPrincipal, TaskRequest.TaskCreateRequest req);
}
