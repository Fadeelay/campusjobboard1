package com.example.campusjobboard.service;

import com.example.campusjobboard.enums.ApplicationStatus;
import com.example.campusjobboard.model.Job;
import com.example.campusjobboard.model.JobApplication;
import com.example.campusjobboard.model.User;

import java.util.List;

public interface ApplicationService {

    JobApplication applyToJob(Job job, User student, JobApplication formData);

    JobApplication findById(Long id);

    List<JobApplication> findApplicationsByStudent(User student);

    List<JobApplication> findApplicationsByJob(Job job);

    JobApplication updateApplicationStatus(Long applicationId, ApplicationStatus status);
}
