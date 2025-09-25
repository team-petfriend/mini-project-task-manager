package com.example.petfriend.controller;

import com.example.petfriend.common.contants.ApiMappingPattern;
import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.project.request.ProjectRequest;
import com.example.petfriend.dto.project.response.ProjectResponse;
import com.example.petfriend.dto.tag.request.TagRequest;
import com.example.petfriend.dto.tag.response.TagResponse;
import com.example.petfriend.security.UserPrincipal;
import com.example.petfriend.service.ProjectService;
import com.example.petfriend.service.TagService;
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
    private final TagService tagService;

    // 프로젝트 생성
    //http://localhost:8080/api/v1/project
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
    //http://localhost:8080/api/v1/project
    public ResponseEntity<ResponseDto<List<ProjectResponse.DetailResponse>>> searchAll() {
        ResponseDto<List<ProjectResponse.DetailResponse>> response = projectService.getAllProject();
        return ResponseEntity.ok(response);
    }

    // 프로젝트 검색
    // 정렬은 나중 추가

    @GetMapping(ApiMappingPattern.Project.SEARCH)
    //http://localhost:8080/api/v1/project/search?projectName=1
    public ResponseEntity<ResponseDto<List<ProjectResponse.DetailResponse>>> search(
            @RequestParam("projectName") @NotBlank(message = "검색 키워드는 비워질 수 없습니다.") String projectName
    ) {
        ResponseDto<List<ProjectResponse.DetailResponse>> response = projectService.search(projectName);
        return ResponseEntity.ok(response);
    }


    //프로젝트 이름 수정
    @PutMapping(ApiMappingPattern.Project.ONLY_ID)
    //http://localhost:8080/api/v1/project/:projectId
    public ResponseEntity<ResponseDto<ProjectResponse.DetailResponse>> update(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectRequest.Update req
    ) {
        ResponseDto<ProjectResponse.DetailResponse> response = projectService.update(userPrincipal, projectId, req);
        return ResponseEntity.ok(response);
    }
  

    // 프로젝트 안 태그 생성
    @PostMapping("{projectId}")
    public ResponseEntity<ResponseDto<TagResponse.DetailTag>> createTag(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("projectId") Long projectId,
            @Valid @RequestBody TagRequest.createTag req
    ) {
        ResponseDto<TagResponse.DetailTag> response = tagService.createTag(userPrincipal, projectId,  req);
        return ResponseEntity.ok().body(response);
    }
}
