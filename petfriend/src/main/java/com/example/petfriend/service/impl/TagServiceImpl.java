package com.example.petfriend.service.impl;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.tag.request.TagRequest;
import com.example.petfriend.dto.tag.response.TagResponse;
import com.example.petfriend.entity.Project;
import com.example.petfriend.entity.Tag;
import com.example.petfriend.repository.ProjectRepository;
import com.example.petfriend.repository.TagRepository;
import com.example.petfriend.security.UserPrincipal;
import com.example.petfriend.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final ProjectRepository projectRepository;

    /** 생성 권한 : ADMIN, MANAGER */
    @Override
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Transactional
    public ResponseDto<TagResponse.DetailTag> createTag(UserPrincipal userPrincipal, Long projectId, TagRequest.@Valid createTag req) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("프로젝트에 해당하는 ID가 존재하지 않습니다. id = " + projectId));

        boolean exists = tagRepository.existsByProjectIdAndName(projectId, req.name());
        if (exists) {
            throw new IllegalArgumentException("동일한 태그가 존재합니다.");
        }

        Tag tag = Tag.builder()
                .name(req.name())
                .color(req.color())
                .project(project)
                .build();

        Tag saved = tagRepository.save(tag);

        TagResponse.DetailTag data = TagResponse.DetailTag.from(saved);

        return ResponseDto.setSuccess("SUCCESS", data);
    }

    /** 삭제 권한 : ADMIN, MANAGER, 태그를 생성한 사용자*/
    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')" )
    @Transactional
    public ResponseDto<Void> delete(UserPrincipal userPrincipal, Long tagId) {

        if (tagId == null) throw new IllegalArgumentException("값을 입력해주세요.");

        Tag tag = tagRepository.findById(tagId)
            .orElseThrow(() -> new RuntimeException("해당 태그의 ID가 존재하지 않습니다. ID = " + tagId));

        tagRepository.delete(tag);

        return ResponseDto.setSuccess("SUCCESS", null);
        }

    /** 수정 권한 : ADMIN, MANAGER, 태그를 생성한 사용자*/
    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Transactional
    public ResponseDto<TagResponse.DetailTag> updateTag(UserPrincipal userPrincipal, TagRequest.@Valid updateTag req, Long tagId) {

        if (tagId == null) throw new IllegalArgumentException("값을 입력해주세요");

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("해당 태그의 ID가 존재하지 않습니다. ID = " + tagId));

        if (tag.getName().equals(req.name())) {
            throw new IllegalArgumentException("해당 태그가 이미 존재합니다.");
        }

        tag.updateTags(
                req.name(),
                req.color()
        );

        tagRepository.flush();

        TagResponse.DetailTag data = TagResponse.DetailTag.from(tag);
        return ResponseDto.setSuccess("SUCCESS", data);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseDto<List<TagResponse.DetailTag>> getByIdTag(Long projectId) {
        if (projectId == null) throw new IllegalArgumentException("해당 값을 입력해주세요");

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("해당 id가 존재하지않습니다."));

        List<Tag> tags = tagRepository.findAllByProjectId(projectId);


        List<TagResponse.DetailTag> data = tags.stream()
                .map(TagResponse.DetailTag::from)
                .toList();

        return ResponseDto.setSuccess("SUCCESS", data);
    }
}

