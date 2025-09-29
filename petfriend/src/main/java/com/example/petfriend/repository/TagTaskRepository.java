package com.example.petfriend.repository;

import com.example.petfriend.entity.TaskTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagTaskRepository extends JpaRepository<TaskTag, Long> {
    boolean existsByTaskIdAndTagId(Long tagId, Long taskId);

    @Query("""
        SELECT tt
        FROM TaskTag tt
        JOIN FETCH tt.tag tg
        JOIN FETCH tt.task ta
        WHERE tg.project.id = :projectId
    """)
    List<TaskTag> getByIdProjectTagTask(@Param("projectId") Long projectId);
}
