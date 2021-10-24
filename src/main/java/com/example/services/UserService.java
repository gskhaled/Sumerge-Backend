package com.example.services;

import com.example.repositories.UserRepository;
import com.example.tables.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public User signUp(User user) {
        return userRepository.save(user);
    }

    public String signIn(String email, String password) {
        User user = userRepository.findById(email).orElse(null);
        if (user != null) {
            if (user.getPassword().equals(password))
                return "OK";
            else
                return "Wrong Password";
        }
        return "Error";
    }
}
