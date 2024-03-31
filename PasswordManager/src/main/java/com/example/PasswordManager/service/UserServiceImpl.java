package com.example.PasswordManager.service;

import com.example.PasswordManager.entity.PasswordResetToken;
import com.example.PasswordManager.entity.User;
import com.example.PasswordManager.repository.TokenRepo;
import com.example.PasswordManager.repository.UserRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

@Autowired
    JavaMailSender javaMailSender;

    @Autowired
    TokenRepo tokenRepo;

    @Override
    public User saveUser(User user) {

        String password=passwordEncoder.encode(user.getPassword());
        String confirmPassword=passwordEncoder.encode(user.getConfirmPassword());
        user.setPassword(password);
        user.setConfirmPassword(confirmPassword);
        User newuser = userRepo.save(user);

        return newuser;
    }

    @Override
    public void removeSessionMessage() {

        HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest()
                .getSession();

        session.removeAttribute("msg");
    }

    @Override
    public boolean checkEmail(String email){
        return userRepo.existsByEmail(email);
    }

    @Override
    public boolean checkMobile(String mobile)
    {
        return userRepo.existsByMobileNo(mobile);
    }

    public boolean isValidPassword(String password) {
        // Password must be 8-20 characters long
        if (password.length() < 8 || password.length() > 20) {
            return false;
        }

        // Must contain at least one letter and one number
        boolean hasLetter = false;
        boolean hasNumber = false;
        for (char ch : password.toCharArray()) {
            if (Character.isLetter(ch)) {
                hasLetter = true;
            } else if (Character.isDigit(ch)) {
                hasNumber = true;
            }
        }
        if (!hasLetter || !hasNumber) {
            return false;
        }

        // Must not contain spaces
        if (!password.matches("[a-zA-Z0-9@/_]+")) {
            return false;
        }

        if (!password.matches(".*[@*_/].*")) {
            return false;
        }

        return true;
    }

    public String sendEmail(User user) {
        try {
            String resetLink = generateResetToken(user);

            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom("team5shei@gmail.com");
            msg.setTo(user.getEmail());

            msg.setSubject("Reset your your password for the Password Manager");
            msg.setText("Hello \n\n" + "Please click on this link to Reset your Password :" + resetLink + ". \n\n"
                    + "Regards \n" + "ABC");

            javaMailSender.send(msg);

            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }

    }

    public String generateResetToken(User user) {
        UUID uuid = UUID.randomUUID();
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime expiryDateTime = currentDateTime.plusMinutes(30);
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(user);
        resetToken.setToken(uuid.toString());
        resetToken.setExpiryDateTime(expiryDateTime);
        resetToken.setUser(user);
        PasswordResetToken token = tokenRepo.save(resetToken);
        if (token != null) {
            String endpointUrl = "http://localhost:8080/resetPassword";
            return endpointUrl + "/" + resetToken.getToken();
        }
        return "";
    }

    public boolean hasExipred(LocalDateTime expiryDateTime) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return expiryDateTime.isAfter(currentDateTime);
    }







}