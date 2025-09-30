package com.example.petfriend.entity;

import com.example.petfriend.common.enums.Field;
import com.example.petfriend.entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "task_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_task_history_task"))
    private Task task;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "actor_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_task_history_actor"))
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "field", length = 20)
    private Field field;

    @Size(max = 255)
    @Column(name = "old_value", length = 255)
    private String old_value;

    @Size(max = 255)
    @Column(name = "new_value", length = 255)
    private String new_value;

    @Builder
    public TaskHistory(Task task, User user,Field field, String old_value, String new_value){
        this.task = task;
        this.user= user;
        this.field = field;
        this.old_value = old_value;
        this.new_value = new_value;
    }
}
