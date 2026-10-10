package com.example.vinicius.dto;

import java.time.LocalDateTime;

public record CommentResponseDTO(
        Long id,
        String content,
        LocalDateTime createdAt,
        Long User,
        String username
) {
}
