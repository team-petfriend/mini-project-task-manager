package com.example.petfriend.controller;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.tagTask.response.TagTaskResponse;
import com.example.petfriend.service.TagService;
import com.example.petfriend.service.TagTaskService;
import com.example.petfriend.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tagTask")
@RequiredArgsConstructor
public class TagTaskController {

    private final TagTaskService tagTaskService;

    @PostMapping("/tag/{tagId}/task/{taskId}")
    public ResponseEntity<ResponseDto<TagTaskResponse.DetailTag>> createTagTask(
            @PathVariable Long tagId,
            @PathVariable Long taskId
    ) {
        ResponseDto<TagTaskResponse.DetailTag> response = tagTaskService.create(tagId,taskId);

        return ResponseEntity.ok().body(response);

    }
}
