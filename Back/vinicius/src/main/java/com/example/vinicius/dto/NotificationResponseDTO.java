package com.example.vinicius.dto;

import com.example.vinicius.enums.NotificationType;

import java.time.LocalDateTime;

public class NotificationResponseDTO {

    private Long id;
    private UserSummaryDTO sender;
    private NotificationType type;
    private boolean isRead;
    private LocalDateTime createdAt;

    public NotificationResponseDTO(Long id, UserSummaryDTO sender, NotificationType type, boolean isRead, LocalDateTime createdAt) {
        this.id = id;
        this.sender = sender;
        this.type = type;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public Long getId(){return id;}
    public UserSummaryDTO getSender(){return sender;}
    public NotificationType getType(){return type;}
    public boolean isRead(){return isRead;}
    public LocalDateTime getCreatedAt(){return createdAt;}
}
