package com.example.services;

import com.example.entities.User;
import com.example.repositories.UserRepository;
import com.example.security.AuthorizationRequest;
import com.example.security.AuthorizationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.internal.matchers.Equality.areEqual;

class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtil jwtUtil;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signUp() {
        User user = new User();
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);
        assertEquals(user, userService.signUp(user));
    }

    @Test
    void signIn() throws Exception {
        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        )).thenAnswer(i -> i.getArguments()[0]);
        when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn("token");
        User user = new User();
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        AuthorizationRequest form = new AuthorizationRequest("test", "test");
        AuthorizationResponse response = new AuthorizationResponse("token");
        areEqual(response, userService.signIn(form).getBody());
    }

    @Test
    void loadUserByUsername() {
        User user = new User();
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        areEqual(user, userService.loadUserByUsername(""));
    }
}