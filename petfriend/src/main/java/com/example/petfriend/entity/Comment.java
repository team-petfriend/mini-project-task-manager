package com.example.petfriend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "task")
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false, length = 100)
    private String commenter;

    private Comment(String content, String commenter) {
        this.content = content;
        this.commenter = commenter;
    }

    public static Comment create(String content, String commenter) {
        return new Comment(content, commenter);
    }

    void setPost(Task post) {
        this.task = post;
    }

    public void changeContent(String content) {
        this.content = content;
    }
}
