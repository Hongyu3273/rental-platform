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

    // Find user by email and role
    Optional<User> findByEmailAndRole(String email, User.Role role);

    // Find user by phone and role
    Optional<User> findByPhoneAndRole(String phone, User.Role role);

    // Check if email + role combination already exists (for registration)
    boolean existsByEmailAndRole(String email, User.Role role);

    // Check if phone + role combination already exists (for registration)
    boolean existsByPhoneAndRole(String phone, User.Role role);
}