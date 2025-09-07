package com.example.petfriend.controller;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.project.request.ProjectRequest;
import com.example.petfriend.dto.project.response.ProjectResponse;
import com.example.petfriend.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    // 프로젝트 생성
    @PostMapping
    public ResponseEntity<ResponseDto<ProjectResponse.DetailResponse>> create(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody ProjectRequest.Create req
            ){
        ResponseDto<ProjectResponse.DetailResponse> response = projectService.create(userPrincipal, req);
        return ResponseEntity.ok(response);
    }

    // 프로젝트 전체조회
    @GetMapping("/api/v1/projects")
    public ResponseEntity<ResponseDto<List<ProjectResponse.DetailResponse>>> searchAll(){
        ResponseDto<List<ProjectResponse.DetailResponse>> response = projectService.getAllProject();
        return ResponseEntity.ok(response);
    }

    // 프로젝트 정렬 검색
    @GetMapping
    public ResponseEntity<ResponseDto<List<ProjectResponse.DetailResponse>>> search(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(required = false) String projectName,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime to,
            ){
        ResponseDto<List<ProjectResponse.DetailResponse>> response = projectService.search(userPrincipal, projectName,from,to);
        return ResponseEntity.ok(response);
    }


    //프로젝트 이름 수정
    @PutMapping("/{projectId}")
    public ResponseEntity<ResponseDto<ProjectResponse.DetailResponse>> update(
            @PathVariable Long projectId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody ProjectRequest.Update req
    ){
        ResponseDto<ProjectResponse.DetailResponse> response = projectService.get(projectId);
        return ResponseEntity.ok(response);
    }


}
