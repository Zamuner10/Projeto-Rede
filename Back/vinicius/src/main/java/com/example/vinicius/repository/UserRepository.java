package com.example.vinicius.repository;


import com.example.vinicius.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail (String email);

    @Query("SELECT u FROM User u LEFT JOIN u.profile p WHERE u.email = :login OR p.nickname = :login")
    Optional<User> findByEmailOrNickname(@Param("login") String login);

}
