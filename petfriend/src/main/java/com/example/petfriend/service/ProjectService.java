package com.example.petfriend.service;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.project.request.ProjectRequest;
import com.example.petfriend.dto.project.response.ProjectResponse;
import com.example.petfriend.dto.task.response.TaskResponse;
import com.example.petfriend.security.UserPrincipal;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public interface ProjectService {
    ResponseDto<ProjectResponse.DetailResponse> create(UserPrincipal userPrincipal, ProjectRequest.@Valid Create req);
    ResponseDto<List<ProjectResponse.DetailResponse>> getAllProject();
    ResponseDto<List<ProjectResponse.DetailResponse>> search(@NotBlank(message = "검색 키워드는 비워질 수 없습니다.") String projectName);
    ResponseDto<ProjectResponse.DetailResponse> update(UserPrincipal userPrincipal, Long projectId, ProjectRequest.@Valid Update req);
    ResponseDto<List<TaskResponse.DetailTaskResponse>> getProjectByIdTasks(Long projectId);
    ResponseDto<ProjectResponse.DetailResponse> getById(Long projectId);
}
