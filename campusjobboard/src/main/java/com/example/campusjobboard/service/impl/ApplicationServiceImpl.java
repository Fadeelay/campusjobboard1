/**
 * Concrete implementation of ApplicationService.
 * Implements the rule that a student can apply only once per job and
 * delegates persistence to JobApplicationRepository.
 */

package com.example.campusjobboard.service.impl;

import com.example.campusjobboard.enums.ApplicationStatus;
import com.example.campusjobboard.exception.DuplicateApplicationException;
import com.example.campusjobboard.model.Job;
import com.example.campusjobboard.model.JobApplication;
import com.example.campusjobboard.model.User;
import com.example.campusjobboard.repository.JobApplicationRepository;
import com.example.campusjobboard.service.ApplicationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final JobApplicationRepository jobApplicationRepository;

    public ApplicationServiceImpl(JobApplicationRepository jobApplicationRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
    }

    @Override
    public JobApplication applyToJob(Job job, User student) {
        boolean exists = jobApplicationRepository.existsByJobAndStudent(job, student);
        if (exists) {
            throw new DuplicateApplicationException("You have already applied to this job");
        }

        JobApplication application = new JobApplication();
        application.setJob(job);
        application.setStudent(student);
        application.setStatus(ApplicationStatus.SUBMITTED);

        return jobApplicationRepository.save(application);
    }

    @Override
    public List<JobApplication> findApplicationsByStudent(User student) {
        return jobApplicationRepository.findByStudent(student);
    }

    @Override
    public List<JobApplication> findApplicationsByJob(Job job) {
        return jobApplicationRepository.findByJob(job);
    }
}
