package com.example.petfriend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "task_tag")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskTag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id", nullable = false, foreignKey =  @ForeignKey(name = "fk_task_tag_task"))
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tag_id", nullable = false, foreignKey =  @ForeignKey(name = "fk_task_tag_tag"))
    private Tag tag;

    @Builder
    public TaskTag(@NotNull Task task, Tag tag) {
        this.task = task;
        this.tag = tag;
    }
}
