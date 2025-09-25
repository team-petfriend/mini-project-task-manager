package com.example.petfriend.repository;

import com.example.petfriend.entity.TaskTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagTaskRepository extends JpaRepository<TaskTag, Long> {
}
