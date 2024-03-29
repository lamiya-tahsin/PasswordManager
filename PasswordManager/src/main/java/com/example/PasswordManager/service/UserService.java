package com.example.PasswordManager.service;

import com.example.PasswordManager.entity.User;

public interface UserService {

    public User saveUser(User user);

    public void removeSessionMessage();

    public boolean checkEmail(String email);

    public boolean checkMobile(String mobile);

}