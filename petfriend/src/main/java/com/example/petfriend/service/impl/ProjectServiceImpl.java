package com.example.petfriend.service.impl;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.project.request.ProjectRequest;
import com.example.petfriend.dto.project.response.ProjectResponse;
import com.example.petfriend.entity.Project;
import com.example.petfriend.entity.User;
import com.example.petfriend.repository.ProjectRepository;
import com.example.petfriend.security.UserPrincipal;
import com.example.petfriend.service.ProjectService;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final EntityManager em;

    @Override
    @Transactional
    @PreAuthorize("hasRole('OWNER')")
    public ResponseDto<ProjectResponse.DetailResponse> create(UserPrincipal userPrincipal, ProjectRequest.@Valid Create req) {
        ProjectResponse.DetailResponse data = null;

        Long authUserId = userPrincipal.getId();
        User userRef = em.getReference(User.class, authUserId);

        Project project = Project.builder()
                .user(userRef)
                .name(req.name())
                .build();

        Project saved = projectRepository.save(project);
        data = new ProjectResponse.DetailResponse(saved.getId(), saved.getName());

        return ResponseDto.setSuccess("프로젝트가 성공적으로 생성되었습니다.", data);
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
