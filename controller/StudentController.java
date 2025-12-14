package com.example.campusjobboard.controller;

import com.example.campusjobboard.model.Job;
import com.example.campusjobboard.model.User;
import com.example.campusjobboard.service.ApplicationService;
import com.example.campusjobboard.service.JobService;
import com.example.campusjobboard.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final JobService jobService;
    private final ApplicationService applicationService;
    private final UserService userService;

    public StudentController(JobService jobService,
                             ApplicationService applicationService,
                             UserService userService) {
        this.jobService = jobService;
        this.applicationService = applicationService;
        this.userService = userService;
    }

    @GetMapping("/jobs")
    public String listJobs(Model model) {
        List<Job> jobs = jobService.findApprovedJobs();
        model.addAttribute("jobs", jobs);
        return "student/jobs";
    }

    @GetMapping("/jobs/{id}")
    public String jobDetails(@PathVariable Long id, Model model) {
        Job job = jobService.findById(id);
        model.addAttribute("job", job);
        return "student/job-details";
    }

    @PostMapping("/jobs/{id}/apply")
    public String applyToJob(@PathVariable Long id,
                             @AuthenticationPrincipal UserDetails userDetails) {
        User student = userService.findByEmail(userDetails.getUsername());
        Job job = jobService.findById(id);
        applicationService.applyToJob(job, student);
        return "redirect:/student/my-applications";
    }

    @GetMapping("/my-applications")
    public String myApplications(@AuthenticationPrincipal UserDetails userDetails,
                                 Model model) {
        User student = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("applications", applicationService.findApplicationsByStudent(student));
        return "student/my-applications";
    }
}
