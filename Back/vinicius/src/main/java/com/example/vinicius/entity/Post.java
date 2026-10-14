package com.example.vinicius.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mediaUrl;
    private String mediaType;

    private String caption;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy= "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostLike> like = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comment = new ArrayList<>();

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

    public void setLike(List<PostLike> like) {this.like = like;}
    public List<PostLike>getLike() {return like;}

    public void setComment(List<Comment> comment){this.comment = comment;}
    public List<Comment> getComment() {return comment;}
}
