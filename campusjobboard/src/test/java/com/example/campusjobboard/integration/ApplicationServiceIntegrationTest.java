package com.example.campusjobboard.integration;

import com.example.campusjobboard.enums.ApplicationStatus;
import com.example.campusjobboard.enums.JobStatus;
import com.example.campusjobboard.enums.Role;
import com.example.campusjobboard.exception.DuplicateApplicationException;
import com.example.campusjobboard.model.Job;
import com.example.campusjobboard.model.JobApplication;
import com.example.campusjobboard.model.User;
import com.example.campusjobboard.service.ApplicationService;
import com.example.campusjobboard.service.JobService;
import com.example.campusjobboard.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ApplicationServiceIntegrationTest {

    @Autowired ApplicationService applicationService;
    @Autowired JobService jobService;
    @Autowired UserService userService;

    private User student;
    private Job approvedJob;

    @BeforeEach
    void setUp() {
        User emp = new User();
        emp.setFullName("Employer");
        emp.setEmail("emp_" + System.nanoTime() + "@test.com");
        emp.setPassword("pass");
        User employer = userService.registerUser(emp, Role.EMPLOYER);

        Job job = new Job();
        job.setTitle("Campus Intern");
        job.setDescription("Great role for students.");
        approvedJob = jobService.createJob(job, employer);
        jobService.changeJobStatus(approvedJob.getJobId(), JobStatus.APPROVED);
        approvedJob = jobService.findById(approvedJob.getJobId());

        User s = new User();
        s.setFullName("Student One");
        s.setEmail("student_" + System.nanoTime() + "@test.com");
        s.setPassword("pass");
        student = userService.registerUser(s, Role.STUDENT);
    }

    private JobApplication newFormData() {
        JobApplication formData = new JobApplication();
        formData.setStudentIdNumber("STU-TEST-001");
        return formData;
    }

    @Test
    void apply_createsApplicationWithAppliedStatus() {
        JobApplication app = applicationService.applyToJob(approvedJob, student, newFormData());

        assertThat(app.getApplicationId()).isNotNull();
        assertThat(app.getStatus()).isEqualTo(ApplicationStatus.APPLIED);
        assertThat(app.getStudent().getUserId()).isEqualTo(student.getUserId());
    }

    @Test
    void applyTwice_throwsDuplicateApplicationException() {
        applicationService.applyToJob(approvedJob, student, newFormData());

        assertThatThrownBy(() -> applicationService.applyToJob(approvedJob, student, newFormData()))
                .isInstanceOf(DuplicateApplicationException.class);
    }

    @Test
    void findApplicationsByStudent_returnsCorrectApplications() {
        applicationService.applyToJob(approvedJob, student, newFormData());

        List<JobApplication> apps = applicationService.findApplicationsByStudent(student);
        assertThat(apps).hasSize(1);
        assertThat(apps.get(0).getJob().getJobId()).isEqualTo(approvedJob.getJobId());
    }

    @Test
    void updateApplicationStatus_toInterviewing() {
        JobApplication app = applicationService.applyToJob(approvedJob, student, newFormData());

        JobApplication updated = applicationService.updateApplicationStatus(
                app.getApplicationId(), ApplicationStatus.INTERVIEWING);

        assertThat(updated.getStatus()).isEqualTo(ApplicationStatus.INTERVIEWING);
    }

    @Test
    void updateApplicationStatus_toHired() {
        JobApplication app = applicationService.applyToJob(approvedJob, student, newFormData());

        applicationService.updateApplicationStatus(app.getApplicationId(), ApplicationStatus.INTERVIEWING);
        JobApplication hired = applicationService.updateApplicationStatus(
                app.getApplicationId(), ApplicationStatus.HIRED);

        assertThat(hired.getStatus()).isEqualTo(ApplicationStatus.HIRED);
    }

    @Test
    void updateApplicationStatus_toRejected() {
        JobApplication app = applicationService.applyToJob(approvedJob, student, newFormData());

        JobApplication rejected = applicationService.updateApplicationStatus(
                app.getApplicationId(), ApplicationStatus.REJECTED);

        assertThat(rejected.getStatus()).isEqualTo(ApplicationStatus.REJECTED);
    }
}
