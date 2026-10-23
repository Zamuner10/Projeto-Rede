package com.example.vinicius.service;

import com.example.vinicius.dto.MessageResponseDTO;
import com.example.vinicius.dto.SendMessageDTO;
import com.example.vinicius.entity.Conversation;
import com.example.vinicius.entity.Message;
import com.example.vinicius.entity.User;
import com.example.vinicius.repository.ConversationRepository;
import com.example.vinicius.repository.MessageRepository;
import com.example.vinicius.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ChatService {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;

    public ChatService(UserRepository userRepository, MessageRepository messageRepository, ConversationRepository conversationRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
    }

    @Transactional
    public MessageResponseDTO sendMessage(User sender, SendMessageDTO dto){
            User receiver = userRepository.findById(dto.receiverId())
                    .orElseThrow( () -> new RuntimeException("Usuário destinatário não encontrado "));

        Conversation conversation = conversationRepository.findByUser1AndUser2OrUser1AndUser2(sender, receiver, receiver, sender)
                .orElseGet(() -> {
                    Conversation newConversation = new Conversation(sender, receiver);
                    return conversationRepository.save(newConversation);
                });

        Message message = new Message();

        message.setConversation(conversation);
        message.setSender(sender);
        message.setContent(dto.content());
        message.setCreatedAt(LocalDateTime.now());
        message.setIsRead(false);

        Message savedMessage = messageRepository.save(message);

        return new MessageResponseDTO(
                savedMessage.getId(),
                conversation.getId(),
                sender.getId(),
                savedMessage.getContent(),
                savedMessage.getCreatedAt()
        );
    }
}
