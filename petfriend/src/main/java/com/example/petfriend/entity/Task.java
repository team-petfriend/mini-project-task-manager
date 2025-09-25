package com.example.petfriend.entity;

import com.example.petfriend.common.enums.TaskPriority;
import com.example.petfriend.common.enums.TaskStatus;
import com.example.petfriend.entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "tasks",
        indexes = {
                @Index(name = "idx_tasks_projects_status", columnList = "project_id, task_status"),
                @Index(name = "idx_tasks_assignees_due", columnList = "due_date")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_task_project"))
    private Project project;

    @Column(nullable = false, length = 200)
    private String title;

    @Lob
    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_status", nullable = false, length = 250)
    private TaskStatus taskStatus = TaskStatus.TODO;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false, length = 250)
    private TaskPriority taskPriority = TaskPriority.MEDIUM;

    @Column(name = "due_date")
    private LocalDate dueDate;


    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comments> comments = new HashSet<>();

    @Builder
    public Task(
            @NotNull Project project,
            String title,
            String description,
            TaskStatus taskStatus,
            TaskPriority priority
    ) {
        this.project = project;
        this.title = title;
        this.description = description;
        this.taskStatus = (taskStatus != null) ? taskStatus : TaskStatus.TODO;
        this.taskPriority = (priority != null) ? priority : TaskPriority.MEDIUM;
    }

    public void addComment(Comments comment) {
        comments.add(comment);
        comment.setTask(this);
    }

    public void removeComment(Comments comment) {
        comments.remove(comment);
        comment.setTask(null);
    }

    public void update(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public void changeTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
        if (taskStatus == TaskStatus.DONE) {
            this.dueDate = LocalDate.now();
        }
    }

    public void changeTaskPriority(TaskPriority taskPriority) {
        this.taskPriority = taskPriority;
    }


}
