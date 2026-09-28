package com.example.vinicius.repository;

import com.example.vinicius.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByNickname(String nickname);
    Optional<Profile> findByUserId(Long userId);
}
