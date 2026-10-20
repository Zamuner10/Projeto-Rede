package com.example.vinicius.service;

import com.example.vinicius.dto.PostResponseDTO;
import com.example.vinicius.entity.Post;
import com.example.vinicius.entity.Profile;
import com.example.vinicius.entity.User;
import com.example.vinicius.enums.FollowStatus;
import com.example.vinicius.exception.PrivateProfileAccessException;
import com.example.vinicius.repository.FollowRepository;
import com.example.vinicius.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private FollowRepository followRepository;

    @InjectMocks
    private PostService postService;

    @Test
    @DisplayName("Dono do post deve conseguir vizualizar o próprio post mesmo se o perfil for privado ")
    public void ownerShouldBeAbleToViewOwnPost() {

        User author = new User();
        author.setId(1L);

        Profile privateProfile = new Profile();
        privateProfile.setPrivate(true);
        author.setProfile(privateProfile);

        Post post = new Post();
        post.setId(10L);
        post.setCaption("Post de teste do dono");
        post.setUser(author);

        when(postRepository.findById(10L)).thenReturn(Optional.of(post));

        PostResponseDTO result = postService.getPostById(author, 10L);

        assertNotNull(result);
        assertEquals(10L, result.id());
        assertEquals("Post de teste do dono", result.caption());

        verifyNoInteractions(followRepository);
    }

    @Test
    @DisplayName("Deve lançar uma exceção ao tentar ver post de perfil privado sem seguir com status ACCEPTED")
    public void shouldThrowExceptionWhenViewingPrivateWithoutFollowing() {

        User author = new User();
        author.setId(1L);

        Profile privateProfile = new Profile();
        privateProfile.setPrivate(true);
        author.setProfile(privateProfile);

        Post post = new Post();
        post.setId(10L);
        post.setUser(author);

        User loggerUser = new User();
        loggerUser.setId(2L);

        when(postRepository.findById(10L)).thenReturn(Optional.of(post));
        when(followRepository.existsByFollowerAndFollowingAndStatus(loggerUser, author, FollowStatus.ACCEPTED)).thenReturn(false);
        PrivateProfileAccessException exception = assertThrows(
                PrivateProfileAccessException.class,
                () -> postService.getPostById(loggerUser, 10L)
        );

        assertNotNull(exception);
    }

    @Test
    @DisplayName("Qualquer usuário deve conseguir ver post de perfil público")
    public void shouldAllowAnyoneToViewPublicPost() {
        User author = new User();
        author.setId(1L);

        Profile publicProfile = new Profile();
        publicProfile.setPrivate(false);
        author.setProfile(publicProfile);

        Post post = new Post();
        post.setId(10L);
        post.setUser(author);

        User loggerUser = new User();
        loggerUser.setId(2L);

        when(postRepository.findById(10L)).thenReturn(Optional.of(post));

        PostResponseDTO result = postService.getPostById(loggerUser, 10L);

        assertNotNull(result);
        assertEquals(10L, result.id());
        verifyNoInteractions(followRepository);
    }

    @Test
    @DisplayName("Seguidor com status ACCEPTED deve conseguir ver post de perfil privado")
    public void shouldAllowAcceptedFollowerToViewPrivatePost(){
        User author = new User();
        author.setId(1L);

        Profile privateProfile = new Profile();
        privateProfile.setPrivate(true);
        author.setProfile(privateProfile);

        Post post = new Post();
        post.setId(10L);
        post.setUser(author);

        User loggerUser = new User();
        loggerUser.setId(2L);

        when(postRepository.findById(10L)).thenReturn(Optional.of(post));
        when(followRepository.existsByFollowerAndFollowingAndStatus(loggerUser, author, FollowStatus.ACCEPTED)).thenReturn(true);

        PostResponseDTO result = postService.getPostById(loggerUser, 10L);

        assertNotNull(result);
        assertEquals(10L, result.id());
    }

}
