package com.example.vinicius.entity;

import com.example.vinicius.enums.NotificationType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    private boolean isRead;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Notification(){}

    public Notification(User recipient, User sender, NotificationType type) {
        this.recipient = recipient;
        this.sender = sender;
        this.type = type;
        this.createdAt = LocalDateTime.now();
        this.isRead = false;
    }

    public Long getId(){return id;}
    public void setId(Long id){ this.id = id;}

    public User getRecipient(){return recipient;}
    public void setRecipient(User recipient){this.recipient = recipient;}

    public User getSender(){return sender;}
    public void setSender(User sender){this.sender = sender;}

    public NotificationType getType(){return type;}
    public void setType(NotificationType type){this.type = type;}

    public boolean isRead(){return isRead;}
    public void setRead(boolean isRead){this.isRead = isRead;}

    public LocalDateTime getCreatedAt(){return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt){this.createdAt = createdAt;}


}
