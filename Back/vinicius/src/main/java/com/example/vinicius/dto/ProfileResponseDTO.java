package com.example.vinicius.dto;

import com.example.vinicius.entity.Profile;

public record ProfileResponseDTO (
        Long id,
        String nickname,
        String photoUrl,
        String bio
){

    public ProfileResponseDTO(Profile profile){
        this(
                profile.getId(),
                profile.getNickname(),
                profile.getPhotoUrl(),
                profile.getBio());
    }
}
