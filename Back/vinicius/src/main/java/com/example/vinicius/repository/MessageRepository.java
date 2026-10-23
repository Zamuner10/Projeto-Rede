package com.example.vinicius.repository;

import com.example.vinicius.entity.Conversation;
import com.example.vinicius.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {
    List<Message> findByConversationOrderByCreatedAtAsc(Conversation conversation);
}
