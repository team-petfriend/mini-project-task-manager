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
                @Index(name = "idx_tasks_projects_status", columnList = "project_id, task_status"),
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
    @Column(name = "task_status", nullable = false, length = 250)
    private TaskStatus taskStatus = TaskStatus.TODO;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false, length = 250)
    private TaskPriority taskPriority = TaskPriority.MEDIUM;

    @Column(name = "assignee_id")
    private Long assignee;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6)")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false,  columnDefinition = "DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)")
    private LocalDateTime updatedAt;

    @ManyToMany
    @JoinTable(
            name = "task_assigness",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> assignees;

    @ManyToMany
    @JoinTable(
            name = "task_tag",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comments> comments = new HashSet<>();

    /** 생성자*/
    @Builder
    public Task(
            @NotNull Project project,
            String title,
            String description,
            TaskStatus taskStatus,
            TaskPriority priority,
            Set<TaskAssignees> assignees
    ) {
        this.project = project;
        this.title = title;
        this.description = description;
        this.taskStatus = taskStatus;
        this.taskPriority = priority;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addComment(Comments comment) {
        comments.add(comment);
        comment.setTask(this);
    }

    public void removeComment(Comments comment) {
        comments.remove(comment);
        comment.setTask(null);
    }

    public enum TaskStatus {
        TODO, IN_PROGRESS, DONE
    }

    public enum TaskPriority {
        LOW, MEDIUM, HIGH
    }



}
