package com.example.PasswordManager.service;

import com.example.PasswordManager.entity.Password;
import com.example.PasswordManager.repository.PasswordRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PasswordServiceImpl implements PasswordService {

    private final PasswordRepo passwordRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public PasswordServiceImpl(PasswordRepo passwordRepo) {
        this.passwordRepo = passwordRepo;
    }

    @Override
    public List<Password> getPasswordsForCurrentUser() {
        // Retrieve the currently authenticated user's email
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // Retrieve passwords specific to the current user
        return passwordRepo.findAllByUserEmail(currentUserEmail);
    }

    @Override
    public Password savePasswordEntry(Password password){
        String managedPassword=passwordEncoder.encode(password.getPassword());
        password.setPassword(managedPassword);
        return passwordRepo.save(password);
    }
}
