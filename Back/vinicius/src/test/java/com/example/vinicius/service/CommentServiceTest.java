package com.example.vinicius.service;

import com.example.vinicius.dto.CommentRequestDTO;
import com.example.vinicius.dto.CommentResponseDTO;
import com.example.vinicius.entity.Comment;
import com.example.vinicius.entity.Post;
import com.example.vinicius.entity.User;
import com.example.vinicius.repository.CommentRepository;
import com.example.vinicius.repository.PostRepository;
import com.example.vinicius.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;


    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private CommentService commentService;


    @Test
    @DisplayName("Deve lançar execeção se não achar um comentário")
    void createComment_shouldThrowException_whenPostNotFound(){

        Long postId = 1L;
        CommentRequestDTO dto = new CommentRequestDTO("Testando Comentário !");
        User user = new User();

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> commentService.addComment(postId, dto, user));
        assertEquals("Post não encontrado", exception.getMessage());
        verify(commentRepository, never()).save(any());

    }

    @Test
    @DisplayName("Deve lançar execeção ao se criar um comentario no post")
    void createComment_shouldCreateComment_whenDataIsValid(){

        Long postId = 1L;
        CommentRequestDTO dto = new CommentRequestDTO("Excelente Post!");
        User user = new User();

        user.setId(1L);
        user.setName("Luan");

        when(postRepository.findById(postId)).thenReturn(Optional.of(new Post()));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CommentResponseDTO response = commentService.addComment(postId,dto, user);

        assertNotNull(response);
        assertEquals("Excelente Post!", response.content());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

}
