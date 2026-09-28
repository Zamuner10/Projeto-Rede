package com.example.vinicius.controller;

import com.example.vinicius.dto.UserRequestDTO;
import com.example.vinicius.dto.UserResponseDTO;
import com.example.vinicius.entity.User;
import com.example.vinicius.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController (UserService userService) { this.userService = userService; }

    @PostMapping
    public  ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO dto){
        User registerUser = userService.registerUser(dto);
        UserResponseDTO response = new UserResponseDTO(registerUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

//    @GetMapping("/me")
//    public ResponseEntity<UserResponseDTO> getAuthenticatedUser(@AuthenticationPrincipal User currentUser){
//        UserResponseDTO response = new UserResponseDTO(currentUser);
//        return ResponseEntity.ok(response);
//    }
}
