package com.example.petfriend.entity;

import com.example.petfriend.entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects",
        indexes = {
                @Index(name = "idx_project_name", columnList = "name"),
                @Index(name = "idx_project_createAt", columnList = "created_at")
        },
        uniqueConstraints = {@UniqueConstraint(name = "uk_projects_name", columnNames = "name")}
)
@NoArgsConstructor
@Getter
public class Project extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,
    foreignKey = @ForeignKey(name = "fk_project_user_id"))
    private User user;

    @NotNull
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();
    public void addTask(Task task){
        tasks.add(task);
        task.setTask(null);
    }

    public void removeTask(Task task){
        tasks.remove(task);
        task.setTask(null);
    }

    @Builder
    public Project(@NotNull User user){
        this.user = user;
    }
    public void setName(String name){
        this.name = name;
    }




}
