package com.example.petfriend.controller;

import com.example.petfriend.common.contants.ApiMappingPattern;
import com.example.petfriend.common.enums.TaskPriority;
import com.example.petfriend.common.enums.TaskStatus;
import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.project.response.ProjectResponse;
import com.example.petfriend.dto.task.request.TaskRequest;
import com.example.petfriend.dto.task.response.TaskResponse;
import com.example.petfriend.security.UserPrincipal;
import com.example.petfriend.service.ProjectService;
import com.example.petfriend.service.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class TasksController {
    private final TaskService taskService;
    private final ProjectService projectService;


    // ======================= CRUD ==================================
    
    // 생성
    @PostMapping("/{projectId}/tasks")
    //http://localhost:8080/api/v1/projects/:projectId/tasks
    public ResponseEntity<ResponseDto<TaskResponse.DetailTaskResponse>> create(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("projectId") @Positive(message = "projectId는 1 이상의 정수여야 합니다.") Long projectId,
            @Valid @RequestBody TaskRequest.TaskCreateRequest req
    ) {
        ResponseDto<TaskResponse.DetailTaskResponse> response = taskService.create(projectId, userPrincipal, req);
        return ResponseEntity.ok().body(response);
    }


    // 수정
    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<ResponseDto<TaskResponse.UpdateTaskResponse>> update(
            @PathVariable Long taskId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody TaskRequest.TaskUpdateRequest req
    ) {
        ResponseDto<TaskResponse.UpdateTaskResponse> response = taskService.update(userPrincipal, taskId, req);
        return ResponseEntity.ok().body(response);
    }


    // 삭제
    @DeleteMapping(ApiMappingPattern.Tasks.ID_ONLY)
    public ResponseEntity<ResponseDto<Void>> delete(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long taskId
    ) {
        ResponseDto<Void> response =  taskService.delete(userPrincipal, taskId);

        return ResponseEntity.ok().body(response);
    }


    // ======================= 상태, 담당자 변경 ==================================
    
    
    @PatchMapping("/tasks/{taskId}/status-update")
    public ResponseEntity<ResponseDto<TaskResponse.ChangedTaskStatusResponse>> statusUpdate(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam TaskStatus taskStatus,
            @PathVariable Long taskId
    ){
        ResponseDto<TaskResponse.ChangedTaskStatusResponse> response = taskService.statusUpdate(userPrincipal, taskStatus, taskId);
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/tasks/{taskId}/priority-update")
    public ResponseEntity<ResponseDto<TaskResponse.ChangedTaskPriorityResponse>> priorityUpdate(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam TaskPriority taskPriority,
            @PathVariable Long taskId
    ){
        ResponseDto<TaskResponse.ChangedTaskPriorityResponse> response = taskService.priorityUpdate(userPrincipal, taskPriority, taskId);
        return ResponseEntity.ok().body(response);

    }

    @GetMapping("{projectId}")
    public ResponseEntity<ResponseDto<List<TaskResponse.DetailTaskResponse>>> getTasks(
            @PathVariable Long projectId
    ) {
        ResponseDto<List<TaskResponse.DetailTaskResponse>> response = projectService.getProjectByIdTasks(projectId);
        return ResponseEntity.ok().body(response);
    }

}