package com.example.vinicius.controller;


import com.example.vinicius.dto.ProfileRequestDTO;
import com.example.vinicius.dto.ProfileResponseDTO;
import com.example.vinicius.entity.Profile;
import com.example.vinicius.entity.User;
import com.example.vinicius.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profiles")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {this.profileService = profileService;}

   @PutMapping
   public ResponseEntity<ProfileResponseDTO> completeProfile(
           @AuthenticationPrincipal User user,
           @Valid @RequestBody ProfileRequestDTO dto) {
       Profile completeProfile = profileService.completeProfile(user,dto);
       ProfileResponseDTO profileResponseDTO = new ProfileResponseDTO(completeProfile);
       return ResponseEntity.ok(profileResponseDTO);
   }
}
