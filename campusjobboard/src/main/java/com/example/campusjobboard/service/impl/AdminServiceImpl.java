/**
 * Concrete implementation of AdminService.
 * Coordinates admin actions by delegating to JobService and UserService
 * for job moderation and user account management.
 */

package com.example.campusjobboard.service.impl;

import com.example.campusjobboard.enums.JobStatus;
import com.example.campusjobboard.service.AdminService;
import com.example.campusjobboard.service.JobService;
import com.example.campusjobboard.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    private final JobService jobService;
    private final UserService userService;

    public AdminServiceImpl(JobService jobService,
                            UserService userService) {
        this.jobService = jobService;
        this.userService = userService;
    }

    @Override
    public void approveJob(Long jobId) {
        jobService.changeJobStatus(jobId, JobStatus.APPROVED);
    }

    @Override
    public void rejectJob(Long jobId) {
        jobService.changeJobStatus(jobId, JobStatus.REJECTED);
    }

    @Override
    public void activateUser(Long userId) {
        userService.activateUser(userId);
    }

    @Override
    public void deactivateUser(Long userId) {
        userService.deactivateUser(userId);
    }
}
