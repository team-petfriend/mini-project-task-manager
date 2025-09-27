package com.example.petfriend.service.impl;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.tagTask.response.TagTaskResponse;
import com.example.petfriend.entity.Tag;
import com.example.petfriend.entity.Task;
import com.example.petfriend.entity.TaskTag;
import com.example.petfriend.repository.TagRepository;
import com.example.petfriend.repository.TagTaskRepository;
import com.example.petfriend.repository.TaskRepository;
import com.example.petfriend.service.TagTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagTaskServiceImpl implements TagTaskService {

    private final TagTaskRepository tagTaskRepository;
    private final TagRepository tagRepository;
    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public ResponseDto<TagTaskResponse.DetailTag> create(Long tagId, Long taskId) {

        boolean exists = tagTaskRepository.existsByTaskIdAndTagId(tagId, taskId);
        if (exists) {
            throw new IllegalArgumentException("이미 연결된 태그입니다.");
        }

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new IllegalArgumentException("해당 태그의 ID가 존재하지 않습니다."));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("해당 task의 ID가 존재하지않습니다."));

        if (!tag.getProject().getId().equals(task.getProject().getId())) {
            throw new IllegalArgumentException("같은 project안에 존재하는 tag와 task만 연결이 가능합니다.");
        }

        TaskTag taskTag = TaskTag.builder()
                .tag(tag)
                .task(task)
                .build();

        TaskTag saved = tagTaskRepository.save(taskTag);
        TagTaskResponse.DetailTag data = TagTaskResponse.DetailTag.from(saved);

        return ResponseDto.setSuccess("SUCCESS", data);
    }
}
