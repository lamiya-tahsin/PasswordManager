package com.example.PasswordManager.controller;

import com.example.PasswordManager.entity.Password;
import com.example.PasswordManager.entity.User;
import com.example.PasswordManager.repository.UserRepo;
import com.example.PasswordManager.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class PasswordController {

    private final PasswordService passwordService;

    @Autowired
    private UserRepo userRepo;

    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @GetMapping("/user/passwords")
    public String profile(Principal principal, Model model) {
        // Get the current user's email
        String email = principal.getName();

        // Retrieve the user information
        User user = userRepo.findByEmail(email);
        model.addAttribute("user", user);

        // Retrieve passwords for the current user
        model.addAttribute("passwords", passwordService.getPasswordsForCurrentUser());

        return "passwords";
    }

    @GetMapping("/user/passwords/new")
    public String createNewPasswordEntry(Model model)
    {
        Password password=new Password();
        model.addAttribute("password",password);
        return "passwordEntryForm";
    }

    @PostMapping("/user/passwords")
    public String savePasswordEntry(@ModelAttribute("password")Password password)
    {
        passwordService.savePasswordEntry(password);


    }






}
