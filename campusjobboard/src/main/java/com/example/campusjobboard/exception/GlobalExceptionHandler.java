package com.example.campusjobboard.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserNotFoundException.class)
    public Object handleUserNotFound(UserNotFoundException ex, Model model, HttpServletRequest request) {
        log.warn("UserNotFoundException: {}", ex.getMessage());
        if (isApiRequest(request)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        }
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/user-not-found";
    }

    @ExceptionHandler(JobNotFoundException.class)
    public Object handleJobNotFound(JobNotFoundException ex, Model model, HttpServletRequest request) {
        log.warn("JobNotFoundException: {}", ex.getMessage());
        if (isApiRequest(request)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        }
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/job-not-found";
    }

    @ExceptionHandler(DuplicateApplicationException.class)
    public Object handleDuplicateApplication(DuplicateApplicationException ex, Model model, HttpServletRequest request) {
        log.warn("DuplicateApplicationException: {}", ex.getMessage());
        if (isApiRequest(request)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
        }
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/duplicate-application";
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public Object handleAccessDenied(org.springframework.security.access.AccessDeniedException ex,
                                     Model model, HttpServletRequest request) {
        log.warn("AccessDeniedException on {}: {}", request.getRequestURI(), ex.getMessage());
        if (isApiRequest(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", ex.getMessage()));
        }
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/general-error";
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleNoResource() {
        // silently ignore missing static resources (browser favicon requests, etc.)
    }

    @ExceptionHandler(Exception.class)
    public Object handleGeneric(Exception ex, Model model, HttpServletRequest request) {
        log.error("Unhandled exception on {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        if (isApiRequest(request)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
        model.addAttribute("errorMessage", "An unexpected error occurred.");
        return "error/general-error";
    }

    private boolean isApiRequest(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/api/");
    }
}
