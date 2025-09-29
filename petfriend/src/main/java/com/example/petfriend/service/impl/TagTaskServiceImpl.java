package com.example.petfriend.service.impl;

import com.example.petfriend.dto.ResponseDto;
import com.example.petfriend.dto.tag.response.TagResponse;
import com.example.petfriend.dto.tagTask.response.TagTaskResponse;
import com.example.petfriend.dto.task.response.TaskResponse;
import com.example.petfriend.entity.Project;
import com.example.petfriend.entity.Tag;
import com.example.petfriend.entity.Task;
import com.example.petfriend.entity.TaskTag;
import com.example.petfriend.repository.ProjectRepository;
import com.example.petfriend.repository.TagRepository;
import com.example.petfriend.repository.TagTaskRepository;
import com.example.petfriend.repository.TaskRepository;
import com.example.petfriend.service.TagTaskService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagTaskServiceImpl implements TagTaskService {
    private final TagTaskRepository tagTaskRepository;
    private final TagRepository tagRepository;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

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

    @Override
    public ResponseDto<List<TagTaskResponse.TagToTaskProject>> getByIdProjectTagTask(Long projectId) {
        List<TagTaskResponse.DetailTag> data = null;

        if (projectId == null) throw new IllegalArgumentException("해당 projectId가 존재하지않습니다.");

        List<TaskTag> tagTasks = tagTaskRepository.getByIdProjectTagTask(projectId);

        if (tagTasks.isEmpty()) {
            throw new IllegalArgumentException("해당하는 projectId에 task가 존재하지않습니다.");
        }

        // 태그별로 Task를 그룹핑
        Map<Long, List<TaskResponse.DetailTaskResponse>> tagIdToTasksMap = tagTasks.stream()
                .collect(Collectors.groupingBy(
                        tt -> tt.getTag().getId(),
                        Collectors.mapping(tt -> TaskResponse.DetailTaskResponse.from(tt.getTask()),
                                Collectors.toList())
                ));

        // 태그별 DetailTag DTO 생성
        List<TagTaskResponse.TagToTaskProject> detailTags = tagTasks.stream()
                .map(TaskTag::getTag)                     // 태그 단위로
                .distinct()                               // 중복 제거
                .map(tag -> {
                    List<TaskResponse.DetailTaskResponse> tasks = tagIdToTasksMap.getOrDefault(tag.getId(), List.of());
                    return new TagTaskResponse.TagToTaskProject(
                            tag.getProject().getId(),
                            List.of(TagResponse.DetailTag.from(tag)),
                            tasks
                    );
                })
                .toList();


        return ResponseDto.setSuccess("SUCCESS", detailTags);
    }
}
