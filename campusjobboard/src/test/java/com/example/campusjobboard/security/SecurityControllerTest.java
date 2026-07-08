package com.example.campusjobboard.security;

import com.example.campusjobboard.enums.Role;
import com.example.campusjobboard.model.User;
import com.example.campusjobboard.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SecurityControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired UserService userService;

    @BeforeEach
    void createTestUsers() {
        // employer dashboard controller does userService.findByEmail(principal.getName())
        User emp = new User();
        emp.setFullName("Test Employer");
        emp.setEmail("emp@test.com");
        emp.setPassword("password");
        userService.registerUser(emp, Role.EMPLOYER);
    }

    // ── Public routes ─────────────────────────────────────────────────────────

    @Test
    void homePage_isPublic() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    void loginPage_isPublic() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    void registerPage_isPublic() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk());
    }

    @Test
    void apiJobs_isPublic() throws Exception {
        mockMvc.perform(get("/api/jobs"))
                .andExpect(status().isOk());
    }

    // ── Admin routes ──────────────────────────────────────────────────────────

    @Test
    void adminDashboard_redirectsAnonymousToLogin() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void adminDashboard_forbiddenForStudent() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "EMPLOYER")
    void adminDashboard_forbiddenForEmployer() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminDashboard_allowedForAdmin() throws Exception {
        // AdminController.dashboard() has no DB lookup — just counts
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isOk());
    }

    // ── Employer routes ───────────────────────────────────────────────────────

    @Test
    void employerDashboard_redirectsAnonymousToLogin() throws Exception {
        mockMvc.perform(get("/employer/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void employerDashboard_forbiddenForStudent() throws Exception {
        mockMvc.perform(get("/employer/dashboard"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void employerDashboard_forbiddenForAdmin() throws Exception {
        mockMvc.perform(get("/employer/dashboard"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "EMPLOYER", username = "emp@test.com")
    void employerDashboard_allowedForEmployer() throws Exception {
        // username matches user created in @BeforeEach so DB lookup succeeds
        mockMvc.perform(get("/employer/dashboard"))
                .andExpect(status().isOk());
    }

    // ── Student routes ────────────────────────────────────────────────────────

    @Test
    void studentJobs_redirectsAnonymousToLogin() throws Exception {
        mockMvc.perform(get("/student/jobs"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    @WithMockUser(roles = "EMPLOYER")
    void studentJobs_forbiddenForEmployer() throws Exception {
        mockMvc.perform(get("/student/jobs"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void studentJobs_forbiddenForAdmin() throws Exception {
        mockMvc.perform(get("/student/jobs"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void studentJobs_allowedForStudent() throws Exception {
        // browseJobs() has no principal DB lookup — just filters jobs
        mockMvc.perform(get("/student/jobs"))
                .andExpect(status().isOk());
    }
}
