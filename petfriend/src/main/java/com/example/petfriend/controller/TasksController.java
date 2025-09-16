package com.example.petfriend.controller;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.task.request.TaskRequest;
import com.example.petfriend.dto.task.response.TaskListResponse;
import com.example.petfriend.dto.task.response.TaskResponse;
import com.example.petfriend.security.UserPrincipal;
import com.example.petfriend.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects/{projectId}")
@RequiredArgsConstructor
public class TasksController {
private final TaskService taskService;
    private final ResourcePatternResolver resourcePatternResolver;

    @PostMapping("/{projectId}/tasks")
    public ResponseEntity<ResponseDto<TaskResponse.DetailTaskResponse>> create(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody TaskRequest.TaskCreateRequest req
            ){
        ResponseDto<TaskResponse.DetailTaskResponse> response = taskService.create(userPrincipal, req);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<ResponseDto<List<TaskResponse.TaskListResponse>>> getAll(){
        ResponseDto<List<TaskResponse.TaskListResponse>> response = taskService.getAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{projectId}/tasks/{taskId}")
    public ResponseEntity<ResponseDto<TaskResponse.DetailTaskResponse>> getById(
            @PathVariable Long projectId,
            @PathVariable Long taskId
    ){
        ResponseDto<TaskResponse.DetailTaskResponse> response = taskService.getBuId(projectId, taskId);
        return ResponseEntity.ok(response);
    }



    @PutMapping("/{projectId}/tasks/{taskId}")
    public ResponseEntity<ResponseDto<TaskResponse.DetailTaskResponse>> update(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @Valid @RequestBody TaskRequest.TaskUpdateRequest req
            ) {
        ResponseDto<TaskResponse.DetailTaskResponse> response = taskService.update(projectId, taskId, req);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{projectId}/tasks/{taskId}")
    public ResponseEntity<ResponseDto<Void>>  delete (
            @PathVariable Long projectId,
            @PathVariable Long taskId
    ){
        taskService.delete(projectId, taskId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }


}