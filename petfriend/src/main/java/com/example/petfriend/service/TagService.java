package com.example.petfriend.service;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.tag.request.TagRequest;
import com.example.petfriend.dto.tag.response.TagResponse;
import com.example.petfriend.security.UserPrincipal;
import jakarta.validation.Valid;

import java.util.List;

public interface TagService {
    ResponseDto<TagResponse.DetailTag> createTag(UserPrincipal userPrincipal, Long projectId, TagRequest.@Valid createTag req);
    ResponseDto<Void> delete(UserPrincipal userPrincipal, Long id);
    ResponseDto<TagResponse.DetailTag> updateTag(UserPrincipal userPrincipal, TagRequest.@Valid updateTag req, Long id);
    ResponseDto<List<TagResponse.DetailTag>> getByIdTag(Long projectId);
}
