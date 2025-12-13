

package com.example.campusjobboard.service;

import com.example.campusjobboard.model.Job;
import com.example.campusjobboard.model.JobApplication;
import com.example.campusjobboard.model.User;

import java.util.List;

public interface ApplicationService {

    JobApplication applyToJob(Job job, User student);

    List<JobApplication> findApplicationsByStudent(User student);

    List<JobApplication> findApplicationsByJob(Job job);
}

