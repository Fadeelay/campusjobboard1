package com.example.campusjobboard.service.impl;

import com.example.campusjobboard.enums.JobStatus;
import com.example.campusjobboard.enums.Role;
import com.example.campusjobboard.exception.JobNotFoundException;
import com.example.campusjobboard.model.Job;
import com.example.campusjobboard.model.User;
import com.example.campusjobboard.repository.JobRepository;
import com.example.campusjobboard.repository.JobSpecifications;
import com.example.campusjobboard.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobServiceImpl implements JobService {

    private static final Logger log = LoggerFactory.getLogger(JobServiceImpl.class);

    private final JobRepository jobRepository;

    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public Job createJob(Job job, User employer) {
        job.setEmployer(employer);
        job.setStatus(JobStatus.PENDING);
        Job saved = jobRepository.save(job);
        log.info("Employer {} created job '{}' (id={})", employer.getEmail(), saved.getTitle(), saved.getJobId());
        return saved;
    }

    @Override
    public Job updateJob(Long jobId, Job updatedJob, User employer) {
        Job existing = findById(jobId);
        requireOwnerOrAdmin(existing, employer);

        existing.setTitle(updatedJob.getTitle());
        existing.setDescription(updatedJob.getDescription());
        existing.setLocation(updatedJob.getLocation());
        existing.setSalary(updatedJob.getSalary());
        existing.setCategory(updatedJob.getCategory());
        existing.setDeadline(updatedJob.getDeadline());
        return jobRepository.save(existing);
    }

    @Override
    public void deleteJob(Long jobId, User employer) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException("Job not found with id " + jobId));
        requireOwnerOrAdmin(job, employer);
        jobRepository.delete(job);
        log.info("Job {} deleted by {}", jobId, employer.getEmail());
    }

    private void requireOwnerOrAdmin(Job job, User employer) {
        if (employer.getRole() != Role.ADMIN && !job.getEmployer().getUserId().equals(employer.getUserId())) {
            throw new AccessDeniedException("You can only manage your own jobs");
        }
    }

    @Override
    public Job findById(Long jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException("Job not found with id: " + jobId));
    }

    @Override
    public List<Job> findApprovedJobs() {
        return jobRepository.findByStatus(JobStatus.APPROVED);
    }

    @Override
    public List<Job> findApprovedJobsFiltered(String location, String category, Double minSalary, Double maxSalary) {
        Specification<Job> spec = JobSpecifications.hasStatus(JobStatus.APPROVED);

        if (location != null && !location.isBlank()) {
            spec = spec.and(JobSpecifications.locationContains(location));
        }
        if (category != null && !category.isBlank()) {
            spec = spec.and(JobSpecifications.hasCategory(category));
        }
        if (minSalary != null) {
            spec = spec.and(JobSpecifications.salaryAtLeast(minSalary));
        }
        if (maxSalary != null) {
            spec = spec.and(JobSpecifications.salaryAtMost(maxSalary));
        }

        return jobRepository.findAll(spec);
    }

    @Override
    public List<Job> findJobsByEmployer(User employer) {
        return jobRepository.findByEmployer(employer);
    }

    @Override
    public Job changeJobStatus(Long jobId, JobStatus status) {
        Job job = findById(jobId);
        job.setStatus(status);
        Job saved = jobRepository.save(job);
        log.info("Job {} status changed to {}", jobId, status);
        return saved;
    }

    @Override
    public List<Job> findAll() {
        return jobRepository.findAll();
    }
}
