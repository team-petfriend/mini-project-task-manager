package com.example.petfriend.repository;

import com.example.petfriend.entity.Comments;
import com.example.petfriend.entity.Task;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comments, Long> {
    Optional<Comments> findByIdAndTask(Long commentId, Task task);

    @Query("""
            SELECT c FROM Comments c
            WHERE c.task.id = :taskId
            AND (:commenterId IS NULL OR c.commenter.id = :commenterId)
            """)
    List<Comments> findByTaskIdAndOptionalCommenterId(
            @Param("taskId") Long taskId,
            @Param("commenterId") Long commenterId,
            Sort sort
    );
    
}
