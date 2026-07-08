package com.example.campusjobboard.controller;

import com.example.campusjobboard.model.Job;
import com.example.campusjobboard.model.JobApplication;
import com.example.campusjobboard.model.User;
import com.example.campusjobboard.service.ApplicationService;
import com.example.campusjobboard.service.JobService;
import com.example.campusjobboard.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/student")
public class StudentController {

    private static final Logger log = LoggerFactory.getLogger(StudentController.class);

    private final JobService jobService;
    private final ApplicationService applicationService;
    private final UserService userService;

    public StudentController(JobService jobService, ApplicationService applicationService, UserService userService) {
        this.jobService = jobService;
        this.applicationService = applicationService;
        this.userService = userService;
    }

    @GetMapping("/jobs")
    public String browseJobs(@RequestParam(required = false) String location,
                             @RequestParam(required = false) String category,
                             @RequestParam(required = false) Double minSalary,
                             @RequestParam(required = false) Double maxSalary,
                             Model model) {
        model.addAttribute("jobs", jobService.findApprovedJobsFiltered(location, category, minSalary, maxSalary));
        model.addAttribute("location", location);
        model.addAttribute("category", category);
        model.addAttribute("minSalary", minSalary);
        model.addAttribute("maxSalary", maxSalary);
        return "student/jobs";
    }

    @GetMapping("/jobs/{id}")
    public String viewJob(@PathVariable Long id, Model model, Principal principal) {
        Job job = jobService.findById(id);
        User student = userService.findByEmail(principal.getName());
        boolean alreadyApplied = applicationService.findApplicationsByStudent(student)
                .stream().anyMatch(a -> a.getJob().getJobId().equals(id));
        model.addAttribute("job", job);
        model.addAttribute("alreadyApplied", alreadyApplied);
        return "student/job-detail";
    }

    @GetMapping("/apply/{jobId}")
    public String showApplyForm(@PathVariable Long jobId, Model model, Principal principal) {
        Job job = jobService.findById(jobId);
        User student = userService.findByEmail(principal.getName());
        boolean alreadyApplied = applicationService.findApplicationsByStudent(student)
                .stream().anyMatch(a -> a.getJob().getJobId().equals(jobId));
        if (alreadyApplied) {
            return "redirect:/student/jobs/" + jobId;
        }
        JobApplication form = new JobApplication();
        form.setFullName(student.getFullName());
        form.setEmail(student.getEmail());
        model.addAttribute("job", job);
        model.addAttribute("jobApplication", form);
        return "student/apply";
    }

    @PostMapping("/apply/{jobId}")
    public String apply(@PathVariable Long jobId,
                        @ModelAttribute("jobApplication") JobApplication formData,
                        Principal principal,
                        RedirectAttributes ra) {
        try {
            User student = userService.findByEmail(principal.getName());
            Job job = jobService.findById(jobId);
            applicationService.applyToJob(job, student, formData);
            ra.addFlashAttribute("success", "Application submitted successfully! You can track your status below.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/student/apply/" + jobId;
        }
        return "redirect:/student/my-applications";
    }

    @GetMapping("/my-applications")
    public String myApplications(Principal principal, Model model) {
        User student = userService.findByEmail(principal.getName());
        model.addAttribute("applications", applicationService.findApplicationsByStudent(student));
        return "student/my-applications";
    }
}
