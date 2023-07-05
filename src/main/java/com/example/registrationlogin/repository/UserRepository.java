package com.example.registrationlogin.repository;

import com.example.registrationlogin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByResetPasswordToken(String token);
}
