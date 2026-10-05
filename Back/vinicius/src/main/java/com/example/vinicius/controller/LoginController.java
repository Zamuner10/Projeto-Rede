package com.example.vinicius.controller;


import com.example.vinicius.dto.LoginRequestDTO;
import com.example.vinicius.dto.LoginResponseDTO;
import com.example.vinicius.service.LoginService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RequestMapping
@RestController
public class LoginController {

    private final LoginService loginService;

    @Value("${app.cookie.secure:false}")
    private boolean isSecureCookie;

    public LoginController(LoginService loginService){this.loginService = loginService;}

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login (@RequestBody @Valid LoginRequestDTO dto, HttpServletResponse response){

        String token = loginService.login(dto);
        ResponseCookie cookie = ResponseCookie.from("jwtToken", token)
                .httpOnly(true)
                .secure(isSecureCookie)
                .path("/")
                .maxAge(Duration.ofHours(2))
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(new LoginResponseDTO("Login successful"));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response){
        ResponseCookie cookei = ResponseCookie.from("jwtToken", "")
                .httpOnly(true)
                .secure(isSecureCookie)
                .path("/")
                .maxAge(0)
                .sameSite("None")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookei.toString());
        return ResponseEntity.ok().build();
    }
}
