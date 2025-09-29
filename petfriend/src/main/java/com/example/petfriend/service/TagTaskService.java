package com.example.petfriend.service;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.tagTask.response.TagTaskResponse;

import java.util.List;

public interface TagTaskService {
    ResponseDto<TagTaskResponse.DetailTag> create(Long tagId, Long taskId);

    ResponseDto<List<TagTaskResponse.TagToTaskProject>> getByIdProjectTagTask(Long projectId);
}
