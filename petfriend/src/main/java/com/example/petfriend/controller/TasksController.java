package com.example.petfriend.controller;

import com.example.petfriend.common.contants.ApiMappingPattern;
import com.example.petfriend.common.enums.TaskPriority;
import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.task.request.TaskRequest;
import com.example.petfriend.dto.task.response.TaskResponse;
import com.example.petfriend.security.UserPrincipal;
import com.example.petfriend.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.Tasks.ROOT)
@RequiredArgsConstructor
public class TasksController {
    private final TaskService taskService;


    @PostMapping
    public ResponseEntity<ResponseDto<TaskResponse.DetailTaskResponse>> create(
            @PathVariable Long projectId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody TaskRequest.TaskCreateRequest req
    ) {
        ResponseDto<TaskResponse.DetailTaskResponse> response = taskService.create(projectId, userPrincipal, req);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<TaskResponse.TaskListResponse>>> getAll(
            @PathVariable Long projectId
    ) {
        ResponseDto<List<TaskResponse.TaskListResponse>> response = taskService.getAll(projectId);
        return ResponseEntity.ok(response);
    }

    // 정렬 및 필터링 추가 예정
    @GetMapping(ApiMappingPattern.Tasks.ID_ONLY)
    public ResponseEntity<ResponseDto<TaskResponse.DetailTaskResponse>> getById(
            @PathVariable Long projectId,
            @PathVariable Long taskId
    ) {
        ResponseDto<TaskResponse.DetailTaskResponse> response = taskService.getById(projectId, taskId);
        return ResponseEntity.ok(response);
    }


    @PutMapping(ApiMappingPattern.Tasks.ID_ONLY)
    public ResponseEntity<ResponseDto<TaskResponse.DetailTaskResponse>> update(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @Valid @RequestBody TaskRequest.TaskUpdateRequest req
    ) {
        ResponseDto<TaskResponse.DetailTaskResponse> response = taskService.update(projectId, taskId, req);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(ApiMappingPattern.Tasks.ID_ONLY)
    public ResponseEntity<ResponseDto<TaskResponse.DetailTaskResponse>> statusUpdate(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long taskId
    ){
        ResponseDto<TaskResponse.DetailTaskResponse> response = taskService.statusUpdate(userPrincipal, taskId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(ApiMappingPattern.Tasks.ID_ONLY)
    public ResponseEntity<ResponseDto<TaskResponse.DetailTaskResponse>> priorityUpdate(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long taskId
    ){
        ResponseDto<TaskResponse.DetailTaskResponse> response = taskService.priorityUpdate(userPrincipal, taskId);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }


    @DeleteMapping(ApiMappingPattern.Tasks.ID_ONLY)
    public ResponseEntity<ResponseDto<Void>> delete(
            @PathVariable Long projectId,
            @PathVariable Long taskId
    ) {
        taskService.delete(projectId, taskId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}