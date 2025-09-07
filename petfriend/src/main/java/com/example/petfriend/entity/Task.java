package com.example.petfriend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "task")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false,
    foreignKey = @ForeignKey(name = "fk_task_project"))
    private Project project;

    void setTask(Project project){
        this.project = project;
    }
}
