package com.example.petfriend.repository;

import com.example.petfriend.entity.Comments;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comments, Long> {
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
