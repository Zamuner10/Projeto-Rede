package com.example.vinicius.controller;

import com.example.vinicius.dto.MessageResponseDTO;
import com.example.vinicius.dto.SendMessageDTO;


import com.example.vinicius.entity.User;
import com.example.vinicius.repository.UserRepository;
import com.example.vinicius.service.ChatService;
import com.example.vinicius.service.UserService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


import java.security.Principal;

@Controller
public class ChatController {
    private final ChatService chatService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController (ChatService chatService, UserService userService, SimpMessagingTemplate messagingTemplate){
        this.chatService = chatService;
        this.userService = userService;
        this.messagingTemplate = messagingTemplate;
    }


    @MessageMapping("/chat.sendMessage")
    public void processMessage(@Payload SendMessageDTO dto, Principal principal){
        User sender = userService.findByUsername(principal.getName());

        MessageResponseDTO responseDTO = chatService.sendMessage(sender, dto);

        messagingTemplate.convertAndSend("/queue/messages-" + dto.receiverId(), responseDTO);
    }


}
