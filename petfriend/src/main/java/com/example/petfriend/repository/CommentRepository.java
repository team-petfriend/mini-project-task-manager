package com.example.petfriend.repository;

import com.example.petfriend.entity.Comments;
import com.example.petfriend.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comments, Long> {
    Optional<Comments> findByIdAndTask(Long commentId, Task task);
    
}
