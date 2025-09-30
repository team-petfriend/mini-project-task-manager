package com.example.petfriend.entity;

import com.example.petfriend.entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comments",
        indexes = {
                @Index(name = "idx_comment_task_id", columnList = "task_id"),
                @Index(name = "idx_comment_commenter", columnList = "commenter")})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comments extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id", nullable = false
            , foreignKey = @ForeignKey(name = "fk_comments_task")
    )
    private Task task;

    @Column(nullable = false, length = 500)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "commenter", nullable = false
            , foreignKey = @ForeignKey(name = "fk_comments_user"))
    private User commenter;


    private Comments(String content, User commenter) {
        this.content = content;
        this.commenter = commenter;
    }

    public static Comments create(String content, User commenter) {
        return new Comments(content, commenter);
    }

    void setTask(Task task) {
        this.task = task;
    }

    public void changeContent(String content) {
        this.content = content;
    }
}