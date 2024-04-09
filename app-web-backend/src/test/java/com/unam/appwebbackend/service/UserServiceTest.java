package com.unam.appwebbackend.service;

import com.unam.appwebbackend.dao.UserRepository;
import com.unam.appwebbackend.entity.User;
import com.unam.appwebbackend.utils.PasswordUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authenticateUserWithCorrectCredentials() {
        String email = "user3@example.com";
        String password = "user3";
        User user = new User();
        user.setEmail(email);
        user.setPassword(PasswordUtils.hashPassword(password));

        when(userRepository.findByEmail(email)).thenReturn(user);

        assertTrue(userService.authenticateUser(email, password));
    }

    @Test
    void authenticateUserWithIncorrectCredentials() {
        String email = "user3@example.com";
        String password = "user3";
        User user = new User();
        user.setEmail(email);
        user.setPassword(PasswordUtils.hashPassword(password));

        when(userRepository.findByEmail(email)).thenReturn(user);

        assertFalse(userService.authenticateUser(email, "wrongpassword"));
    }
}
