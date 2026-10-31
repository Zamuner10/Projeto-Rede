package com.example.vinicius.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EmailService emailService;


    @Test
    @DisplayName("Deve enviar e-mail com sucesso através do RestTemplate")
    void sendEmail_shouldSendEmailSucessfully(){

        String to = "usuario@email.com";
        String subject = "Bem-vindo!";
        String content = "Sua conta foi criada";

        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>("Email enviado com sucesso!", HttpStatus.OK));
        emailService.sendEmail(to, subject, content);

        verify(restTemplate, times(1)).postForEntity(anyString(), any(), eq(String.class));
    }


    @Test
    @DisplayName("Deve lançar exceção quando a chamada da API de e-mail falhar")
    void sendEmail_shouldThrowsException_whenApiFails(){

        String to = "usuario@email.com";
        String subject = "Bem-vindo!";
        String contentc = "Sua conta foi criada";

        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenThrow(new RuntimeException("Erro ao conectar com o servidor do e-mail"));
        assertThrows(RuntimeException.class, () -> emailService.sendEmail(to, subject, contentc));
        verify(restTemplate, times(1)).postForEntity(anyString(), any(), eq(String.class));
    }
}
