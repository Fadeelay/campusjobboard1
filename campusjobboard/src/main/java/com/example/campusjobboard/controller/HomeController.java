package com.example.campusjobboard.controller;

import com.example.campusjobboard.service.JobService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final JobService jobService;

    public HomeController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("recentJobs", jobService.findApprovedJobs().stream().limit(15).toList());
        return "index";
    }
}
