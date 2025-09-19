package com.example.petfriend.repository;

import com.example.petfriend.entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notifications, Long> {
    List<Notifications> findByUserIdAndIsRead(Long userId, Boolean isRead);
    List<Notifications> findByUserId(Long userId);
}
