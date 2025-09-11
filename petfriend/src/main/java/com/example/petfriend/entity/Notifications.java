package com.example.petfriend.entity;

import com.example.petfriend.common.enums.RefType;
import com.example.petfriend.common.enums.Type;
import com.example.petfriend.entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "notifications",
        indexes = {
                @Index(name = "idx_notify_user_read", columnList = "user_id, is_read")
        }
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notifications extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 사용자 (N:1 관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private User user;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Type type;

    @Enumerated(EnumType.STRING)
    @Column(name = "ref_type", nullable = false, length = 20)
    private RefType refType;

    @Column(name = "ref_id", nullable = false)
    private Long refId; // 참조하는 엔티티 id (taskId or commentId)

    @Column(nullable = false, length = 255)
    private String message;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    @PrePersist
    public void prePersist() {
        this.isRead = false;
    }
}
