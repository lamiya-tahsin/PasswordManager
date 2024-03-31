package com.example.PasswordManager.service;

import com.example.PasswordManager.entity.User;

import java.time.LocalDateTime;

public interface UserService {

    public User saveUser(User user);

    public void removeSessionMessage();

    public boolean checkEmail(String email);

    public boolean checkMobile(String mobile);

    public boolean isValidPassword(String password);

    public String sendEmail(User user);

    boolean hasExipred(LocalDateTime expiryDateTime);
}