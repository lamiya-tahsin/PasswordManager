package com.example.PasswordManager.controller;

import com.example.PasswordManager.entity.PasswordResetToken;
import com.example.PasswordManager.entity.User;
import com.example.PasswordManager.repository.UserRepo;
import com.example.PasswordManager.service.UserService;
import com.example.PasswordManager.util.PasswordGenerator;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/")
    public String index(){
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

        boolean validPassword=userService.isValidPassword(user.getPassword());
        if (!validPassword) {
            session.setAttribute("msg", "Password must be 8-20 characters long, contain letters and numbers, at least one special character from /,@,#_ and must not contain any spaces, or emoji.");
            return "redirect:/register"; // Redirect back to the registration page
        }

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            // Password and confirm password do not match
            session.setAttribute("msg", "Password does not match");
            return "redirect:/register"; // Redirect back to the registration page
        }

        User u = userService.saveUser(user);
//        System.out.println(u);

        if (u != null) {
            session.setAttribute("msg", "Registration saved successfully");
            return "redirect:/user/passwords";
        } else {
            session.setAttribute("msg", "Something went wrong! User info not saved.");
            return "redirect:/register";
        }

    }

    @GetMapping("/generatePassword")
    public String generatePassword(Model model, HttpSession session) {
        if(session.getAttribute("generatedPassword") == null) {
            String generatedPassword = PasswordGenerator.generatePassword(14);
            model.addAttribute("generatedPassword", generatedPassword);
            session.setAttribute("generatedPassword", generatedPassword);
        }
        System.out.println(session.getAttribute("generatedPassword"));
        return "index";
    }

    @GetMapping("/clearPassword")
    public String clearPassword(Model model, HttpSession session)
    {
        if (session.getAttribute("generatedPassword") != null)
        {
            session.removeAttribute("generatedPassword");
        }
        System.out.println(session.getAttribute("generatedPassword"));
        return "index";


    }





}
