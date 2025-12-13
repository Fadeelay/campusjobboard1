/**
 * Concrete implementation of JobService.
 * Manages job posting lifecycle, including creation, employer updates,
 * deletion, and admin status changes (pending/approved/rejected).
 */

package com.example.campusjobboard.service.impl;

import com.example.campusjobboard.enums.JobStatus;
import com.example.campusjobboard.exception.JobNotFoundException;
import com.example.campusjobboard.model.Job;
import com.example.campusjobboard.model.User;
import com.example.campusjobboard.repository.JobRepository;
import com.example.campusjobboard.service.JobService;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;

    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public Job createJob(Job job, User employer) {
        job.setEmployer(employer);
        job.setStatus(JobStatus.PENDING);
        return jobRepository.save(job);
    }

    @Override
    public Job updateJob(Long jobId, Job updatedJob, User employer) {
        Job existing = findById(jobId);
        if (!existing.getEmployer().getUserId().equals(employer.getUserId())) {
            throw new IllegalStateException("You can only update your own jobs");
        }

        existing.setTitle(updatedJob.getTitle());
        existing.setDescription(updatedJob.getDescription());
        existing.setLocation(updatedJob.getLocation());
        existing.setSalary(updatedJob.getSalary());
        existing.setCategory(updatedJob.getCategory());
        existing.setDeadline(updatedJob.getDeadline());
        // status and employer usually not changed here
        return jobRepository.save(existing);
    }

    @Override
    public void deleteJob(Long jobId, User employer) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException("Job not found with id " + jobId));
        jobRepository.delete(job);
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
    public List<Job> findJobsByEmployer(User employer) {
        return jobRepository.findByEmployer(employer);
    }

    @Override
    public Job changeJobStatus(Long jobId, JobStatus status) {
        Job job = findById(jobId);
        job.setStatus(status);
        return jobRepository.save(job);
    }

    @Override
    public List<Job> findAll() {
        return jobRepository.findAll();
    }
}
