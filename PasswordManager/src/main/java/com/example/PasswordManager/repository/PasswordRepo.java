package com.example.PasswordManager.repository;

import com.example.PasswordManager.entity.Password;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordRepo extends JpaRepository<Password, Integer> {
    List<Password> findAllByUserEmail(String email);
}
