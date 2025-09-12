package com.example.petfriend.entity;

import com.example.petfriend.common.enums.TaskPriority;
import com.example.petfriend.common.enums.TaskStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "tasks",
        indexes = {
                @Index(name = "idx_tasks_projects_status", columnList = "project_id, status"),
                @Index(name = "idx_tasks_assignees_due", columnList = "due_date")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false,
    foreignKey = @ForeignKey(name = "fk_task_project"))
    private Project project;

    @Column(nullable = false, length = 200)
    private String title;

    @Lob
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_status", nullable = false, length = 50)
    private TaskStatus status = TaskStatus.TODO;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false, length = 250)
    private TaskPriority priority = TaskPriority.MEDIUM;

    @Column(name = "assignee_id")
    private Long assignee;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, updatable = false)
    private LocalDateTime updatedAt;

    /** TaskAssigness 조인을 위해서 사용
     *  mappedBy = task(owner)가 "주인이 아님을 명시하고, 반대편 필드가 FK를 관리함을 알려준다”
     *  cascade = CascadeType.ALL => PK가 삭제된다면 FK도 같이 삭제
     * */
    @OneToMany(mappedBy = "assignessTask", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TaskAssignees> assignees = new HashSet<>();

    @OneToMany(mappedBy = "TaskTagTask", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TaskTag> taskTags = new HashSet<>();

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comments> comments = new HashSet<>();


    /** 생성자*/
    @Builder
    public Task(
            @NotNull Project project,
            String title,
            String description,
            TaskStatus status,
            TaskPriority priority,
            Set<TaskAssignees> assignees
    ) {
        this.project = project;
        this.title = title;
        this.description = description;
        this.status = (status != null) ? status : TaskStatus.TODO;
        this.priority = (priority != null) ? priority : TaskPriority.HIGH;
        this.assignees = assignees;
    }

    public void changeTask(String title, String description) {
        this.title = title;
        this.description = description;
    }

    /**
     * === ★중요★ ===
     * tasks에서 addAssignees 기능을 부여하는 이유
     * tasks에 담당자를 부여하기 때문에 assignees(담당자)에 해당하는 기능을 Task Entity에 정의해준다.
     * */


    /** 담당자 부여 */
    public void addAssignees(User user, TaskAssignees taskAssignees) {
        boolean exists = assignees.stream()
                .anyMatch(ta -> ta.getAssignessUser().equals(user));
        if (!exists) {
            assignees.add(new TaskAssignees(this, user));
        }
    }

    /** 담당자 삭제 */
    public void deleteAssignees(User user) {
        assignees.removeIf(ta -> ta.getAssignessUser().equals(user));
    }

    void setTask(Project project){
        this.project = project;
    }

    public void addComment(Comments comment) {
        comments.add(comment);
        comment.setTask(this);
    }

    public void removeComment(Comments comment) {
        comments.remove(comment);
        comment.setTask(null);
    }

}
