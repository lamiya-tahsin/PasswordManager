package com.example.PasswordManager.repository;

import com.example.PasswordManager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer> {

    public User findByEmail(String email);
    public boolean existsByEmail(String email);

    public boolean existsByMobileNo(String mobile);

}