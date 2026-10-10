package com.example.vinicius.service;

import com.example.vinicius.dto.CommentRequestDTO;
import com.example.vinicius.dto.CommentResponseDTO;
import com.example.vinicius.entity.Comment;
import com.example.vinicius.entity.Post;
import com.example.vinicius.entity.User;
import com.example.vinicius.repository.CommentRepository;
import com.example.vinicius.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public CommentResponseDTO addComment(Long postId, CommentRequestDTO dto, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));

        Comment comment = new Comment();
        comment.setContent(dto.content());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setPost(post);
        comment.setUser(user);

        Comment savedComment = commentRepository.save(comment);

        return new CommentResponseDTO(
                savedComment.getId(),
                savedComment.getContent(),
                savedComment.getCreatedAt(),
                savedComment.getUser().getId(),
                savedComment.getUser().getUsername()
        );
    }

    public List<CommentResponseDTO> getCommentsByPost(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);

        return comments.stream()
                .map(comment -> new CommentResponseDTO(
                        comment.getId(),
                        comment.getContent(),
                        comment.getCreatedAt(),
                        comment.getUser().getId(),
                        comment.getUser().getUsername()
                ))
                .toList();
    }
}
