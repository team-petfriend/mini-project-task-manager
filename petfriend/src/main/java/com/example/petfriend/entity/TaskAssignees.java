package com.example.petfriend.entity;

import com.example.petfriend.common.enums.TaskStatus;
import com.example.petfriend.security.UserPrincipal;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "task_assignees")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskAssignees   {

    @EmbeddedId
    private UserTaskId id;


    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_task_assignees_user")
    )
    private User user;

    @MapsId("taskId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "task_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_task_assignees_task")
    )
    private Task task;

    public TaskAssignees(Task task, User user) {
        this.task = task;
        this.user = user;
        Long userId = user.getId();
        Long taskId = task.getId();
        this.id = new UserTaskId(taskId, userId);


    }


}
