package com.example.petfriend.repository;

import com.example.petfriend.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByProjectId(Long projectId);

//    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.taskTags tt LEFT JOIN FETCH tt.tag WHERE t.id = :taskId")
//    Optional<Task> findByIdWithTags(Long id);
}
