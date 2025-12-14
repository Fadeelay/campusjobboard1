package com.example.campusjobboard.service;



import com.example.campusjobboard.enums.Role;
import com.example.campusjobboard.enums.Status;
import com.example.campusjobboard.exception.UserNotFoundException;
import com.example.campusjobboard.model.User;
import com.example.campusjobboard.repository.UserRepository;
import com.example.campusjobboard.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_shouldEncodePasswordAndSetDefaults() {
        User user = new User();
        user.setEmail("test@student.com");
        user.setPassword("plain");
        when(passwordEncoder.encode("plain")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User saved = userService.registerUser(user, Role.STUDENT);

        assertEquals(Role.STUDENT, saved.getRole());
        assertEquals(Status.ACTIVE, saved.getStatus());
        assertEquals("encoded", saved.getPassword());
        verify(userRepository).save(saved);
    }

    @Test
    void findByEmail_whenNotFound_shouldThrowException() {
        when(userRepository.findByEmail("missing@test.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.findByEmail("missing@test.com"));
    }
}

