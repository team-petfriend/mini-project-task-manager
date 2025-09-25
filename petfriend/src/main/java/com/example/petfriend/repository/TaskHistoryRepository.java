package com.example.petfriend.repository;

import com.example.petfriend.entity.Task;
import com.example.petfriend.entity.TaskHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskHistoryRepository extends JpaRepository<TaskHistory, Task> {
}
