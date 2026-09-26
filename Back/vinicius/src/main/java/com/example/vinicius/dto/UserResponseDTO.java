package com.example.vinicius.dto;


import com.example.vinicius.entity.User;

public record UserResponseDTO (
        Long id,
        String name,
        String password,
        String email,
        Integer age
){
    public UserResponseDTO(User user){
        this(user.getId(), user.getName(), user.getPassword(),user.getEmail(), user.getAge());
    }
}
