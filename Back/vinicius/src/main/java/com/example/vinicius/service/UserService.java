package com.example.vinicius.service;

import com.example.vinicius.dto.UserRequestDTO;
import com.example.vinicius.repository.UserRepository;
import com.example.vinicius.entity.User;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserService (UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public User registerUser(UserRequestDTO dto){

        validateName(dto.name());
        validatePassword(dto.password());
        validateEmailAlreadyExist(dto.email());

    User user = new User();
    user.setName(dto.name());
    user.setPassword(passwordEncoder.encode(dto.password()));
    user.setEmail(dto.email());
    user.setAge(dto.age());

    User savedUser = userRepository.save(user);

    return savedUser;
    }

    private void validateEmailAlreadyExist(String email){
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            throw new IllegalArgumentException("Endereço de e-mail já cadastrado !");
        }
    }

    private void validateName(String name){
        if(name == null || name.isBlank()){
            throw new IllegalArgumentException("Nome não pode ficar vazio.");
        }
        char firstLetter = name.charAt(0);
        if(!Character.isUpperCase(firstLetter)){
            throw new IllegalArgumentException("A primeira letra deve ser Maiúscula");
        }
        for(int i = 0 ; i < name.length(); i++){
            char ch = name.charAt(i);
            if(Character.isDigit(ch)){
                throw new IllegalArgumentException("Não pode conter números no nome.");
            }
            if(!Character.isLetter(ch)  && !Character.isWhitespace(ch)){
                throw new IllegalArgumentException("Não pode conter caracteres especiais.");
            }
        }
    }

    private void validatePassword(String password){
        if(password == null || password.isBlank()){
            throw new IllegalArgumentException("Senha não pode estar vazia !");
        }
        boolean hasLetter = false;
        boolean hasUppercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (int i = 0; i <password.length(); i++){
            char ch = password.charAt(i);

            if(Character.isLetter(ch)){
                hasLetter = true;
            }
            if (Character.isUpperCase(ch)){
                hasUppercase = true;
            }
            if(Character.isDigit(ch)){
                hasDigit = true;
            }
            if (!Character.isLetterOrDigit(ch) && !Character.isWhitespace(ch)){
                hasSpecialChar = true;
            }
        }

        if (!hasLetter){
            throw new IllegalArgumentException("Senha deve conter pelo menos uma Letra");
        }

        if(!hasUppercase){
            throw  new IllegalArgumentException("Senha deve conter pelo menos uma Letra Maiúscula");
        }
        if(!hasDigit){
            throw new IllegalArgumentException("Senha deve conter pelo menos um número");
        }
        if(!hasSpecialChar){
            throw new IllegalArgumentException("Senha deve conter pelo menos um caractere especial ");
        }
    }
}


