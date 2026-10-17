package com.example.vinicius.service;

import com.example.vinicius.dto.UserSummaryDTO;
import com.example.vinicius.entity.Follow;
import com.example.vinicius.entity.User;
import com.example.vinicius.enums.FollowStatus;
import com.example.vinicius.enums.NotificationType;
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
    private final NotificationService notificationService;

    public FollowService(FollowRepository followRepository, UserRepository userRepository, NotificationService notificationService) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
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

        boolean isPrivate = targetUser.getProfile() != null && targetUser.getProfile().isPrivate();

        if(isPrivate){
            follow.setStatus(FollowStatus.PENDING);
            notificationService.createNotification(targetUser, follower, NotificationType.FOLLOW_REQUEST);
        }else{
            follow.setStatus(FollowStatus.ACCEPTED);
        }
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
        return followRepository.countByFollowerAndStatus(user, FollowStatus.ACCEPTED);
    }

    @Transactional(readOnly = true)
    public long getFollowingCount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com id: " + userId));
        // Para saber quem o usuário SEGUE, contamos onde ele é o SEGUIDOR (follower)
        return followRepository.countByFollowingAndStatus(user, FollowStatus.ACCEPTED);
    }

    @Transactional(readOnly = true)
    public List<UserSummaryDTO> getFollowers(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com id:" + userId));
        return followRepository.findByFollowerAndStatus(user, FollowStatus.ACCEPTED).stream()
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
        return followRepository.findByFollowingAndStatus(user,  FollowStatus.ACCEPTED).stream()
                .map(follow -> {
                    User f = follow.getFollowing();

                    String nickname = f.getProfile() != null ? f.getProfile().getNickname() : f.getName();
                    String photo = f.getProfile() != null ? f.getProfile().getPhotoUrl() : null;
                    return new UserSummaryDTO(f.getId(), nickname, photo);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void acceptFollowRequest(User targetUser, Long requesterId) {
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com id: " + requesterId));
        Follow follow = followRepository.findByFollowerAndFollowing(requester, targetUser)
                .orElseThrow(() -> new NotFollowingException("Solicitação de follow não encotrada."));

        follow.setStatus(FollowStatus.ACCEPTED);
        followRepository.save(follow);

        notificationService.createNotification(requester, targetUser, NotificationType.FOLLOW_ACCEPTED);
    }

    @Transactional
    public void rejectFollowRequest(User targetUser, Long requesterId) {
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com id: " + requesterId));
        Follow follow = followRepository.findByFollowerAndFollowing(requester, targetUser)
                .orElseThrow(() -> new NotFollowingException("Solicitação de follow não econtrada."));

        followRepository.delete(follow);
    }

    @Transactional(readOnly = true)
    public List<UserSummaryDTO> getPendingRequests(User targertUser){
        return followRepository.findByFollowingAndStatus(targertUser, FollowStatus.PENDING).stream()
                .map(follow ->{
                    User f = follow.getFollower();
                    String nickname = f.getProfile() != null ? f.getProfile().getNickname() : f.getName();
                    String photo = f.getProfile() != null ? f.getProfile().getPhotoUrl() : null;

                    return new UserSummaryDTO(f.getId(), nickname, photo);
                })
                .collect(Collectors.toList());
    }
}