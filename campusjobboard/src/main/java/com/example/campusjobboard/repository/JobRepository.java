package com.example.campusjobboard.repository;

import com.example.campusjobboard.model.Job;
import com.example.campusjobboard.model.User;
import com.example.campusjobboard.enums.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {
    List<Job> findByStatus(JobStatus status);
    List<Job> findByEmployer(User employer);
}
