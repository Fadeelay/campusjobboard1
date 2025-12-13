package com.example.campusjobboard.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(UserNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/user-not-found";
    }

    @ExceptionHandler(JobNotFoundException.class)
    public String handleJobNotFound(JobNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/job-not-found";
    }

    @ExceptionHandler(DuplicateApplicationException.class)
    public String handleDuplicateApplication(DuplicateApplicationException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/duplicate-application";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneric(Exception ex, Model model) {
        model.addAttribute("errorMessage", "An unexpected error occurred.");
        return "error/general-error";
    }
}
