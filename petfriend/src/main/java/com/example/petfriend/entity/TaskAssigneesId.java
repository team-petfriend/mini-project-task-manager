package com.example.petfriend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskAssigneesId implements Serializable {

    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "user_id ")
    private Long userId;

    public TaskAssigneesId(Long taskId, Long userId ) {
        this.taskId = taskId;
        this.userId = userId;
    }
}