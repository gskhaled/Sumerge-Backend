package com.example.controllers;

import com.example.entities.User;
import com.example.security.AuthorizationRequest;
import com.example.services.UserService;
import com.example.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService = new UserServiceImpl();

    @RequestMapping(method = RequestMethod.POST, value = "/users/signUp")
    public User signUp(@RequestBody User user) {
        return userService.signUp(user);
    }

    @RequestMapping("/users/signIn")
    public ResponseEntity<?> signIn(@RequestBody AuthorizationRequest form) throws Exception {
        return userService.signIn(form);
    }
}
