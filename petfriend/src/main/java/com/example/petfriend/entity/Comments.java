package com.example.petfriend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "task")
public class Comments {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Comment("댓글 내용")
    @Column(nullable = false, length = 1000)
    private String content;

    @Comment("댓글 작성자 표시명 또는 ID")
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
