package com.example.PasswordManager.controller;

import com.example.PasswordManager.entity.Password;
import com.example.PasswordManager.entity.User;
import com.example.PasswordManager.repository.UserRepo;
import com.example.PasswordManager.service.PasswordService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String passwords(Principal principal, Model model) {
        // Get the current user's email
        String email = principal.getName();
//        System.out.println("PasswordController e " +email);
        // Retrieve the user information
        User user = userRepo.findByEmail(email);
        model.addAttribute("user", user);
//        System.out.println("PasswordController e ami k" +user);
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

    @PostMapping("/user/savePasswordEntry")
    public String savePasswordEntry(@ModelAttribute("password") Password password, HttpSession session, Principal p) {
        password.setUserEmail(p.getName()); // Set the userEmail

        Password savedPassword = passwordService.savePasswordEntry(password);

        if (savedPassword != null) {
            session.setAttribute("msg", "Password entry saved successfully");
            return "redirect:/user/passwords";
        } else {
            session.setAttribute("msg", "Something went wrong! Password entry not saved.");
            return "redirect:/user/passwords/new";
        }
    }






}
