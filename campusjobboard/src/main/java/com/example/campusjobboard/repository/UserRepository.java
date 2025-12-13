package com.example.campusjobboard.repository;
/**
 * Spring Data JPA repository for User entities.
 * Provides CRUD operations and a finder method to look up users by email.
 */

import com.example.campusjobboard.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}