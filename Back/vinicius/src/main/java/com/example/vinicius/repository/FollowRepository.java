package com.example.vinicius.repository;

import com.example.vinicius.entity.Follow;
import com.example.vinicius.entity.User;
import com.example.vinicius.enums.FollowStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow,Long> {

    boolean existsByFollowerAndFollowing(User follower, User following);
    boolean existsByFollowerAndFollowingAndStatus(User follower, User following, FollowStatus status);

    Optional<Follow>findByFollowerAndFollowing(User follower, User following);

    long countByFollowerAndStatus(User follower, FollowStatus status);
    long countByFollowingAndStatus(User following, FollowStatus status);

    List<Follow> findByFollowerAndStatus(User follower, FollowStatus status);
    List<Follow> findByFollowingAndStatus(User following, FollowStatus status);
}

