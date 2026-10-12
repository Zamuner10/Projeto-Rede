package com.example.vinicius.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequestDTO {

    @NotBlank(message = "O login (e-mail ou nickname) é obrigatório")
    private String login;

    @NotBlank(message = "A senha é obrigatória")
    private String password;

    public LoginRequestDTO() {}

    public LoginRequestDTO(String login, String password) {
        this.login = login;
        this.password = password;
    }

    // Getters e Setters
    public String getLogin() {return login;}

    public void setLogin(String login) {this.login = login;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}
}