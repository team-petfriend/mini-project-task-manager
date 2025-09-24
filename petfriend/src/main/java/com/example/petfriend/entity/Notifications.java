package com.example.petfriend.entity;

import com.example.petfriend.common.enums.RefType;
import com.example.petfriend.common.enums.Type;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications",
        indexes = @Index(name = "idx_notify_user_read", columnList = "user_id, is_read"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Notifications {
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
    private Type type;

    // 참조 대상 종류
    @Enumerated(EnumType.STRING)
    @Column(name = "ref_type", length = 20, nullable = false)
    private RefType refType;

    // 참조 대상 ID
    @Column(name = "ref_id", nullable = false)
    private Long refId;

    // 알림 메시지
    @Column(length = 255, nullable = false)
    private String message;

    // 읽음 여부
    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "DATETIME(6)")
    private LocalDateTime createdAt;

    public void markAsRead() {
        this.isRead = true;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }
}