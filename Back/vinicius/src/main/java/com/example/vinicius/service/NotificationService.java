package com.example.vinicius.service;

import com.example.vinicius.entity.Notification;
import com.example.vinicius.entity.User;
import com.example.vinicius.enums.NotificationType;
import com.example.vinicius.repository.FollowRepository;
import com.example.vinicius.repository.NotificationRepository;
import com.example.vinicius.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository){
        this.notificationRepository = notificationRepository;
    }



    @Transactional
    public void createNotification(User recipient, User sender, NotificationType type){
        if(recipient.getId().equals(sender.getId())){
            return;
        }
        Notification notification = new Notification(recipient, sender, type);
        notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public List<Notification> getUserNotifications(User recipient){
        return notificationRepository.findByRecipientOrderByCreatedAtDesc(recipient);
    }

    @Transactional
    public void markAsRead(Long notificationId, User recipient){
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notificação não encontrada"));
        if(!notification.getRecipient().equals(recipient)){
            throw new IllegalArgumentException("Você não tem permissão para alterar está notificação.");
        }

        notification.setRead(true);
        notificationRepository.save(notification);
    }

}
