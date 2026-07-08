package com.example.campusjobboard.controller;

import com.example.campusjobboard.logging.RecentLogStore;
import com.example.campusjobboard.service.AdminService;
import com.example.campusjobboard.service.JobService;
import com.example.campusjobboard.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    private final AdminService adminService;
    private final JobService jobService;
    private final UserService userService;
    private final RecentLogStore recentLogStore;

    public AdminController(AdminService adminService, JobService jobService,
                           UserService userService, RecentLogStore recentLogStore) {
        this.adminService = adminService;
        this.jobService = jobService;
        this.userService = userService;
        this.recentLogStore = recentLogStore;
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("totalJobs", jobService.findAll().size());
        model.addAttribute("totalUsers", userService.findAll().size());
        model.addAttribute("pendingJobs", jobService.findAll().stream()
                .filter(j -> j.getStatus().name().equals("PENDING")).count());
        return "admin/dashboard";
    }

    @GetMapping("/jobs")
    public String manageJobs(Model model) {
        model.addAttribute("jobs", jobService.findAll());
        return "admin/jobs";
    }

    @PostMapping("/jobs/{id}/approve")
    public String approveJob(@PathVariable Long id, RedirectAttributes ra) {
        adminService.approveJob(id);
        ra.addFlashAttribute("success", "Job approved.");
        return "redirect:/admin/jobs";
    }

    @PostMapping("/jobs/{id}/reject")
    public String rejectJob(@PathVariable Long id, RedirectAttributes ra) {
        adminService.rejectJob(id);
        ra.addFlashAttribute("success", "Job rejected.");
        return "redirect:/admin/jobs";
    }

    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    @PostMapping("/users/{id}/activate")
    public String activateUser(@PathVariable Long id, RedirectAttributes ra) {
        adminService.activateUser(id);
        log.info("Admin activated user {}", id);
        ra.addFlashAttribute("success", "User activated.");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/deactivate")
    public String deactivateUser(@PathVariable Long id, RedirectAttributes ra) {
        adminService.deactivateUser(id);
        log.info("Admin deactivated user {}", id);
        ra.addFlashAttribute("success", "User deactivated.");
        return "redirect:/admin/users";
    }

    @GetMapping("/logs")
    public String viewLogs(Model model) {
        model.addAttribute("logs", recentLogStore.getAll());
        return "admin/logs";
    }
}
