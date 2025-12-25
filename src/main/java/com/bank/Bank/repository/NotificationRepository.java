package com.bank.Bank.repository;

import com.bank.Bank.model.Notification;
import com.bank.Bank.model.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Notification> findByUserIdAndIsRead(Long userId, Boolean isRead);
    List<Notification> findByUserIdAndNotificationType(Long userId, NotificationType notificationType);
    Long countByUserIdAndIsRead(Long userId, Boolean isRead);
}


