package com.example.campusjobboard.service;

import com.example.campusjobboard.model.Job;
import com.example.campusjobboard.model.User;
import com.example.campusjobboard.enums.JobStatus;

import java.util.List;

public interface JobService {

    Job createJob(Job job, User employer);

    Job updateJob(Long jobId, Job updatedJob, User employer);

    void deleteJob(Long jobId, User employer);

    Job findById(Long jobId);

    List<Job> findApprovedJobs();

    List<Job> findApprovedJobsFiltered(String location, String category, Double minSalary, Double maxSalary);

    List<Job> findAll();

    List<Job> findJobsByEmployer(User employer);

    Job changeJobStatus(Long jobId, JobStatus status);
}
