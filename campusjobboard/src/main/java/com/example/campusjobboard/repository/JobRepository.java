package com.example.campusjobboard.repository;
/**
 * Spring Data JPA repository for Job entities.
 * Provides CRUD operations plus methods to query jobs by status and employer.
 */

import com.example.campusjobboard.model.*;
import com.example.campusjobboard.enums.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByStatus(JobStatus status);
    List<Job> findByEmployer(User employer);
}
