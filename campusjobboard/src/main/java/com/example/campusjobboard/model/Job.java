package com.example.campusjobboard.model;
/**
 * JPA entity representing a job posting created by an employer.
 * Contains job details such as title, description, location, salary,
 * category, deadline, status, and audit timestamps.
 */


import com.example.campusjobboard.enums.JobStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.*;
import java.util.List;

@Entity
@Table(name = "JOB")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;

    @ManyToOne
    @JoinColumn(name = "employer_id", nullable = false)
    private User employer;

    @NotBlank
    @Size(max = 100)
    private String title;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobApplication> applications;


    @NotBlank
    @Lob
    private String description;

    private String location;
    private Double salary;
    private String category;
    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    private JobStatus status; // PENDING, APPROVED, REJECTED

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
        if (status == null) {
            status = JobStatus.PENDING;
        }
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // getters/setters

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public User getEmployer() {
        return employer;
    }

    public void setEmployer(User employer) {
        this.employer = employer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}

