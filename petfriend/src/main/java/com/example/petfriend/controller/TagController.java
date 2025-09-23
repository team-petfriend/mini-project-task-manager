package com.example.petfriend.controller;

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

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    /**
     * 태그 생성
     */
    
    @PostMapping
    public ResponseEntity<ResponseDto<TagResponse.DetailTag>> createTag(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody TagRequest.createTag req
    ) {
        ResponseDto<TagResponse.DetailTag> response = tagService.createTag(userPrincipal, req);
        return ResponseEntity.ok().body(response);
    }

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
}