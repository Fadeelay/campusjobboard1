/**
 * Service interface for user-related business operations.
 * Defines methods for registering users, finding users, and
 * activating or deactivating accounts.
 */

package com.example.campusjobboard.service;

import com.example.campusjobboard.model.User;
import com.example.campusjobboard.enums.Role;

import java.util.List;

public interface UserService {

    User registerUser(User user, Role role);

    User findById(Long id);

    User findByEmail(String email);

    List<User> findAll();

    void activateUser(Long userId);

    void deactivateUser(Long userId);
}
