package com.example.PasswordManager.service;

import com.example.PasswordManager.entity.User;
import com.example.PasswordManager.repository.UserRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

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



}