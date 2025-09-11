package com.example.petfriend.controller;

import com.example.petfriend.common.enums.TaskStatus;
import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.task.request.TaskRequest;
import com.example.petfriend.dto.task.response.TaskResponse;
import com.example.petfriend.security.UserPrincipal;
import com.example.petfriend.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class TasksController {
private final TaskService taskService;

    @PostMapping("/{projectId}/tasks")
    public ResponseEntity<ResponseDto<TaskResponse.DetailTaskResponse>> create(
            @PathVariable Long projectId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody TaskRequest.TaskCreateRequest req
            ){
        ResponseDto<TaskResponse.DetailTaskResponse> response = taskService.create(projectId, userPrincipal, req);
        return ResponseEntity.ok().body(response);
    }



}
