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
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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


        Project project = Project.builder()

                .name(req.name())
                .build();

        Project saved = projectRepository.save(project);
        data =  ProjectResponse.DetailResponse.from(saved);

        return ResponseDto.setSuccess("프로젝트가 성공적으로 생성되었습니다.", data);
    }

    @Override
    public ResponseDto<List<ProjectResponse.DetailResponse>> getAllProject() {
        List<Project> projects = projectRepository.findAll();
        List<ProjectResponse.DetailResponse> response = projects.stream()
                .map(ProjectResponse.DetailResponse::from)
                .toList();
        return ResponseDto.setSuccess("프로젝트 전체 목록을 가져왔습니다.", response);
    }

    @Override
    public ResponseDto<List<ProjectResponse.DetailResponse>> search(String projectName) {
        List<Project> projects = projectRepository.findByNameContainingIgnoreCase(projectName);
        List<ProjectResponse.DetailResponse> response = projects.stream()
                .map(ProjectResponse.DetailResponse::from)
                .toList();
        return ResponseDto.setSuccess("성공적으로 조회하였습니다.", response);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('OWNER','ADMIN','MANAGER')")
    public ResponseDto<ProjectResponse.DetailResponse> update(UserPrincipal userPrincipal, Long projectId,ProjectRequest.@Valid Update req ) {
        ProjectResponse.DetailResponse data = null;
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("프로젝트를 찾을 수 없습니다"));
        if(req.name() == null){
            throw new IllegalArgumentException("수정할 데이터 없음");
        }
        boolean nameChanged = req.name() != null && !Objects.equals(project.getName(), req.name());
        if(!nameChanged) throw new IllegalArgumentException("변경된 데이터가 없습니다.");
        if(req.name() != null) project.setName(req.name());
        data = new ProjectResponse.DetailResponse(
                project.getId(),
                project.getName(),
                project.getCreatedAt(),
                project.getUpdatedAt()
        );

        return ResponseDto.setSuccess("수정이 완료되었습니다.", data);
    }



}
