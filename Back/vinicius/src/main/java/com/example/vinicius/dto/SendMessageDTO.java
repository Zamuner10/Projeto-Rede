package com.example.vinicius.dto;

public record SendMessageDTO (
        Long receiverId,
        String content
){
    public SendMessageDTO(Long receiverId, String content){
        this.receiverId = receiverId;
        this.content = content;
    }
}
