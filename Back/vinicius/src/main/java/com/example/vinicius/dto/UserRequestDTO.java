package com.example.vinicius.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDTO (
        @NotBlank(message = "Nome é obrigatório")
        String name,

        @NotBlank(message = "Nickname obrigatório")
        @Size(min = 3, max = 15, message = "O nickname deve ter 3 a 15 caracteres")
        String nickname,

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "Formato Inválido")
        String email,

        @NotBlank(message= "Senha inválida ")
        @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
        String password,

        @NotBlank(message = "Confirmar senha obrigatório")
        String confirmPassword,

        @Min(value = 13, message = "É necessário ter pelo menos 13 anos")
        Integer age
){ }
