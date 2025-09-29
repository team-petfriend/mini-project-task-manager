package com.example.petfriend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "task_assignees")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskAssignees {

    @EmbeddedId
    private TaskAssigneesId id;

    @MapsId("taskId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_Id", nullable = false, foreignKey = @ForeignKey(name = "fk_task_assignees_task")
	)
    private Task assignessTask;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_Id", nullable = false, foreignKey = @ForeignKey(name = "fk_task_assignees_user")
	)
    private User assignessUser;

    public TaskAssignees(Task task, User user) {
        this.assignessTask = assignessTask;
        this.assignessUser = assignessUser;
        Long userId = user.getId();
        Long taskId = task.getId();
        this.id = new TaskAssigneesId(taskId, userId);
    }
}
