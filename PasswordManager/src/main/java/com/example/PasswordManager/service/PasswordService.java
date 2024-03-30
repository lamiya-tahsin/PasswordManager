package com.example.PasswordManager.service;

import com.example.PasswordManager.entity.Password;

import java.util.List;

public interface PasswordService {
    List<Password> getPasswordsForCurrentUser();
    public Password savePasswordEntry(Password password);
    public void removeSessionMessage();
}
