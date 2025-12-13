package com.example.campusjobboard.model;
/**
 * JPA entity representing a student's application to a specific job.
 * Links a student and a job, tracks application status and applied date,
 * and enforces a unique job–student combination.
 */

import com.example.campusjobboard.enums.ApplicationStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "JOB_APPLICATION",
        uniqueConstraints = @UniqueConstraint(columnNames = {"job_id", "student_id"}))
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status; // SUBMITTED, ACCEPTED, REJECTED

    private LocalDateTime appliedAt;

    @PrePersist
    public void onCreate() {
        if (appliedAt == null) {
            appliedAt = LocalDateTime.now();
        }
        if (status == null) {
            status = ApplicationStatus.SUBMITTED;
        }
    }

    // getters and setters

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }
}
