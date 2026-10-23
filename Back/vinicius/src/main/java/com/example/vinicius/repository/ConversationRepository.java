package com.example.vinicius.repository;

import com.example.vinicius.entity.Conversation;
import com.example.vinicius.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Optional<Conversation> findByUser1AndUser2OrUser1AndUser2(User user1, User use2, User userAgain, User userAgain2);
    List<Conversation> findByUser1OrUser2(User user1, User use2);
}
