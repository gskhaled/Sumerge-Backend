package com.example.services;

import com.example.entities.User;
import com.example.repositories.UserRepository;
import com.example.security.AuthorizationRequest;
import com.example.security.AuthorizationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;

    public User signUp(User user) {
        if (userRepository.findById(user.getUsername()).orElse(null) == null)
            return userRepository.save(user);
        else
            throw new BadCredentialsException("User invalid");
    }

    public ResponseEntity<?> signIn(AuthorizationRequest form) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(form.getUsername(), form.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password");
        }
        final UserDetails userDetails = loadUserByUsername(form.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthorizationResponse(jwt));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findById(username).orElse(null);
        if (user == null)
            throw new UsernameNotFoundException(username);
        return user;
    }

    /*public String oldSignIn(String username, String password) {
        User user = userRepository.findById(username).orElse(null);
        if (user != null) {
            if (user.getPassword().equals(password))
                return "OK";
            else
                return "Wrong Password";
        }
        return "Error";
    }*/
}
