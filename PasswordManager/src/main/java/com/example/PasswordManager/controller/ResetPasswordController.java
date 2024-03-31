package com.example.PasswordManager.controller;

import com.example.PasswordManager.entity.PasswordResetToken;
import com.example.PasswordManager.entity.User;
import com.example.PasswordManager.repository.TokenRepo;
import com.example.PasswordManager.repository.UserRepo;
import com.example.PasswordManager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ResetPasswordController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TokenRepo tokenRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/forgotPassword")
    public String forgotPassword() {
        return "forgotPassword";
    }

    @PostMapping("/forgotPassword")
    public String forgotPasswordProcess(@RequestParam("email") String email) {
        String output = "";
        User user = userRepo.findByEmail(email);
        if (user != null) {
            output = userService.sendEmail(user);
        }
        if (output.equals("success")) {
            return "redirect:/register?success";
        }
        return "redirect:/signin";
    }

    @GetMapping("/resetPassword/{token}")
    public String resetPasswordForm(@PathVariable String token, Model model) {
        PasswordResetToken reset = tokenRepo.findByToken(token);
        if (reset != null && userService.hasExipred(reset.getExpiryDateTime())) {
            model.addAttribute("email", reset.getUser().getEmail());
            return "resetPassword";
        }
        return "redirect:/forgotPassword?error";
    }

    @PostMapping("/resetPassword")
    public String passwordResetProcess(@RequestParam("email") String email, @RequestParam("password") String password) {
        User user = userRepo.findByEmail(email);
        if(user != null) {
            user.setPassword(passwordEncoder.encode(password));
            userRepo.save(user);
        }
        return "redirect:/signin";
    }


}
