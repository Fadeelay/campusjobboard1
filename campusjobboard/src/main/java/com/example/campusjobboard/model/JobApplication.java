package com.example.campusjobboard.model;

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
    @Column(length = 20)
    private ApplicationStatus status;

    private LocalDateTime appliedAt;

    // ── Personal information ───────────────────────────────────────────────────

    @Column(name = "student_id_number", length = 50)
    private String studentIdNumber;

    @Column(length = 100)
    private String fullName;

    @Column(length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(name = "field_of_study", length = 100)
    private String fieldOfStudy;

    @Column(length = 10)
    private String gpa;

    @Column(name = "year_of_study", length = 20)
    private String yearOfStudy;

    @Column(length = 50)
    private String availability;

    // ── Application questions ──────────────────────────────────────────────────

    @Column(name = "cover_letter", columnDefinition = "TEXT")
    private String coverLetter;

    @Column(name = "relevant_experience", columnDefinition = "TEXT")
    private String relevantExperience;

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    @PrePersist
    public void onCreate() {
        if (appliedAt == null) appliedAt = LocalDateTime.now();
        if (status == null)    status    = ApplicationStatus.APPLIED;
    }

    // ── Getters / Setters ─────────────────────────────────────────────────────

    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }

    public Job getJob() { return job; }
    public void setJob(Job job) { this.job = job; }

    public User getStudent() { return student; }
    public void setStudent(User student) { this.student = student; }

    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }

    public LocalDateTime getAppliedAt() { return appliedAt; }
    public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; }

    public String getStudentIdNumber() { return studentIdNumber; }
    public void setStudentIdNumber(String studentIdNumber) { this.studentIdNumber = studentIdNumber; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getFieldOfStudy() { return fieldOfStudy; }
    public void setFieldOfStudy(String fieldOfStudy) { this.fieldOfStudy = fieldOfStudy; }

    public String getGpa() { return gpa; }
    public void setGpa(String gpa) { this.gpa = gpa; }

    public String getYearOfStudy() { return yearOfStudy; }
    public void setYearOfStudy(String yearOfStudy) { this.yearOfStudy = yearOfStudy; }

    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }

    public String getCoverLetter() { return coverLetter; }
    public void setCoverLetter(String coverLetter) { this.coverLetter = coverLetter; }

    public String getRelevantExperience() { return relevantExperience; }
    public void setRelevantExperience(String relevantExperience) { this.relevantExperience = relevantExperience; }
}
