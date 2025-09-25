package com.example.petfriend.service;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.tagTask.response.TagTaskResponse;

public interface TagTaskService {
    ResponseDto<TagTaskResponse.DetailTag> create(Long tagId, Long taskId);
}
