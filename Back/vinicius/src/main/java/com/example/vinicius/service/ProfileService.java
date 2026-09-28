package com.example.vinicius.service;

import com.example.vinicius.dto.ProfileRequestDTO;
import com.example.vinicius.entity.Profile;
import com.example.vinicius.entity.User;
import com.example.vinicius.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository){
        this.profileRepository = profileRepository;
    }

    public Profile completeProfile(User user, ProfileRequestDTO dto){

        validateNickName(dto.nickname());
        validateNicknameAlreadyExist(dto.nickname());
        validateBio(dto.bio());

        Profile profile = profileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Perfil não encontrado"));

        profile.setNickname(dto.nickname());
        profile.setBio(dto.bio());
        profile.setPhotoUrl(dto.photoUrl());

        return profileRepository.save(profile);
    }
    private void validateNicknameAlreadyExist(String nickname){
        Optional<Profile> profile = profileRepository.findByNickname(nickname);
        if(profile.isPresent()){
            throw new IllegalArgumentException("Nickname já usado");
        }
    }

    private void validateNickName(String nickname){
        if(nickname == null || nickname.isBlank()){
            throw new IllegalArgumentException("Nickname não pode ficar vazio");
        }
        if (nickname.length() > 15){
            throw new IllegalArgumentException("Nickname não pode passar de 15 caracteres");
        }
    }
    private void validateBio(String bio){
        if (bio != null && bio.length()> 50){
            throw new IllegalArgumentException("A biografia não pode passar de 50 caracteres");
        }
    }
}
