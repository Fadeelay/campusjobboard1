/**
 * Spring Data JPA repository for JobApplication entities.
 * Provides operations to query applications by student or job and
 * to check if a student has already applied to a given job.
 */

package com.example.campusjobboard.repository;
import com.example.campusjobboard.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByStudent(User student);
    List<JobApplication> findByJob(Job job);
    boolean existsByJobAndStudent(Job job, User student);
}
