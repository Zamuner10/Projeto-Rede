package com.example.vinicius.controller;

import com.example.vinicius.dto.PostRequestDTO;
import com.example.vinicius.dto.PostResponseDTO;
import com.example.vinicius.entity.User;
import com.example.vinicius.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostResponseDTO> createdPost(@RequestBody @Valid PostRequestDTO dto, @AuthenticationPrincipal User user) {
        PostResponseDTO response = postService.createPost(dto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getFeed(@AuthenticationPrincipal User loggedUser){
        List<PostResponseDTO> feed = postService.getFeed(loggedUser);
        return ResponseEntity.ok(feed);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@AuthenticationPrincipal User loggedUser, @PathVariable Long id){
        return ResponseEntity.ok(postService.getPostById(loggedUser, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, @AuthenticationPrincipal User user) {
        postService.deletePost(id, user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likePost(@PathVariable Long id, @AuthenticationPrincipal User user) {
        postService.likePost(id, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/like")
    public ResponseEntity<Void> unlikePost(@PathVariable Long id, @AuthenticationPrincipal User user) {
        postService.unlikePost(id, user);
        return ResponseEntity.noContent().build();
    }
}