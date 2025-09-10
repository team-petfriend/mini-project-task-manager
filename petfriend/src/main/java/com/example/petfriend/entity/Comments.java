package com.example.petfriend.entity;

import com.example.petfriend.entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "task")
public class Comments extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_comment_user"))
    private User user;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false, length = 100)
    private String commenter;

    private Comments(String content, String commenter) {
        this.content = content;
        this.commenter = commenter;
    }

    public static Comments create(String content, String commenter) {
        return new Comments(content, commenter);
    }

    void setTask(Task task) {
        this.task = task;
    }

    public void changeContent(String content) {
        this.content = content;
    }
}
