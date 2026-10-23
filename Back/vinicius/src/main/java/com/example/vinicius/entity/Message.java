package com.example.vinicius.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="conversation_id", nullable = false)
    private Conversation conversation;

    @ManyToOne
    @JoinColumn(name="sender_id", nullable = false)
    private User sender;

    @Column(nullable = false, columnDefinition ="TEXT")
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_read", nullable = false)
    private boolean isRead;


    public Message(){}

    public Message(Long id, Conversation conversation, User sender, String content, LocalDateTime createdAt, boolean isRead) {
        this.id = id;
        this.conversation = conversation;
        this.sender = sender;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.isRead = false;
    }

    public Long getId() {return id;}
    public void setId(Long id){this.id = id;}

    public Conversation getConversation() {return conversation;}
    public void setConversation(Conversation conversation) {this.conversation = conversation;}

    public User getSender() {return sender;}
    public void  setSender(User sender) {this.sender = sender;}

    public String getContent(){return content;}
    public void setContent(String content){this.content = content;}

    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt){ this.createdAt = createdAt;}

    public boolean getIsRead() {return isRead;}
    public void setIsRead(boolean isRead) {this.isRead = isRead;}
}
