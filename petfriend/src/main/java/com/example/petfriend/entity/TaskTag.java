package com.example.petfriend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "task_tag",
        uniqueConstraints ={
                @UniqueConstraint(name = "uk_task_tag_task", columnNames = "task_id"),
                @UniqueConstraint(name = "uk_task_tag_tag", columnNames = "tag_id")
        }

)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskTag {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id", nullable = false, foreignKey =  @ForeignKey(name = "fk_task_tag_task"))
    private Task TaskTagTask;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tag_id", nullable = false, foreignKey =  @ForeignKey(name = "fk_task_tag_tag"))
    private Tag TaskTagtag;
}
