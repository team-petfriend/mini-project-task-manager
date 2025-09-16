package com.example.petfriend.service;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.task.request.TaskRequest;
import com.example.petfriend.dto.task.response.TaskListResponse;
import com.example.petfriend.dto.task.response.TaskResponse;
import com.example.petfriend.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskService {


    ResponseDto<TaskResponse.DetailTaskResponse> create(UserPrincipal userPrincipal, TaskRequest.@Valid TaskCreateRequest req);



    ResponseDto<TaskResponse.DetailTaskResponse> update(Long projectId, Long taskId, TaskRequest.@Valid TaskUpdateRequest dto);

    ResponseDto<Void> delete(Long projectId, Long taskId);

    ResponseDto<TaskResponse.DetailTaskResponse> getBuId(Long projectId, Long taskId);

    ResponseDto<List<TaskResponse.TaskListResponse>> getAll();
}
