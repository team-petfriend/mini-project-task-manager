package com.example.petfriend.entity;

import com.example.petfriend.common.enums.RefType;
import com.example.petfriend.common.enums.Type;
import com.example.petfriend.entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notifications")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Notifications extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 알림을 받을 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_notify_user"))
    private User user;

    // 알림 유형
    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private Type type;  // TASK_ASSIGNED, MENTION, COMMENT, STATUS_CHANGED

    // 참조 대상 종류
    @Enumerated(EnumType.STRING)
    @Column(name = "ref_type", length = 20, nullable = false)
    private RefType refType;  // TASK or COMMENT

    // 참조 대상 ID
    @Column(name = "ref_id", nullable = false)
    private Long refId;

    // 알림 메시지
    @Column(length = 255, nullable = false)
    private String message;

    // 읽음 여부
    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;

    public void markAsRead() {
        this.isRead = true;
    }
}
