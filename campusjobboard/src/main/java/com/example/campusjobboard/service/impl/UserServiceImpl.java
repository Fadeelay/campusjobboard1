/**
 * Concrete implementation of UserService.
 * Handles user registration with password encryption, user lookups,
 * and account activation/deactivation using the UserRepository.
 */

package com.example.campusjobboard.service.impl;

import com.example.campusjobboard.enums.Role;
import com.example.campusjobboard.enums.Status;
import com.example.campusjobboard.exception.UserNotFoundException;
import com.example.campusjobboard.model.User;
import com.example.campusjobboard.repository.UserRepository;
import com.example.campusjobboard.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(User user, Role role) {
        user.setRole(role);
        user.setStatus(Status.ACTIVE);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void activateUser(Long userId) {
        User user = findById(userId);
        user.setStatus(Status.ACTIVE);
        userRepository.save(user);
    }

    @Override
    public void deactivateUser(Long userId) {
        User user = findById(userId);
        user.setStatus(Status.INACTIVE);
        userRepository.save(user);
    }
}
