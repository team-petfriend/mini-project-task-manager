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

    /**
     * === MapsId ===
     * PK를 FK와 연결할 때 사용 주로 중간 테이블에서 사용한다.
     * @MapsId("taskId")를 통해서 FK임을 명시해준다.
     * */
    
    /** tasks 테이블을 조인 */
    @MapsId("taskId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_Id", nullable = false, foreignKey = @ForeignKey(name = "fk_task_assignees_task")
	)
    private Task assignessTask;

    /** user 테이블을 조인 */
    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_Id", nullable = false, foreignKey = @ForeignKey(name = "fk_task_assignees_user")
	)
    private User assignessUser;

    /** tasks, user의 값을 가져와서 TaskAssignees 생성자를 생성해준다.
     * task, user의 값을 모두 사용 가능.
     * */
    public TaskAssignees(Task task, User user) {
        this.assignessTask = assignessTask;
        this.assignessUser = assignessUser;
        Long userId = user.getId();
        Long taskId = task.getId();
        this.id = new TaskAssigneesId(taskId, userId);
    }
}
