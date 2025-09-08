package com.example.petfriend.service.impl;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.project.request.ProjectRequest;
import com.example.petfriend.dto.project.response.ProjectResponse;
import com.example.petfriend.repository.ProjectRepository;
import com.example.petfriend.security.UserPrincipal;
import com.example.petfriend.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;

    @Override
    public ResponseDto<ProjectResponse.DetailResponse> create(UserPrincipal userPrincipal, ProjectRequest.@Valid Create req) {
        return null;
    }

    @Override
    public ResponseDto<List<ProjectResponse.DetailResponse>> getAllProject() {
        return null;
    }
    @Override
    public ResponseDto<List<ProjectResponse.DetailResponse>> search(UserPrincipal userPrincipal, String projectName, LocalDateTime from, LocalDateTime to) {
        return null;
    }

    @Override
    public ResponseDto<ProjectResponse.DetailResponse> get(Long projectId) {
        return null;
    }


}
