package com.example.campusjobboard.controller;

import com.example.campusjobboard.enums.Role;
import com.example.campusjobboard.model.User;
import com.example.campusjobboard.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user,
                           @RequestParam Role role,
                           Model model) {
        try {
            userService.registerUser(user, role);
            log.info("New user registered: {} as {}", user.getEmail(), role);
            return "redirect:/login?registered";
        } catch (Exception e) {
            log.warn("Registration failed for {}: {}", user.getEmail(), e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", user);
            return "auth/register";
        }
    }
}
