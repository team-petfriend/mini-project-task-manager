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
public class UserTaskId implements Serializable {
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "task_id", length = 30, nullable = false)
    private Long taskId;

    public UserTaskId(Long userId, Long taskId ) {
        this.userId = userId;
        this.taskId = taskId;
    }





}
