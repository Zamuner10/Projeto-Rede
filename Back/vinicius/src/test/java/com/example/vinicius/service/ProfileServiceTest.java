package com.example.vinicius.service;

import com.example.vinicius.dto.ProfileRequestDTO;
import com.example.vinicius.dto.ProfileResponseDTO;
import com.example.vinicius.entity.Profile;
import com.example.vinicius.entity.User;
import com.example.vinicius.enums.FollowStatus;
import com.example.vinicius.exception.PrivateProfileAccessException;
import com.example.vinicius.exception.UserNotFoundException;
import com.example.vinicius.repository.FollowRepository;
import com.example.vinicius.repository.ProfileRepository;
import com.example.vinicius.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class ProfileServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private FollowRepository followRepository;

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ProfileService profileService;

    @Test
    @DisplayName("Deve lançar PrivateProfileAccessExceptions quando o perfil for privado e o usuário não for seguidor aceito")
    void getProfile_shouldThrowPrivateProfileAccessException_whenProfileIsPrivateAndNotFollowing() {
        User loggedUser = new User();
        loggedUser.setId(1L);

        Long targetUserId = 2L;

        Profile profile = new Profile();
        profile.setPrivate(true);

        User targetUser = new User();
        targetUser.setId(targetUserId);
        targetUser.setProfile(profile);

        when(userRepository.findById(targetUserId)).thenReturn(Optional.of(targetUser));
        when(followRepository.existsByFollowerAndFollowingAndStatus(loggedUser,targetUser, FollowStatus.ACCEPTED))
                .thenReturn(false);
        assertThrows(
                PrivateProfileAccessException.class,
                () -> profileService.getProfile(targetUserId, loggedUser)
        );
        verify(userRepository, times(1)).findById(targetUserId);
        verify(followRepository, times(1)).existsByFollowerAndFollowingAndStatus(loggedUser, targetUser, FollowStatus.ACCEPTED);
    }

    @Test
    @DisplayName("Deve retornar o perfil com sucesso quando for privado e o usuário for seguidor aceito")
    void getProfile_shouldReturnProfile_whenPrivateAndUserIsFollowing(){
        User loggedUser = new User();
        loggedUser.setId(1L);

        Long targetUserId = 2L;

        Profile profile = new Profile();
        profile.setPrivate(true);

        User targetUser = new User();
        targetUser.setId(targetUserId);
        targetUser.setProfile(profile);

        when(userRepository.findById(targetUserId)).thenReturn(Optional.of(targetUser));
        when(followRepository.existsByFollowerAndFollowingAndStatus(loggedUser, targetUser, FollowStatus.ACCEPTED))
                .thenReturn(true);
        ProfileResponseDTO response = profileService.getProfile(targetUserId, loggedUser);
        assertNotNull(response);
        verify(userRepository, times(1)).findById(targetUserId);
        verify(followRepository,times(1)).existsByFollowerAndFollowingAndStatus(loggedUser, targetUser, FollowStatus.ACCEPTED);
    }

    @Test
    @DisplayName("Deve lançar UserNotFoundException quando o usuário não existir")
    void getProfile_shouldThrowUserNotFoundException_whenUserDoesNotExist(){
        User loggedUser = new User();
        loggedUser.setId(1L);
        Long targetUserId = 99L;

        when(userRepository.findById(targetUserId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> profileService.getProfile(targetUserId, loggedUser));
        verify(userRepository, times(1)).findById(targetUserId);
        verify(followRepository, never()).existsByFollowerAndFollowingAndStatus(any(), any(), any());
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando a bio ultrapassar 50 caracteres")
    void completeProfile_shouldThrowIllegalArgumentException_whenBioExceeds50Chars() {
        User loggedUser = new User();
        loggedUser.setId(1L);

        String bioLonga = "A".repeat(51);
        ProfileRequestDTO dto = new ProfileRequestDTO(bioLonga, "https://foto.com/minhafoto.jpg");

        assertThrows(IllegalArgumentException.class, () -> profileService.completeProfile(loggedUser, dto));
        verify(profileRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando o perfil não for encontrado no banco")
    void completeProfile_shouldThrowIllegalArgumentException_whenProfileNotFound(){

        User loggedUser = new User();
        loggedUser.setId(1L);

        ProfileRequestDTO dto = new ProfileRequestDTO("Minha nova bio","https://foto.com/minhafoto.jpg");
        when(profileRepository.findByUserId(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> profileService.completeProfile(loggedUser, dto));

        verify(profileRepository, times(1)).findByUserId(1L);
        verify(profileRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve atualizar e salvar o perfil com sucesso quando os dados forem válidos")
    void completeProfile_shouldUpdateAndSaveProfile_whenDataIsValid() {
        User loggedUser = new User();
        loggedUser.setId(1L);

        Profile existingProfile = new Profile();
        existingProfile.setId(10L);
        existingProfile.setUser(loggedUser);

        ProfileRequestDTO dto = new ProfileRequestDTO("Bio atualizada","https://foto.com/minhafoto.jpg" );
        when(profileRepository.findByUserId(1L)).thenReturn(Optional.of(existingProfile));
        when(profileRepository.save(any(Profile.class))).thenAnswer(invocation ->  invocation.getArgument(0));

        Profile result = profileService.completeProfile(loggedUser, dto);
        assertNotNull(result);
        assertEquals("https://foto.com/minhafoto.jpg", result.getBio());
        assertEquals("Bio atualizada", result.getPhotoUrl());

        verify(profileRepository, times(1)).findByUserId(1L);
        verify(profileRepository, times(1)).save(any(Profile.class));
    }
}
