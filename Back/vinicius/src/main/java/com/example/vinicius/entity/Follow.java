package com.example.vinicius.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "follows", uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "following_id"}) )
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="follower_id", nullable = false)
    private User follower;

    @ManyToOne
    @JoinColumn(name="following_id", nullable = false)
    private User following;

    private LocalDateTime createdAt;

    public Follow() {}

    public Follow (User follower, User following) {
        this.follower = follower;
        this.following = following;
    }
    public Follow(Long id, User follower, User following,  LocalDateTime createdAt) {
        this.id = id;
        this.follower = follower;
        this.following = following;
        this.createdAt = createdAt;
    }

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
    }

    public void setId(Long id) {this.id = id;}
    public Long getId(){return id;}

    public void setFollower(User follower){this.follower = follower;}
    public User getFollower() {return follower;}

    public void setFollowing(User following){this.following = following;}
    public User getFollowing() {return following;}

}
