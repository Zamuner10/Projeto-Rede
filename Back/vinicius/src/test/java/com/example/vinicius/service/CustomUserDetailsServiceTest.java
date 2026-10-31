package com.example.vinicius.service;

import com.example.vinicius.entity.User;
import com.example.vinicius.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @DisplayName("Deve carregar o usuário pelo e-mail ou pelo nickname quando ele existir ")
    void loadUserByUsername_shouldReturnUserDetails_whenUserExists(){
        User user = new User();
        user.setEmail("luan@email.com");

        when(userRepository.findByEmailOrNickname("luan@email.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("luan@email.com");

        assertNotNull(userDetails);
        assertEquals("luan@email.com", userDetails.getUsername());
        verify(userRepository, times(1)).findByEmailOrNickname("luan@email.com");
    }

    @Test
    @DisplayName("Deve lançar exceção quando o usuário não tiver e-mail cadastrado")
    void loadUserByUsername_shouldThrowException_whenUserDoesNotExist(){
        when(userRepository.findByEmailOrNickname("email_inexistente@email.com")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername("email_inexistente@email.com"));

        verify(userRepository,times(1)).findByEmailOrNickname("email_inexistente@email.com");
    }
}
