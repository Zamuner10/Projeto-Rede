package com.example.vinicius.dto;

import java.time.LocalDateTime;

public record PostResponseDTO(
        Long id,
        String caption,
        String mediaUrl,
        String mediaType,
        LocalDateTime createdAt,
        String authorNickname,
        String authorPhotoUrl
){}

