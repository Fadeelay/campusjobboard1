package com.example.campusjobboard.controller.api;

import com.example.campusjobboard.enums.ApplicationStatus;
import com.example.campusjobboard.enums.Role;
import com.example.campusjobboard.model.Job;
import com.example.campusjobboard.model.JobApplication;
import com.example.campusjobboard.model.User;
import com.example.campusjobboard.service.ApplicationService;
import com.example.campusjobboard.service.JobService;
import com.example.campusjobboard.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
@Tag(name = "Applications", description = "Apply to jobs and manage application statuses")
public class ApplicationApiController {

    private final ApplicationService applicationService;
    private final JobService jobService;
    private final UserService userService;

    public ApplicationApiController(ApplicationService applicationService,
                                    JobService jobService, UserService userService) {
        this.applicationService = applicationService;
        this.jobService = jobService;
        this.userService = userService;
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "List my applications (Student only)")
    public List<JobApplication> myApplications(Principal principal) {
        User student = userService.findByEmail(principal.getName());
        return applicationService.findApplicationsByStudent(student);
    }

    @PostMapping("/job/{jobId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Apply to a job (Student only)")
    public JobApplication apply(@PathVariable Long jobId,
                                @RequestParam(required = false) String studentIdNumber,
                                Principal principal) {
        User student = userService.findByEmail(principal.getName());
        Job job = jobService.findById(jobId);
        JobApplication formData = new JobApplication();
        formData.setStudentIdNumber(studentIdNumber != null ? studentIdNumber.trim() : "");
        return applicationService.applyToJob(job, student, formData);
    }

    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasAnyRole('EMPLOYER','ADMIN')")
    @Operation(summary = "List applications for a specific job (Employer or Admin)")
    public List<JobApplication> getApplicationsForJob(@PathVariable Long jobId, Principal principal) {
        Job job = jobService.findById(jobId);
        requireOwnerOrAdmin(job, principal);
        return applicationService.findApplicationsByJob(job);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('EMPLOYER','ADMIN')")
    @Operation(summary = "Update application status (Employer or Admin)")
    public JobApplication updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body, Principal principal) {
        JobApplication application = applicationService.findById(id);
        requireOwnerOrAdmin(application.getJob(), principal);
        ApplicationStatus status = ApplicationStatus.valueOf(body.get("status").toUpperCase());
        return applicationService.updateApplicationStatus(id, status);
    }

    private void requireOwnerOrAdmin(Job job, Principal principal) {
        User caller = userService.findByEmail(principal.getName());
        boolean isAdmin = caller.getRole() == Role.ADMIN;
        if (!isAdmin && !job.getEmployer().getUserId().equals(caller.getUserId())) {
            throw new AccessDeniedException("You do not own this job");
        }
    }
}
