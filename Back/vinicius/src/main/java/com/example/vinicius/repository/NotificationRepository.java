package com.example.vinicius.repository;
import com.example.vinicius.entity.Notification;
import com.example.vinicius.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByRecipientOrderByCreatedAtDesc(User recipient);
    long countByRecipientOrderByCreatedAtDesc(User recipient);
}
