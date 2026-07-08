package com.example.campusjobboard.integration;

import com.example.campusjobboard.enums.JobStatus;
import com.example.campusjobboard.enums.Role;
import com.example.campusjobboard.model.Job;
import com.example.campusjobboard.model.User;
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
class JobServiceIntegrationTest {

    @Autowired JobService jobService;
    @Autowired UserService userService;

    private User employer;

    @BeforeEach
    void setUp() {
        User u = new User();
        u.setFullName("Test Employer");
        u.setEmail("employer_" + System.nanoTime() + "@test.com");
        u.setPassword("password123");
        employer = userService.registerUser(u, Role.EMPLOYER);
    }

    @Test
    void createdJob_hasStatusPending() {
        Job job = buildJob("Software Intern", "Engineering");
        Job saved = jobService.createJob(job, employer);

        assertThat(saved.getJobId()).isNotNull();
        assertThat(saved.getStatus()).isEqualTo(JobStatus.PENDING);
        assertThat(saved.getEmployer().getUserId()).isEqualTo(employer.getUserId());
    }

    @Test
    void approvedJob_appearsInApprovedList() {
        Job job = jobService.createJob(buildJob("Data Analyst", "Data"), employer);
        jobService.changeJobStatus(job.getJobId(), JobStatus.APPROVED);

        List<Job> approved = jobService.findApprovedJobs();
        assertThat(approved).anyMatch(j -> j.getJobId().equals(job.getJobId()));
    }

    @Test
    void filteredSearch_byCategory() {
        jobService.changeJobStatus(
                jobService.createJob(buildJob("Design Lead", "Design"), employer).getJobId(),
                JobStatus.APPROVED);
        jobService.changeJobStatus(
                jobService.createJob(buildJob("Backend Dev", "Engineering"), employer).getJobId(),
                JobStatus.APPROVED);

        List<Job> results = jobService.findApprovedJobsFiltered(null, "design", null, null);
        assertThat(results).allMatch(j -> j.getCategory().equalsIgnoreCase("Design"));
    }

    @Test
    void filteredSearch_byLocation() {
        Job job = buildJob("Remote Dev", "Engineering");
        job.setLocation("Austin, TX");
        jobService.changeJobStatus(
                jobService.createJob(job, employer).getJobId(), JobStatus.APPROVED);

        List<Job> results = jobService.findApprovedJobsFiltered("austin", null, null, null);
        assertThat(results).anyMatch(j -> j.getLocation().toLowerCase().contains("austin"));
    }

    @Test
    void updateJob_changesFields() {
        Job job = jobService.createJob(buildJob("Old Title", "Tech"), employer);

        Job update = buildJob("New Title", "Tech");
        update.setLocation("Chicago");
        jobService.updateJob(job.getJobId(), update, employer);

        Job fetched = jobService.findById(job.getJobId());
        assertThat(fetched.getTitle()).isEqualTo("New Title");
        assertThat(fetched.getLocation()).isEqualTo("Chicago");
    }

    @Test
    void updateJob_byDifferentEmployer_throws() {
        Job job = jobService.createJob(buildJob("Job", "Tech"), employer);

        User other = new User();
        other.setFullName("Other");
        other.setEmail("other_" + System.nanoTime() + "@test.com");
        other.setPassword("pass");
        User otherEmployer = userService.registerUser(other, Role.EMPLOYER);

        assertThatThrownBy(() -> jobService.updateJob(job.getJobId(), buildJob("Hack", "X"), otherEmployer))
                .isInstanceOf(org.springframework.security.access.AccessDeniedException.class);
    }

    @Test
    void deleteJob_removesFromRepository() {
        Job job = jobService.createJob(buildJob("Temp Job", "Misc"), employer);
        Long id = job.getJobId();
        jobService.deleteJob(id, employer);

        assertThatThrownBy(() -> jobService.findById(id))
                .isInstanceOf(RuntimeException.class);
    }

    private Job buildJob(String title, String category) {
        Job j = new Job();
        j.setTitle(title);
        j.setDescription("A great opportunity.");
        j.setCategory(category);
        j.setLocation("New York");
        j.setSalary(20.0);
        return j;
    }
}
