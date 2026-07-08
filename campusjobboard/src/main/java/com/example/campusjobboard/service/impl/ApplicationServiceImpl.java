package com.example.campusjobboard.service.impl;

import com.example.campusjobboard.enums.ApplicationStatus;
import com.example.campusjobboard.exception.DuplicateApplicationException;
import com.example.campusjobboard.model.Job;
import com.example.campusjobboard.model.JobApplication;
import com.example.campusjobboard.model.User;
import com.example.campusjobboard.repository.JobApplicationRepository;
import com.example.campusjobboard.service.ApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private static final Logger log = LoggerFactory.getLogger(ApplicationServiceImpl.class);

    private final JobApplicationRepository jobApplicationRepository;

    public ApplicationServiceImpl(JobApplicationRepository jobApplicationRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
    }

    @Override
    public JobApplication applyToJob(Job job, User student, JobApplication formData) {
        boolean exists = jobApplicationRepository.existsByJobAndStudent(job, student);
        if (exists) {
            throw new DuplicateApplicationException("You have already applied to this job");
        }
        formData.setJob(job);
        formData.setStudent(student);
        formData.setStatus(ApplicationStatus.APPLIED);
        JobApplication saved = jobApplicationRepository.save(formData);
        log.info("Student {} applied to job {} (applicationId={})",
                student.getEmail(), job.getJobId(), saved.getApplicationId());
        return saved;
    }

    @Override
    public JobApplication findById(Long id) {
        return jobApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + id));
    }

    @Override
    public List<JobApplication> findApplicationsByStudent(User student) {
        return jobApplicationRepository.findByStudent(student);
    }

    @Override
    public List<JobApplication> findApplicationsByJob(Job job) {
        return jobApplicationRepository.findByJob(job);
    }

    @Override
    public JobApplication updateApplicationStatus(Long applicationId, ApplicationStatus status) {
        JobApplication application = findById(applicationId);
        application.setStatus(status);
        JobApplication saved = jobApplicationRepository.save(application);
        log.info("Application {} status updated to {}", applicationId, status);
        return saved;
    }
}
