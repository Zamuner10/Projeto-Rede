package com.example.vinicius.service;

import com.example.vinicius.dto.LoginRequestDTO;
import com.example.vinicius.entity.User;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {


    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private LoginService loginService;

    @Mock
    private JwtService jwtService;

    @Test
    @DisplayName("Deve retornar o token JWT quando as credenciais forem válidas")
    void login_shouldReturnToken_whenCredentialsAreValid(){

        LoginRequestDTO dto = new LoginRequestDTO("luan@email.com", "Senha123*");

        User user = new User();
        user.setEmail("luan@email.com");

        Authentication authMock = mock(Authentication.class);
        when(authMock.getPrincipal()).thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authMock);
        when(jwtService.generatedToken(user)).thenReturn("token_jwt_valido_123");

        String token = loginService.login(dto);
        assertNotNull(token);
        assertEquals("token_jwt_valido_123", token);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generatedToken(user);
    }

    @Test
    @DisplayName("Deve retornar uma exceção quando a senha for inválida")
    void login_shouldThrowException_whenPasswordIsInvalid(){

        LoginRequestDTO dto = new LoginRequestDTO("luan@email.com", "SenhaInvalida");
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));
        assertThrows(BadCredentialsException.class, () -> loginService.login(dto));
        verify(jwtService, never()).generatedToken(any());
    }

    @Test
    @DisplayName("Deve retornar uma execeção quando não achar o email")
    void login_shouldThrowException_whenEmailNotFound(){
        LoginRequestDTO dto = new LoginRequestDTO("email_inexistente@email.com", "Senha123*");
        when(authenticationManager.authenticate(any())).thenThrow(new UsernameNotFoundException("Usuário não encontrado"));
        assertThrows(UsernameNotFoundException.class, () -> loginService.login(dto));
        verify(jwtService, never()).generatedToken(any());
    }
}

