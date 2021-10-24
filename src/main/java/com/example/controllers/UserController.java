package com.example.controllers;

import com.example.services.UserService;
import com.example.tables.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.POST, value = "/users/signUp")
    public User signUp(@RequestBody User user) {
        System.out.println(user);
        return userService.signUp(user);
    }

    @RequestMapping("/users/signIn")
    public String signIn(@RequestBody SignInForm user) {
        return userService.signIn(user.email, user.password);
    }

    private static class SignInForm {
        String email;
        String password;

        public SignInForm(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }
}
