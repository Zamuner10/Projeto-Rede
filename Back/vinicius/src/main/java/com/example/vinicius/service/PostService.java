package com.example.vinicius.service;

import com.example.vinicius.dto.PostRequestDTO;
import com.example.vinicius.dto.PostResponseDTO;
import com.example.vinicius.entity.Post;
import com.example.vinicius.entity.User;
import com.example.vinicius.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public PostResponseDTO createPost(PostRequestDTO dto, User user){

        Post post = new Post();

        post.setCaption(dto.caption());
        post.setMediaUrl(dto.mediaUrl());
        post.setMediaType(dto.mediaType());

        post.setCreatedAt(LocalDateTime.now());

        post.setUser(user);

        Post savedPost = postRepository.save(post);

        return new PostResponseDTO(
                savedPost.getId(),
                savedPost.getCaption(),
                savedPost.getMediaUrl(),
                savedPost.getMediaType(),
                savedPost.getCreatedAt(),
                savedPost.getUser().getProfile() != null ? savedPost.getUser().getProfile().getNickname() : savedPost.getUser().getName(),
                savedPost.getUser().getProfile() != null ? savedPost.getUser().getProfile().getPhotoUrl() : null
        );
    }

    public List<PostResponseDTO>getFeed(){
        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();
        List<PostResponseDTO> dtos = new ArrayList<>();

        for (Post post: posts){
            PostResponseDTO dto = new PostResponseDTO(
                    post.getId(),
                    post.getCaption(),
                    post.getMediaUrl(),
                    post.getMediaType(),
                    post.getCreatedAt(),
                    post.getUser().getProfile() != null ? post.getUser().getProfile().getNickname() : post.getUser().getName(),
                    post.getUser().getProfile() != null ? post.getUser().getProfile().getPhotoUrl() : null
            );
            dtos.add(dto);
        }
        return dtos;
    }
}
