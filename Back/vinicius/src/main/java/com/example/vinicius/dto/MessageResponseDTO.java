package com.example.vinicius.dto;

import java.time.LocalDateTime;

public record MessageResponseDTO(
        Long id,
        Long conversationId,
        Long senderId,
        String content,
        LocalDateTime createdAt
) {
    public MessageResponseDTO(Long id, Long conversationId, Long senderId, String content, LocalDateTime createdAt) {
        this.id = id;
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.content = content;
        this.createdAt = createdAt;
    }
}
