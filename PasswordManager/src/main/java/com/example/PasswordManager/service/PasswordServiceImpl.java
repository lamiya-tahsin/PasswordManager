package com.example.PasswordManager.service;

import com.example.PasswordManager.entity.Password;
import com.example.PasswordManager.repository.PasswordRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
    public void removeSessionMessage() {

        HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest()
                .getSession();

        session.removeAttribute("msg");
    }


    @Override
    public Password savePasswordEntry(Password password){
        String managedPassword=passwordEncoder.encode(password.getSitePassword());
        password.setSitePassword(managedPassword);
        return passwordRepo.save(password);
    }
}
