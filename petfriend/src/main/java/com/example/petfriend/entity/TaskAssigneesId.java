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

    /** TaskAssigneesId를 만드는 이유
     *  Id의 값을 명시하지 않았기 때문에 TaskAssigneesId라는 클래스를 이용해서 Id의 값을 생성해준다.
     * */
    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "user_id ")
    private Long userId;

    public TaskAssigneesId(Long taskId, Long userId ) {
        this.taskId = taskId;
        this.userId = userId;
    }
}