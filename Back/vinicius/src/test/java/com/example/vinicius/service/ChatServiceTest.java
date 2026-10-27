package com.example.vinicius.service;

import com.example.vinicius.dto.MessageResponseDTO;
import com.example.vinicius.dto.SendMessageDTO;
import com.example.vinicius.entity.Conversation;
import com.example.vinicius.entity.Message;
import com.example.vinicius.entity.User;
import com.example.vinicius.repository.ConversationRepository;
import com.example.vinicius.repository.MessageRepository;
import com.example.vinicius.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChatServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MessageResponseDTO messageResponseDTO;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ConversationRepository conversationRepository;

    @InjectMocks
    private ChatService chatService;

    @Test
    @DisplayName("Deve lançar exceção se não encontrar o usuário destinatário")
    void sendMessage_shouldThrowsException_whenReceiverNotFund(){

        User sender = new User();
        SendMessageDTO dto = new SendMessageDTO(1L, "Olá, tudo bem?");


        when(userRepository.findById(dto.receiverId())).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> chatService.sendMessage(sender, dto));

        assertEquals("Usuário destinatário não encontrado ", exception.getMessage());
        verify(messageRepository, never()).save(any());
        verify(conversationRepository,never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar execeção se não existir uma conversa de usuário")
    void sendMessage_shouldCreateNewConversationAndSaveMessage_whenConversationDoesNotExist(){

        User user = new User();
        user.setId(1L);


        User receiver = new User();
        receiver.setId(2L);

        SendMessageDTO dto = new SendMessageDTO(2L, "Olá, Tudo bem ");

        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));
        when(conversationRepository.findByUser1AndUser2OrUser1AndUser2(any(), any(), any(), any())).thenReturn(Optional.empty());
        when(conversationRepository.save(any(Conversation.class))).thenAnswer(i -> i.getArgument(0));
        when(messageRepository.save(any(Message.class))).thenAnswer(i -> i.getArgument(0));

        MessageResponseDTO response = chatService.sendMessage(user, dto);

        assertNotNull(response);
        assertEquals("Olá, Tudo bem ", response.content());
        verify(conversationRepository, times(1)).save(any(Conversation.class));
        verify(messageRepository, times(1)).save(any(Message.class));

    }

    @Test
    @DisplayName("Deve reutilizar conversa existente ao enviar mensagem")
    void sendMessage_shoulReuseConversationAndSaveMessage_whenConversationExists(){
        User user = new User();
        user.setId(1L);

        User receiver = new User();
        receiver.setId(2L);

        Conversation existingConversation = new Conversation(user, receiver);
        SendMessageDTO dto = new SendMessageDTO(2L, "Segunda mensagem!");

        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));
        when(conversationRepository.findByUser1AndUser2OrUser1AndUser2(any(), any(), any(), any()))
                .thenReturn(Optional.of(existingConversation));
        when(messageRepository.save(any(Message.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MessageResponseDTO response = chatService.sendMessage(user, dto);

        assertNotNull(response);
        assertEquals("Segunda mensagem!", response.content());
        verify(conversationRepository, never()).save(any());
        verify(messageRepository, times(1)).save(any(Message.class));

    }
}
