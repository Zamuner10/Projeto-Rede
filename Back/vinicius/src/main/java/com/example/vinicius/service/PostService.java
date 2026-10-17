package com.example.vinicius.service;

import com.example.vinicius.dto.PostRequestDTO;
import com.example.vinicius.dto.PostResponseDTO;
import com.example.vinicius.entity.Follow;
import com.example.vinicius.entity.Post;
import com.example.vinicius.entity.PostLike;
import com.example.vinicius.entity.User;
import com.example.vinicius.enums.FollowStatus;
import com.example.vinicius.exception.AlreadyLikeException;
import com.example.vinicius.exception.PostNotFoundException;
import com.example.vinicius.exception.PrivateProfileAccessException;
import com.example.vinicius.exception.UnauthorizedActionException;
import com.example.vinicius.repository.FollowRepository;
import com.example.vinicius.repository.PostLikeRepository;
import com.example.vinicius.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final FollowRepository followRepository;

    public PostService(PostRepository postRepository, PostLikeRepository postLikeRepository, FollowRepository followRepository) {
        this.postRepository = postRepository;
        this.postLikeRepository = postLikeRepository;
        this.followRepository = followRepository;
    }

    @Transactional(readOnly = true)
    public PostResponseDTO getPostById(User loggedUser, Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post não encontrado com o ID: " + id));

        User author = post.getUser();
        boolean isSelf = author.getId().equals(loggedUser.getId());
        boolean isPrivate = author.getProfile() != null && author.getProfile().isPrivate();

        if (isPrivate && !isSelf) {
            boolean isFollowing = followRepository.existsByFollowerAndFollowingAndStatus(
                    loggedUser, author, FollowStatus.ACCEPTED
            );
            if (!isFollowing) {
                throw new PrivateProfileAccessException("Este post pertence a um perfil privado.");
            }
        }
        return new PostResponseDTO(
                post.getId(),
                post.getCaption(),
                post.getMediaUrl(),
                post.getMediaType(),
                post.getCreatedAt(),
                author.getProfile() != null ? author.getProfile().getNickname() : author.getName(),
                author.getProfile() != null ? author.getProfile().getPhotoUrl() : null
        );
    }

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

    public List<PostResponseDTO>getFeed(User loggedUser){

        List<Follow> follows = followRepository.findByFollowerAndStatus(loggedUser, FollowStatus.ACCEPTED);
        List<User> authors= new ArrayList<>();

        for(Follow follow : follows){
            authors.add(follow.getFollowing());
        }
        authors.add(loggedUser);

        List<Post> posts = postRepository.findByUserInOrderByCreatedAtDesc(authors);
        if(posts.isEmpty()){
            posts = postRepository.findTrendingPosts();
        }

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

    public void deletePost(Long id, User user){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post não encotrado pelo ID: " + id));
        if (!post.getUser().getId().equals(user.getId())){
            throw new UnauthorizedActionException("Você não tem permissão para deletar este post!");
        }
        postRepository.delete(post);
    }

    public void likePost(Long postId, User user){
        Post post = postRepository.findById(postId).
                orElseThrow(() -> new PostNotFoundException("Post não encontrado"));

        if(postLikeRepository.existsByPostIdAndUserId(postId, user.getId())){
            throw new AlreadyLikeException("Post já curtido !");
        }
        PostLike like = new PostLike();
        like.setPost(post);
        like.setUser(user);
        like.setCreatedAt(LocalDateTime.now());

        postLikeRepository.save(like);
    }

    public void unlikePost(Long postId, User user){
        PostLike like = postLikeRepository.findByPostIdAndUserId(postId, user.getId())
                .orElseThrow(() -> new RuntimeException("Você não curtiu este post!"));
        postLikeRepository.delete(like);
    }


}