package com.example.vinicius.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;


@Entity
@Table(name="/profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @Size(max =15)
    private String nickname;

    private String photoUrl;
    private String bio;


    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Profile(Long id, String nickname, String photoUrl, String bio, User user) {
        this.id = id;
        this.nickname = nickname;
        this.bio = bio;
        this.photoUrl = photoUrl;
        this.user = user;
    }

    public Profile() {}

    public void setId(Long id) { this.id = id; }
    public Long getId(){return id;}

    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getNickname(){ return nickname;}

    public void setPhotoUrl(String photoUrl) {this.photoUrl = photoUrl;}
    public String getPhotoUrl(){return photoUrl;}

    public void setBio(String bio) {this.bio = bio;}
    public String getBio(){return bio;}

    public void setUser(User user) {this.user = user;}
    public User getUser(){return user;}
}
