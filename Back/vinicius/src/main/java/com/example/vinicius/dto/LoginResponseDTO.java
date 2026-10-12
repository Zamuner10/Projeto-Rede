package com.example.vinicius.dto;

public record LoginResponseDTO (
        String token,
        String type
){
    public LoginResponseDTO(String token){
        this(token, "Bearer");
    }

}

