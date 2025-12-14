package com.example.campusjobboard.controller;




import com.example.campusjobboard.model.*;
import com.example.campusjobboard.enums.*;
import com.example.campusjobboard.service.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final JobService jobService;
    private final UserService userService;

    public AdminController(AdminService adminService,
                           JobService jobService,
                           UserService userService) {
        this.adminService = adminService;
        this.jobService = jobService;
        this.userService = userService;
    }

    @GetMapping("/jobs")
    public String manageJobs(Model model) {
        model.addAttribute("jobs", jobService.findAll());
        return "admin/jobs";
    }

    @PostMapping("/jobs/{id}/approve")
    public String approveJob(@PathVariable Long id) {
        adminService.approveJob(id);
        return "redirect:/admin/jobs";
    }

    @PostMapping("/jobs/{id}/reject")
    public String rejectJob(@PathVariable Long id) {
        adminService.rejectJob(id);
        return "redirect:/admin/jobs";
    }

    @GetMapping("/users")
    public String manageUsers(Model model) {
        List<User> allUsers = userService.findAll();
        List<User> nonAdmins = allUsers.stream()
                .filter(u -> u.getRole() != Role.ADMIN)
                .toList();
        model.addAttribute("users", nonAdmins);
        return "admin/users";
    }


    @PostMapping("/users/{id}/activate")
    public String activateUser(@PathVariable Long id) {
        adminService.activateUser(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/deactivate")
    public String deactivateUser(@PathVariable Long id) {
        adminService.deactivateUser(id);
        return "redirect:/admin/users";
    }
}

