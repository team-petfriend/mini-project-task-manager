package com.example.petfriend.service;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.project.request.ProjectRequest;
import com.example.petfriend.dto.project.response.ProjectResponse;
import com.example.petfriend.security.UserPrincipal;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;

public interface ProjectService {
    ResponseDto<ProjectResponse.DetailResponse> create(UserPrincipal userPrincipal, ProjectRequest.@Valid Create req);

    ResponseDto<List<ProjectResponse.DetailResponse>> getAllProject();

    ResponseDto<ProjectResponse.DetailResponse> get(Long projectId);

    ResponseDto<List<ProjectResponse.DetailResponse>> search(UserPrincipal userPrincipal, String projectName, LocalDateTime from, LocalDateTime to);
}
