package com.example.PasswordManager.controller;

import com.example.PasswordManager.dto.PasswordDTO;
import com.example.PasswordManager.entity.Password;
import com.example.PasswordManager.entity.User;
import com.example.PasswordManager.repository.UserRepo;
import com.example.PasswordManager.service.PasswordService;
import com.example.PasswordManager.util.EncryptionUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PasswordController {

    private final PasswordService passwordService;

    @Autowired
    private UserRepo userRepo;

    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

//    @GetMapping("/user/passwords")
//    public String passwords(Principal principal, Model model) {
//        // Get the current user's email
//        String email = principal.getName();
////        System.out.println("PasswordController e " +email);
//        // Retrieve the user information
//        User user = userRepo.findByEmail(email);
//        model.addAttribute("user", user);
////        System.out.println("PasswordController e ami k" +user);
//        // Retrieve passwords for the current user
//        model.addAttribute("passwords", passwordService.getPasswordsForCurrentUser());
//        // Decrypt passwords
//        passwordService.getPasswordsForCurrentUser().forEach(password -> password.setSitePassword(EncryptionUtils.decrypt(password.getSitePassword())));
//
//        return "passwords";
//    }

    @GetMapping("/user/passwords")
    public String passwords(Principal principal, Model model) {
        String email = principal.getName();
        User user = userRepo.findByEmail(email);
        model.addAttribute("user", user);

        // Retrieve passwords in encrypted form
        List<Password> encryptedPasswords = passwordService.getPasswordsForCurrentUser();

        // Decrypt passwords and store in DTOs
        List<PasswordDTO> passwordDTOs = new ArrayList<>();
        for (Password password : encryptedPasswords) {
            String decryptedPassword = EncryptionUtils.decrypt(password.getSitePassword());
            PasswordDTO passwordDTO = new PasswordDTO();
            passwordDTO.setId(password.getId());  // Pass the id
            passwordDTO.setSiteName(password.getSiteName());
            passwordDTO.setURL(password.getURL());
            passwordDTO.setUserName(password.getUserName());
            passwordDTO.setDecryptedPassword(decryptedPassword);
            passwordDTOs.add(passwordDTO);
        }

        model.addAttribute("passwords", passwordDTOs);

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

    @GetMapping("/user/passwords/edit/{id}")
    public String editPasswordForm(@PathVariable Integer id, Model model){
        model.addAttribute("password",passwordService.getPasswordById(id));
        return "updatePassword";
    }

    @PostMapping("/user/passwords/{id}")
    public String updatePassword(@PathVariable Integer id,
        @ModelAttribute("password") Password password, Model model){

        //retriev password entry from password by ID
        Password existingPassword=passwordService.getPasswordById(id);
        existingPassword.setSiteName(password.getSiteName());
        existingPassword.setURL(password.getURL());
        existingPassword.setUserName(password.getUserName());
        existingPassword.setSitePassword(password.getSitePassword());
//        existingPassword.getSitePassword();
//
        //save updated password object
        passwordService.updatePassword(existingPassword);
        return "redirect:/user/passwords";


    }

    @GetMapping("/user/passwords/{id}")
    public String deletePassword(@PathVariable Integer id)  {
        passwordService.deletePasswordById(id);
        return "redirect:/user/passwords";
    }










}
