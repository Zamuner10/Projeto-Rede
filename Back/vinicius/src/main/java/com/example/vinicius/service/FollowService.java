package com.example.vinicius.service;

import com.example.vinicius.dto.UserSummaryDTO;
import com.example.vinicius.entity.Follow;
import com.example.vinicius.entity.User;
import com.example.vinicius.exception.AlreadyFollowingException;
import com.example.vinicius.exception.NotFollowingException;
import com.example.vinicius.exception.UserNotFoundException;
import com.example.vinicius.repository.FollowRepository;
import com.example.vinicius.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public FollowService(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void followUser(User follower, Long targetUserId) {
        if (follower.getId().equals(targetUserId)) {
            throw new IllegalArgumentException("Você não pode seguir a si mesmo.");
        }

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com id: " + targetUserId));

        if (followRepository.existsByFollowerAndFollowing(follower, targetUser)) {
            throw new AlreadyFollowingException("Você já segue este usuário.");
        }

        Follow follow = new Follow(follower, targetUser);
        followRepository.save(follow);
    }

    @Transactional
    public void unfollowUser(User follower, Long targetUserId) {
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com id: " + targetUserId));

        Follow follow = followRepository.findByFollowerAndFollowing(follower, targetUser)
                .orElseThrow(() -> new NotFollowingException("Você não segue este usuário."));

        followRepository.delete(follow);
    }

    @Transactional(readOnly = true)
    public long getFollowersCount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com id: " + userId));
        // Para saber os SEGUIDORES do usuário, contamos onde ele é o SEGUIDO (following)
        return followRepository.countByFollowing(user);
    }

    @Transactional(readOnly = true)
    public long getFollowingCount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com id: " + userId));
        // Para saber quem o usuário SEGUE, contamos onde ele é o SEGUIDOR (follower)
        return followRepository.countByFollower(user);
    }

    @Transactional(readOnly = true)
    public List<UserSummaryDTO> getFollowers(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com id: " + userId));
        return followRepository.findByFollowing(user).stream()
                .map(follow -> {
                    User f = follow.getFollower();

                    String nickname = f.getProfile() != null ? f.getProfile().getNickname() : f.getName();
                    String photo = f.getProfile() != null ? f.getProfile().getPhotoUrl() : null;
                    return new UserSummaryDTO(f.getId(), nickname, photo);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserSummaryDTO> getFollowing(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com o id: " + userId));
        return followRepository.findByFollower(user).stream()
                .map(follow -> {
                    User f = follow.getFollowing();

                    String nickname = f.getProfile() != null ? f.getProfile().getNickname() : f.getName();
                    String photo = f.getProfile() != null ? f.getProfile().getPhotoUrl() : null;
                    return new UserSummaryDTO(f.getId(), nickname, photo);
                })
                .collect(Collectors.toList());
    }
}