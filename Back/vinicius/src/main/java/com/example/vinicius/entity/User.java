package com.example.vinicius.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")

public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    @Size(min = 8)
    private String name;

    @NotBlank
    @Size(min = 8)
    @Column(nullable = false)
    private String password;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @NotNull
    @Min(15)
    private Integer age;

    public User (Long id, String name, String password, String email, Integer age){
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.age = age;
    }
    public User(){}

    public void setId(Long id){this.id = id; }
    public Long getId() {return id;}

    public void setName (String name) { this.name = name;}
    public String getName () { return name; }

    public void setPassword(String password) { this.password = password;}
    public String getPassword() { return password; }

    public void setEmail(String email) { this.email=email;}
    public String getEmail() {return email;}

    public void setAge (Integer age) {this.age = age;}
    public Integer getAge() { return age;}

    @Override
    public Collection< ? extends GrantedAuthority> getAuthorities(){return List.of();}

    @Override
    public String getUsername(){ return email;}

    @Override
    public boolean isAccountNonExpired() {return UserDetails.super.isAccountNonExpired();}

    @Override
    public boolean isAccountNonLocked(){return UserDetails.super.isAccountNonLocked();}

    @Override
    public boolean isCredentialsNonExpired(){return UserDetails.super.isCredentialsNonExpired();}
}
