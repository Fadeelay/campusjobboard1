package com.example.campusjobboard.controller;

/**
 * Exposes employer-facing pages and actions.
 * Allows employers to manage their job postings (list, create, edit, delete)
 * and view applications submitted by students for each job.
 */


import com.example.campusjobboard.model.Job;
import com.example.campusjobboard.model.User;
import com.example.campusjobboard.service.ApplicationService;
import com.example.campusjobboard.service.JobService;
import com.example.campusjobboard.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/employer")
public class EmployerController {

    private final JobService jobService;
    private final ApplicationService applicationService;
    private final UserService userService;

    public EmployerController(JobService jobService,
                              ApplicationService applicationService,
                              UserService userService) {
        this.jobService = jobService;
        this.applicationService = applicationService;
        this.userService = userService;
    }

    @GetMapping("/my-jobs")
    public String myJobs(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User employer = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("jobs", jobService.findJobsByEmployer(employer));
        return "employer/my-jobs";
    }

    @GetMapping("/jobs/new")
    public String newJobForm(Model model) {
        model.addAttribute("job", new Job());
        return "employer/job-form";
    }

    @PostMapping("/jobs/new")
    public String createJob(@Valid @ModelAttribute Job job,
                            BindingResult result,
                            @AuthenticationPrincipal UserDetails userDetails) {
        if (result.hasErrors()) {
            return "employer/job-form";
        }

        User employer = userService.findByEmail(userDetails.getUsername());
        jobService.createJob(job, employer);
        return "redirect:/employer/my-jobs";
    }

    @GetMapping("/jobs/{id}/edit")
    public String editJobForm(@PathVariable Long id, Model model) {
        Job job = jobService.findById(id);
        model.addAttribute("job", job);
        return "employer/job-form";
    }

    @PostMapping("/jobs/{id}/edit")
    public String updateJob(@PathVariable Long id,
                            @Valid @ModelAttribute Job job,
                            BindingResult result,
                            @AuthenticationPrincipal UserDetails userDetails) {
        if (result.hasErrors()) {
            return "employer/job-form";
        }

        User employer = userService.findByEmail(userDetails.getUsername());
        jobService.updateJob(id, job, employer);
        return "redirect:/employer/my-jobs";
    }

    @PostMapping("/jobs/{id}/delete")  // ← Remove /employer
    public String deleteJob(@PathVariable Long id,
                            @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("DELETE - Job ID: " + id);
        User employer = userService.findByEmail(userDetails.getUsername());
        System.out.println("DELETE - Employer: " + employer.getEmail());
        jobService.deleteJob(id, employer);
        System.out.println("DELETE - Job deleted successfully");
        return "redirect:/employer/my-jobs?deleted";
    }


    @GetMapping("/jobs/{id}/applicants")
    public String viewApplicants(@PathVariable Long id, Model model) {
        Job job = jobService.findById(id);
        model.addAttribute("job", job);
        model.addAttribute("applicants", applicationService.findApplicationsByJob(job));
        return "employer/applicants";
    }
}
