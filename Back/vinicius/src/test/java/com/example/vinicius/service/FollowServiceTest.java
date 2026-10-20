package com.example.vinicius.service;


import com.example.vinicius.entity.Follow;
import com.example.vinicius.entity.Profile;
import com.example.vinicius.entity.User;
import com.example.vinicius.enums.FollowStatus;
import com.example.vinicius.enums.NotificationType;
import com.example.vinicius.exception.AlreadyFollowingException;
import com.example.vinicius.exception.NotFollowingException;
import com.example.vinicius.repository.FollowRepository;
import com.example.vinicius.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FollowServiceTest {

    @Mock
    private FollowRepository followRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private FollowService followService;


    @Test
    @DisplayName("Deve lançar exceção ao tentar seguir a si mesmo")
    public void shouldThrowExceptionUserTriesToFollowHimself(){
        User user = new User();
        user.setId(1L);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> followService.followUser(user, 1L)
        );
        assertEquals("Você não pode seguir a si mesmo.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar seguir de volta um usuário que já segue")
    public void shouldThrowExceptionWhenAlreadyFollowing(){
        User follower = new User();
        follower.setId(1L);

        User targetUser = new User();
        targetUser.setId(2L);

        when(userRepository.findById(2L)).thenReturn(Optional.of(targetUser));

        when(followRepository.existsByFollowerAndFollowing(follower, targetUser)).thenReturn(true);

        AlreadyFollowingException exception = assertThrows(
                AlreadyFollowingException.class,
                () -> followService.followUser(follower, 2L)
        );
        assertEquals("Você já segue este usuário.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deixar de seguir quem não segue")
    public void shouldThrowExceptionWhenUnfollowingUserNotFollowed(){
        User follower = new User();
        follower.setId(1L);

        User targetUser = new User();
        targetUser.setId(2L);

        when(userRepository.findById(2L)).thenReturn(Optional.of(targetUser));
        when(followRepository.findByFollowerAndFollowing(follower, targetUser)).thenReturn(Optional.empty());

        NotFollowingException exception = assertThrows(
                NotFollowingException.class,
                () -> followService.unfollowUser(follower, 2L)
        );

        assertEquals("Você não segue este usuário.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve criar solicitação com status PENDING ao seguir perfil privado")
    public void shouldCreatePendingFollowingTargetIsPrivate(){
        User follower = new User();
        follower.setId(1L);

        User targetUser = new User();
        targetUser.setId(2L);

        Profile privateProfile = new Profile();
        privateProfile.setPrivate(true);
        targetUser.setProfile(privateProfile);

        when(userRepository.findById(2L)).thenReturn(Optional.of(targetUser));
        when(followRepository.existsByFollowerAndFollowing(follower, targetUser)).thenReturn(false);

        ArgumentCaptor<Follow> followCaptor = ArgumentCaptor.forClass(Follow.class);

        followService.followUser(follower, 2L);

        verify(followRepository).save(followCaptor.capture());
        Follow savedFollow = followCaptor.getValue();

        assertEquals(FollowStatus.PENDING, savedFollow.getStatus());

        verify(notificationService).createNotification(targetUser, follower, NotificationType.FOLLOW_REQUEST);
    }

    @Test
    @DisplayName("Deve criar relacionamento com status ACCEPTED ao seguir perfil público")
    public void shouldCreateAcceptedFollowingTargetIsPublic(){

        User follower = new User();
        follower.setId(1L);

        User targetUser = new User();
        targetUser.setId(2L);

        Profile publicProfile = new Profile();
        publicProfile.setPrivate(false);
        targetUser.setProfile(publicProfile);

        when(userRepository.findById(2L)).thenReturn(Optional.of(targetUser));
        when(followRepository.existsByFollowerAndFollowing(follower, targetUser)).thenReturn(false);

        ArgumentCaptor<Follow> followCaptor = ArgumentCaptor.forClass(Follow.class);

        followService.followUser(follower, 2L);

        verify(followRepository).save(followCaptor.capture());
        Follow savedFollow = followCaptor.getValue();

        assertEquals(FollowStatus.ACCEPTED, savedFollow.getStatus());

        verify(notificationService).createNotification(targetUser, follower, NotificationType.FOLLOW);

    }
}
