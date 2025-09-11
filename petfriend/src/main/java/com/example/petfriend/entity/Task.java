package com.example.petfriend.entity;

import ch.qos.logback.core.status.Status;
import com.example.petfriend.common.enums.TaskPriority;
import com.example.petfriend.common.enums.TaskStatus;
import jakarta.annotation.Priority;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jdk.jshell.Snippet;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tasks")
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

    @ManyToMany(mappedBy = "tasks")
    private Set<User> assignees = new HashSet<>();


    void setTask(Project project){
        this.project = project;
    }
}
