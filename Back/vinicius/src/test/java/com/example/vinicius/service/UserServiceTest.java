package com.example.vinicius.service;


import com.example.vinicius.dto.UserRequestDTO;

import com.example.vinicius.entity.Profile;
import com.example.vinicius.repository.ProfileRepository;
import com.example.vinicius.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserService userService;


    @Test
    void registerUser_shouldThrowException_whenNameIsEmpty(){
        UserRequestDTO dto = new UserRequestDTO(" ", "luanGargalo@email.com", "luGarga", "Senha123*", "Senha123*", 20);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.registerUser(dto));
        assertEquals("Nome não pode ficar vazio.", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerUser_shouldThrowException_whenNameIsInvalid(){
        UserRequestDTO dto = new UserRequestDTO("luan", "luanGargalo@email.com", "luGarga", "Senha123*", "Senha123*", 20);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.registerUser(dto));
        assertEquals("A primeira letra deve ser Maiúscula", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerUser_shouldThrowException_whenNameContainsSpecialCharacters(){
        UserRequestDTO dto = new UserRequestDTO("Lu@n", "luanGargalo@email.com", "luGarga", "Senha123*", "Senha123*", 20);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.registerUser(dto));
        assertEquals("Não pode conter caracteres especiais.", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerUser_shouldThrowException_whenNameContainsNumber(){
        UserRequestDTO dto = new UserRequestDTO("Luan10", "luanGargalo@email.com", "luGarga", "Senha123*", "Senha123*", 20);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.registerUser(dto));
        assertEquals("Não pode conter números no nome.", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar cadastrar nickname vazio.")
    void registerUser_shouldThrowException_whenNicknameIsEmpty(){
        UserRequestDTO dto = new UserRequestDTO("Luan", " ", "luanGargalo@email.com", "Senha123*", "Senha123*", 20);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.registerUser(dto));
        assertEquals("Nickname não pode ficar vazio.", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o nickname já estiver cadastrado no sistema")
    void registerUser_shouldThrowException_whenNicknameAlreadyExist(){
        UserRequestDTO dto = new UserRequestDTO("Luan", "luGarga", "luanGargalo@email.com", "Senha123*", "Senha123*", 20);
        when(profileRepository.findByNickname("luGarga")).thenReturn(Optional.of(new Profile()));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.registerUser(dto));
        assertEquals("Nickname já está em uso por outro usuário!", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando a senha estivar vazia")
    void registerUser_shouldThrowException_whenPasswordIsEmpty(){
        UserRequestDTO dto = new UserRequestDTO("Luan", "luGarga", "luanGargalo@email.com", " ", "Senha123*", 20);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.registerUser(dto));
        assertEquals("Senha não pode estar vazia!", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",                     // Senha vazia
            "1232465",              //  Senha sem letras
            "senha123*",            // Sem letra maiúscula
            "SenhaSemNumero*",      // Sem número
            "Senha12345"            // Sem caractere especial
    })
    @DisplayName("Deve lançar exceção para senhas fracas ou inválidas")
    void registerUser_shouldThrowException_whenPasswordIsWeak(String weakPassword){
        UserRequestDTO dto = new UserRequestDTO("Luan", "luGarga", "luanGargalo@email.com", weakPassword, weakPassword, 20);
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(dto));
        verify(userRepository, never()).save(any());
    }


    @Test
    @DisplayName("Deve lançar exceção quando a confirmação de senha for diferente da senha")
    void registerUser_shouldThrowException_whenPasswordDoNotMatch(){
        UserRequestDTO dto = new UserRequestDTO("Luan", "luGarga", "luanGargalo@email.com", "Luan10*", "Luan10", 20);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.registerUser(dto));
        assertEquals("As senhas não coincidem", exception.getMessage());
        verify(userRepository,never()).save(any());
    }
}
