package com.example.PasswordManager.repository;


import com.example.PasswordManager.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepo extends JpaRepository<PasswordResetToken, Integer> {

    PasswordResetToken findByToken(String token);

}