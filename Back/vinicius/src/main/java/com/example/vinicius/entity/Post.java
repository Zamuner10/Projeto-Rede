package com.example.vinicius.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "/posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mediaUrl;
    private String mediaType;

    private String caption;

    @ManyToOne
    private User user;

    private LocalDateTime createdAt;

    public Post(Long id, String mediaUrl, String mediaType, String caption, User user, LocalDateTime createdAt) {
        this.id = id;
        this.mediaUrl = mediaUrl;
        this.mediaType = mediaType;
        this.caption = caption;
        this.user = user;
        this.createdAt = createdAt;
    }

    public Post() {}

    public void setId(Long id) {this.id = id;}
    public Long getId() {return id;}

    public void setMediaUrl(String mediaUrl) {this.mediaUrl = mediaUrl;}
    public String getMediaUrl() {return mediaUrl;}

    public void setMediaType(String mediaType) {this.mediaType = mediaType;}
    public String getMediaType() {return mediaType;}

    public String getCaption() {return caption;}
    public void setCaption(String caption) {this.caption = caption;}

    public void setUser(User user) {this.user = user;}
    public User getUser() {return user;}

    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}
    public LocalDateTime getCreatedAt() {return createdAt;}
}
