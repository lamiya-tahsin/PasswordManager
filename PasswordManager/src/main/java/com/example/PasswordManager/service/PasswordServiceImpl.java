package com.example.PasswordManager.service;

import com.example.PasswordManager.entity.Password;
import com.example.PasswordManager.repository.PasswordRepo;
import com.example.PasswordManager.util.EncryptionUtils;
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
//        String managedPassword=passwordEncoder.encode(password.getSitePassword());
//        password.setSitePassword(managedPassword);
//        return passwordRepo.save(password);
        String encryptedPassword = EncryptionUtils.encrypt(password.getSitePassword());
        password.setSitePassword(encryptedPassword);
        return passwordRepo.save(password);
    }

//    @Override
//    public Password getPasswordById(Integer id) {
////        return passwordRepo.findById(id).get();
//        Password password = passwordRepo.findById(id).get();
//        if (password != null) {
//            password.setSitePassword(EncryptionUtils.decrypt(password.getSitePassword()));
//        }
//        return password;
//    }

    @Override
    public Password getPasswordById(Integer id) {
        return passwordRepo.findById(id).get();
    }


    @Override
    public Password updatePassword(Password password) {
        String updatedPassword=EncryptionUtils.encrypt(password.getSitePassword());
        password.setSitePassword(updatedPassword);
        return passwordRepo.save(password);
    }

    @Override
    public void deletePasswordById(Integer id) {
        passwordRepo.deleteById(id);
    }
}
