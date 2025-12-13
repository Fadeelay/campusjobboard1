package com.example.campusjobboard.service;
/**
 * Service interface that groups high-level admin operations.
 * Wraps job approval/rejection and user activation/deactivation logic.
 */



public interface AdminService {

    void approveJob(Long jobId);

    void rejectJob(Long jobId);

    void activateUser(Long userId);

    void deactivateUser(Long userId);
}
