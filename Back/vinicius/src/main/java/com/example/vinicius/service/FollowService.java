package com.example.vinicius.service;

import com.example.vinicius.entity.Follow;
import com.example.vinicius.entity.User;
import com.example.vinicius.repository.FollowRepository;
import org.springframework.transaction.annotation.Transactional;
import com.example.vinicius.repository.UserRepository;
import org.springframework.stereotype.Service;

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
                .orElseThrow(() -> new IllegalArgumentException(("Usuário não encotrado.")));

        if (followRepository.existsByFollowerAndFollowing(follower, targetUser)) {
            throw new IllegalArgumentException("Você já segue este usuário.");
        }

        Follow follow = new Follow(follower, targetUser);
        followRepository.save(follow);
    }

    @Transactional
    public void unfollowUser(User follower, Long targetUserId) {
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        Follow follow = followRepository.findByFollowerAndFollowing(follower, targetUser)
                .orElseThrow(() -> new IllegalArgumentException("Você não segue mais esté usuário"));

        followRepository.delete(follow);
    }

    @Transactional(readOnly = true)
    public long getFollowersCount(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
        return followRepository.countByFollower(user);
    }

    @Transactional(readOnly = true)
    public long getFollowingCount(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontraod."));
        return followRepository.countByFollowing(user);
    }

}


