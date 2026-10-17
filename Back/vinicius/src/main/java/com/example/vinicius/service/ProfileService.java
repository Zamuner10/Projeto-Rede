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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    public ProfileService(ProfileRepository profileRepository,
                          UserRepository userRepository,
                          FollowRepository followRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.followRepository = followRepository;
    }

    @Transactional(readOnly = true)
    public ProfileResponseDTO getProfile(Long targetUserId, User loggedUser) {
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado."));

        boolean isSelf = targetUser.getId().equals(loggedUser.getId());
        boolean isPrivate = targetUser.getProfile() != null && targetUser.getProfile().isPrivate();

        if (isPrivate && !isSelf) {
            boolean isFollowing = followRepository.existByFollowerAndFollowingAndStatus(
                    loggedUser, targetUser, FollowStatus.ACCEPTED
            );
            if (!isFollowing) {
                throw new PrivateProfileAccessException("Este perfil é privado. Siga o usuário para ver suas informações.");
            }
        }

        return buildProfileResponseDTO(targetUser);
    }

    public Profile completeProfile(User user, ProfileRequestDTO dto) {
        validateBio(dto.bio());
        Profile profile = profileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Perfil não encontrado"));

        profile.setBio(dto.bio());
        profile.setPhotoUrl(dto.photoUrl());

        return profileRepository.save(profile);
    }

    private void validateBio(String bio) {
        if (bio != null && bio.length() > 50) {
            throw new IllegalArgumentException("A biografia não pode passar de 50 caracteres");
        }
    }

    private ProfileResponseDTO buildProfileResponseDTO(User user) {
        Profile profile = user.getProfile();
        String nickname = profile != null ? profile.getNickname() : user.getName();
        String photoUrl = profile != null ? profile.getPhotoUrl() : null;
        String bio = profile != null ? profile.getBio() : null;
        boolean isPrivate = profile != null && profile.isPrivate();

        return new ProfileResponseDTO(user.getId(), nickname, photoUrl, bio, isPrivate);

    }
}