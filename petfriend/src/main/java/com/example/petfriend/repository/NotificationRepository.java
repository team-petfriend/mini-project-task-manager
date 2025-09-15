package com.example.petfriend.repository;

import com.example.petfriend.entity.Notifications;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NotificationRepository extends CrudRepository<Notifications, Long> {
    List<Notifications> findByUserIdAndIsRead(Long userId, Boolean isRead);
    List<Notifications> findByUserId(Long userId);
}
