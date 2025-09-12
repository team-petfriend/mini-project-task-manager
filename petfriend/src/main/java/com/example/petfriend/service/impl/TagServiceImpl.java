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
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public ResponseDto<TagResponse.DetailTag> createTag(UserPrincipal userPrincipal, TagRequest.@Valid createTag req) {


        Project project = projectRepository.findById(req.projectId())
                .orElseThrow(() -> new RuntimeException("프로젝트에 해당하는 ID가 존재하지 않습니다. id = " + req.projectId()));

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
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') or @tagAuthz.isAuthor(#tagId, authentication)" )
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
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') or @tagAuthz.isAuthor(#tagId, authentication)")
    public ResponseDto<TagResponse.DetailTag> updateTag(UserPrincipal userPrincipal, TagRequest.@Valid updateTag req, Long tagId) {

        if (tagId == null) throw new IllegalArgumentException("값을 입력해주세요");

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("해당 태그의 ID가 존재하지 않습니다. ID = " + tagId));

        tag.updateTags(
                req.name(),
                req.color()
        );

        tagRepository.flush();

        TagResponse.DetailTag data = TagResponse.DetailTag.from(tag);
        return ResponseDto.setSuccess("SUCCESS", data);
    }
}

