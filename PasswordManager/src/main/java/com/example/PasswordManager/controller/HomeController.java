package com.example.PasswordManager.controller;

import com.example.PasswordManager.entity.User;
import com.example.PasswordManager.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index()
    {
        return "index";
    }

    @GetMapping("/register")
    public String register()
    {
        return "register";
    }

    @GetMapping("/signin")
    public String signin()
    {
        return "signin";
    }

    @GetMapping("/user/home")
    public String home()
    {
        return "home";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute User user, HttpSession session, Model m) {
        boolean alreadyEmailExists= userService.checkEmail(user.getEmail());
        if(alreadyEmailExists)
        {
            session.setAttribute("msg", "Email already in use!");
            return "redirect:/register"; // Redirect back to the registration page
        }

        boolean alreadyNumberExists= userService.checkMobile(user.getMobileNo());
        if(alreadyNumberExists)
        {
            session.setAttribute("msg", "Mobile Number already in use!");
            return "redirect:/register"; // Redirect back to the registration page
        }



        if (!user.getPassword().equals(user.getConfirmPassword())) {
            // Password and confirm password do not match
            session.setAttribute("msg", "Password does not match");
            return "redirect:/register"; // Redirect back to the registration page
        }

        User u = userService.saveUser(user);

        if (u != null) {
            session.setAttribute("msg", "Registration saved successfully");
            return "redirect:/user/profile";
        } else {
            session.setAttribute("msg", "Something went wrong! User info not saved.");
            return "redirect:/register";
        }

    }


}
