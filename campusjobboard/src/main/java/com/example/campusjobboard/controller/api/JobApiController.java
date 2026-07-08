package com.example.campusjobboard.controller.api;

import com.example.campusjobboard.model.Job;
import com.example.campusjobboard.model.User;
import com.example.campusjobboard.service.JobService;
import com.example.campusjobboard.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@Tag(name = "Jobs", description = "Browse and manage job postings")
public class JobApiController {

    private final JobService jobService;
    private final UserService userService;

    public JobApiController(JobService jobService, UserService userService) {
        this.jobService = jobService;
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "List approved jobs", description = "Supports filtering by location, category, and salary range")
    public List<Job> listJobs(@RequestParam(required = false) String location,
                              @RequestParam(required = false) String category,
                              @RequestParam(required = false) Double minSalary,
                              @RequestParam(required = false) Double maxSalary) {
        return jobService.findApprovedJobsFiltered(location, category, minSalary, maxSalary);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a job by ID")
    public Job getJob(@PathVariable Long id) {
        return jobService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('EMPLOYER')")
    @Operation(summary = "Create a job posting (Employer only)")
    public Job createJob(@RequestBody Job job, Principal principal) {
        User employer = userService.findByEmail(principal.getName());
        return jobService.createJob(job, employer);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYER')")
    @Operation(summary = "Update a job posting (Employer only — must own the job)")
    public Job updateJob(@PathVariable Long id, @RequestBody Job job, Principal principal) {
        User employer = userService.findByEmail(principal.getName());
        return jobService.updateJob(id, job, employer);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('EMPLOYER','ADMIN')")
    @Operation(summary = "Delete a job posting (Employer or Admin)")
    public void deleteJob(@PathVariable Long id, Principal principal) {
        User employer = userService.findByEmail(principal.getName());
        jobService.deleteJob(id, employer);
    }
}
