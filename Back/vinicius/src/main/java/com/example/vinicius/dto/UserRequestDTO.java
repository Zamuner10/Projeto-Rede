package com.example.vinicius.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDTO (
        @NotBlank(message = "Nome é obrigatório")
        String name,

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "Formato Inválido")
        String email,

        @NotBlank(message= "Senha inválida ")
        String password,

        @NotBlank(message = "Confirmar senha obrigatório")
        String confirmPassword,

        @NotBlank(message= "Idade é obrigatória")
        @Min(value = 15, message = "Usuário deve ter pelo menos 15 anos")
        Integer age
){ }
