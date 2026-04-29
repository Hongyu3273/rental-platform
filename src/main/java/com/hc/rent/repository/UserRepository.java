package com.hc.rent.repository;

import com.hc.rent.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by email (for email login)
    Optional<User> findByEmail(String email);

    // Find user by phone (for phone login)
    Optional<User> findByPhone(String phone);

    // Check if email already exists (for registration)
    boolean existsByEmail(String email);

    // Check if phone already exists (for registration)
    boolean existsByPhone(String phone);
}