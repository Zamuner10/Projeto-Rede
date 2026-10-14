package com.example.vinicius.repository;

import com.example.vinicius.entity.Post;
import com.example.vinicius.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Post> findAllByOrderByCreatedAtDesc();
    List<Post> findByUserInOrderByCreatedAtDesc(List<User> users);

    @Query("SELECT p FROM Post p " +
            "LEFT JOIN p.likes l " +
            "LEFT JOIN p.comments c " +
            "GROUP BY p " +
            "ORDER BY (COUNT(DISTINCT l) + COUNT(DISTINCT c)) DESC, p.createdAt DESC")
    List<Post> findTrendingPosts();
}
