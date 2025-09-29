package com.example.petfriend.controller;

import com.example.petfriend.common.contants.ApiMappingPattern;
import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.tag.request.TagRequest;
import com.example.petfriend.dto.tag.response.TagResponse;
import com.example.petfriend.entity.User;
import com.example.petfriend.repository.TagRepository;
import com.example.petfriend.security.UserPrincipal;
import com.example.petfriend.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<TagResponse.DetailTag>> updateTag(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody TagRequest.updateTag req,
            @PathVariable Long id
    ) {
        ResponseDto<TagResponse.DetailTag> response = tagService.updateTag(userPrincipal, req, id); {
            return ResponseEntity.ok().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Void>> deleteTag(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long id
            ) {
        ResponseDto<Void> response = tagService.delete(userPrincipal, id);
        return ResponseEntity.ok().body(response);
    }

    // 프로젝트 안 태그 생성
    @PostMapping("/{projectId}/tags")
    public ResponseEntity<ResponseDto<TagResponse.DetailTag>> createTag(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("projectId") Long projectId,
            @Valid @RequestBody TagRequest.createTag req
    ) {
        ResponseDto<TagResponse.DetailTag> response = tagService.createTag(userPrincipal, projectId,  req);
        return ResponseEntity.ok().body(response);
    }


    // 프로젝트 안 태그 목록 보기
    @GetMapping("/{projectId}/tags")
    public ResponseEntity<ResponseDto<List<TagResponse.DetailTag>>> getByIdTag (
            @PathVariable("projectId") Long projectId
    ){
        ResponseDto<List<TagResponse.DetailTag>> response = tagService.getByIdTag(projectId);
        return ResponseEntity.ok().body(response);
    }
}