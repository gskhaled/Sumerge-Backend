package com.example.services;

import com.example.entities.User;
import com.example.security.AuthorizationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User signUp(User user);

    ResponseEntity<?> signIn(AuthorizationRequest form) throws Exception;
}
