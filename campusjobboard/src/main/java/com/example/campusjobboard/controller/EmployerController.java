package com.example.campusjobboard.controller;

import com.example.campusjobboard.enums.ApplicationStatus;
import com.example.campusjobboard.model.Job;
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
@RequestMapping("/employer")
public class EmployerController {

    private static final Logger log = LoggerFactory.getLogger(EmployerController.class);

    private final JobService jobService;
    private final ApplicationService applicationService;
    private final UserService userService;

    public EmployerController(JobService jobService, ApplicationService applicationService, UserService userService) {
        this.jobService = jobService;
        this.applicationService = applicationService;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Principal principal, Model model) {
        User employer = userService.findByEmail(principal.getName());
        model.addAttribute("jobs", jobService.findJobsByEmployer(employer));
        model.addAttribute("employer", employer);
        return "employer/dashboard";
    }

    @GetMapping("/post-job")
    public String showPostJob(Model model) {
        model.addAttribute("job", new Job());
        return "employer/post-job";
    }

    @PostMapping("/post-job")
    public String postJob(@ModelAttribute Job job, Principal principal, RedirectAttributes ra) {
        User employer = userService.findByEmail(principal.getName());
        jobService.createJob(job, employer);
        ra.addFlashAttribute("success", "Job posted and awaiting admin approval.");
        return "redirect:/employer/dashboard";
    }

    @GetMapping("/edit-job/{id}")
    public String showEditJob(@PathVariable Long id, Principal principal, Model model) {
        Job job = jobService.findById(id);
        User employer = userService.findByEmail(principal.getName());
        if (!job.getEmployer().getUserId().equals(employer.getUserId())) {
            return "redirect:/employer/dashboard";
        }
        model.addAttribute("job", job);
        return "employer/edit-job";
    }

    @PostMapping("/edit-job/{id}")
    public String editJob(@PathVariable Long id, @ModelAttribute Job job,
                          Principal principal, RedirectAttributes ra) {
        User employer = userService.findByEmail(principal.getName());
        jobService.updateJob(id, job, employer);
        ra.addFlashAttribute("success", "Job updated successfully.");
        return "redirect:/employer/dashboard";
    }

    @PostMapping("/delete-job/{id}")
    public String deleteJob(@PathVariable Long id, Principal principal, RedirectAttributes ra) {
        User employer = userService.findByEmail(principal.getName());
        jobService.deleteJob(id, employer);
        ra.addFlashAttribute("success", "Job deleted.");
        return "redirect:/employer/dashboard";
    }

    @GetMapping("/applications/{jobId}")
    public String viewApplications(@PathVariable Long jobId, Principal principal, Model model) {
        Job job = jobService.findById(jobId);
        User employer = userService.findByEmail(principal.getName());
        if (!job.getEmployer().getUserId().equals(employer.getUserId())) {
            return "redirect:/employer/dashboard";
        }
        model.addAttribute("job", job);
        model.addAttribute("applications", applicationService.findApplicationsByJob(job));
        model.addAttribute("statuses", ApplicationStatus.values());
        return "employer/applications";
    }

    @GetMapping("/applications/{jobId}/detail/{appId}")
    public String viewApplicationDetail(@PathVariable Long jobId, @PathVariable Long appId,
                                        Principal principal, Model model) {
        Job job = jobService.findById(jobId);
        User employer = userService.findByEmail(principal.getName());
        if (!job.getEmployer().getUserId().equals(employer.getUserId())) {
            return "redirect:/employer/dashboard";
        }
        model.addAttribute("job", job);
        model.addAttribute("jobApplication", applicationService.findById(appId));
        model.addAttribute("statuses", ApplicationStatus.values());
        return "employer/application-detail";
    }

    @PostMapping("/applications/{appId}/status")
    public String updateApplicationStatus(@PathVariable Long appId,
                                          @RequestParam ApplicationStatus status,
                                          @RequestParam Long jobId,
                                          @RequestParam(required = false, defaultValue = "false") boolean fromDetail,
                                          Principal principal,
                                          RedirectAttributes ra) {
        User employer = userService.findByEmail(principal.getName());
        var application = applicationService.findById(appId);
        if (!application.getJob().getJobId().equals(jobId)
                || !application.getJob().getEmployer().getUserId().equals(employer.getUserId())) {
            return "redirect:/employer/dashboard";
        }
        applicationService.updateApplicationStatus(appId, status);
        ra.addFlashAttribute("success", "Status updated to " + status);
        if (fromDetail) {
            return "redirect:/employer/applications/" + jobId + "/detail/" + appId;
        }
        return "redirect:/employer/applications/" + jobId;
    }
}
