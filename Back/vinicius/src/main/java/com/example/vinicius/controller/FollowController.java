package com.example.vinicius.controller;

import com.example.vinicius.entity.User;
import com.example.vinicius.service.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class FollowController {
    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }


    @PostMapping("/{id}/follow")
    public ResponseEntity<Void> follow (@AuthenticationPrincipal User loggedUser, @PathVariable Long id){
        followService.followUser(loggedUser,id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/unfollow")
    public ResponseEntity<Void> unfollow(@AuthenticationPrincipal User loggedUser, @PathVariable Long id){
        followService.unfollowUser(loggedUser,id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/followers/count")
    public ResponseEntity<Long> getFollowsCount(@PathVariable Long id){
        long count = followService.getFollowersCount(id);
        return ResponseEntity.ok(count);
    }


    @GetMapping("/{id}/following/count")
    public ResponseEntity<Long> getFollowingCount(@PathVariable Long id){
        long count = followService.getFollowingCount(id);
        return ResponseEntity.ok(count);
    }
}
