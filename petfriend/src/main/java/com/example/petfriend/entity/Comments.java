package com.example.petfriend.entity;

import com.example.petfriend.entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "comments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comments extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @MapsId("taskId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_comment_user"))
    private User commenter;

    @Column(nullable = false, length = 500)
    private String content;


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
