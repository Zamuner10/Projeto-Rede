package com.example.vinicius.repository;

import com.example.vinicius.entity.Follow;
import com.example.vinicius.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow,Long> {

    boolean existsByFollowerAndFollowing(User follower, User following);

    Optional<Follow>findByFollowerAndFollowing(User follower, User following);

    long countByFollower(User follower);

    long countByFollowing(User following);

    List<Follow> findByFollower(User follower);

    List<Follow> findByFollowing(User following);
}

