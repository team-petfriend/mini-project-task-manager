package com.example.petfriend.service.impl;

import com.example.petfriend.common.enums.ErrorCode;
import com.example.petfriend.common.errors.BusinessException;
import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.project.request.ProjectRequest;
import com.example.petfriend.dto.project.response.ProjectResponse;
import com.example.petfriend.dto.task.response.TaskResponse;
import com.example.petfriend.entity.Project;
import com.example.petfriend.entity.Task;
import com.example.petfriend.entity.User;
import com.example.petfriend.repository.ProjectRepository;
import com.example.petfriend.repository.TaskRepository;
import com.example.petfriend.repository.UserRepository;
import com.example.petfriend.security.UserPrincipal;
import com.example.petfriend.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public ResponseDto<ProjectResponse.DetailResponse> create(UserPrincipal userPrincipal, ProjectRequest.@Valid Create req) {
        validateName(req.name());
        final String loginId = userPrincipal.getUsername();

        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(()-> new IllegalArgumentException("AUTHOR_NOT_FOUND"));

        Project saved = projectRepository.save(Project.create(req.name(), user));
        ProjectResponse.DetailResponse data = ProjectResponse.DetailResponse.from(saved);

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

        if(projectName == null)throw new BusinessException(ErrorCode.PROJECT_NOT_FOUND, "PROJECT_NAME_REQUIRED");

        List<ProjectResponse.DetailResponse> result = projects.stream()
                .map(ProjectResponse.DetailResponse::from)
                .toList();

        return ResponseDto.setSuccess("성공적으로 조회되었습니다.", result);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseDto<ProjectResponse.DetailResponse> update(UserPrincipal userPrincipal, Long projectId, ProjectRequest.@Valid Update req) {
        validateName(req.name());

        if(projectId == null) throw new BusinessException(ErrorCode.PROJECT_NOT_FOUND,"PROJECT_ID_REQUIRED");

        Project project = projectRepository.findById(projectId)
                .orElseThrow(()-> new IllegalArgumentException("PROJECT_NOT_FOUND"));

        project.update(req.name());
        projectRepository.flush();

        ProjectResponse.DetailResponse data = ProjectResponse.DetailResponse.from(project);
        return ResponseDto.setSuccess("성공적으로 수정되었습니다.", data);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseDto<List<TaskResponse.DetailTaskResponse>> getProjectByIdTasks(Long projectId) {

        if (projectId == null) throw new BusinessException(ErrorCode.PROJECT_NOT_FOUND , "해당 ID가 존재하지않습니다.");

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));

        List<Task> tasks = taskRepository.findAllByProjectId(projectId);

        List<TaskResponse.DetailTaskResponse> data = tasks.stream()
                .map(TaskResponse.DetailTaskResponse::from)
                .toList();

        return ResponseDto.setSuccess("SUCCESS", data);
    }

    @Override
    public ResponseDto<ProjectResponse.DetailResponse> getById(Long projectId) {

        if (projectId == null) throw new BusinessException(ErrorCode.PROJECT_NOT_FOUND , "해당 ID가 존재하지않습니다.");

        Project project = projectRepository.findById(projectId)
                .orElseThrow(()-> new IllegalArgumentException("PROJECT_NOT_FOUND"));

        ProjectResponse.DetailResponse data = ProjectResponse.DetailResponse.from(project);

        return ResponseDto.setSuccess("SUCCESS", data);
    }

    private void validateName(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("NAME_REQUIRED");
        }

    }
}
