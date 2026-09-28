package com.example.vinicius.dto;

import jakarta.validation.constraints.NotBlank;

public record ProfileRequestDTO(
//        @NotBlank(message="Nickname obrigatório")
        String nickname,

        String photoUrl,
        String bio
) { }
