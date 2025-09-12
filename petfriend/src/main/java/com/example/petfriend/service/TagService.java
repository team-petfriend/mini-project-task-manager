package com.example.petfriend.service;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.tag.request.TagRequest;
import com.example.petfriend.dto.tag.response.TagResponse;
import com.example.petfriend.security.UserPrincipal;
import jakarta.validation.Valid;

public interface TagService {
    ResponseDto<TagResponse.DetailTag> createTag(UserPrincipal userPrincipal, TagRequest.@Valid createTag req);

    ResponseDto<Void> delete(UserPrincipal userPrincipal, Long id);

    ResponseDto<TagResponse.DetailTag> updateTag(UserPrincipal userPrincipal, TagRequest.@Valid updateTag req, Long id);
}
