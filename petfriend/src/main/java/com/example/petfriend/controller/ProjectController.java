package com.example.petfriend.controller;

import com.example.petfriend.common.contants.ApiMappingPattern;
import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.project.request.ProjectRequest;
import com.example.petfriend.dto.project.response.ProjectResponse;
import com.example.petfriend.security.UserPrincipal;
import com.example.petfriend.service.ProjectService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.Project.ROOT)
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    // 프로젝트 생성
    @PostMapping
    public ResponseEntity<ResponseDto<ProjectResponse.DetailResponse>> create(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody ProjectRequest.Create req
    ) {
        ResponseDto<ProjectResponse.DetailResponse> response = projectService.create(userPrincipal, req);
        return ResponseEntity.ok(response);
    }

    // 프로젝트 전체조회
    @GetMapping
    public ResponseEntity<ResponseDto<List<ProjectResponse.DetailResponse>>> searchAll() {
        ResponseDto<List<ProjectResponse.DetailResponse>> response = projectService.getAllProject();
        return ResponseEntity.ok(response);
    }

    // 프로젝트 검색
    // 정렬은 나중 추가
    @GetMapping(ApiMappingPattern.Project.SEARCH)
    public ResponseEntity<ResponseDto<List<ProjectResponse.DetailResponse>>> search(
            @RequestParam("projectName") @NotBlank(message = "검색 키워드는 비워질 수 없습니다.") String projectName
    ) {
        ResponseDto<List<ProjectResponse.DetailResponse>> response = projectService.search(projectName);
        return ResponseEntity.ok(response);
    }


    //프로젝트 이름 수정
    @PutMapping(ApiMappingPattern.Project.ONLY_ID)
    public ResponseEntity<ResponseDto<ProjectResponse.DetailResponse>> update(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectRequest.Update req
    ) {
        ResponseDto<ProjectResponse.DetailResponse> response = projectService.update(userPrincipal, projectId, req);
        return ResponseEntity.ok(response);
    }

}
