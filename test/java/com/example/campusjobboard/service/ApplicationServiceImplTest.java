package com.example.campusjobboard.service;

import com.example.campusjobboard.enums.ApplicationStatus;
import com.example.campusjobboard.exception.DuplicateApplicationException;
import com.example.campusjobboard.model.Job;
import com.example.campusjobboard.model.JobApplication;
import com.example.campusjobboard.model.User;
import com.example.campusjobboard.repository.JobApplicationRepository;
import com.example.campusjobboard.service.impl.ApplicationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApplicationServiceImplTest {

    @Mock
    private JobApplicationRepository jobApplicationRepository;

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void applyToJob_whenNew_shouldCreateApplication() {
        Job job = new Job();
        User student = new User();

        when(jobApplicationRepository.existsByJobAndStudent(job, student)).thenReturn(false);
        when(jobApplicationRepository.save(any(JobApplication.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        JobApplication app = applicationService.applyToJob(job, student);

        assertEquals(job, app.getJob());
        assertEquals(student, app.getStudent());
        assertEquals(ApplicationStatus.SUBMITTED, app.getStatus());
        verify(jobApplicationRepository).save(app);
    }

    @Test
    void applyToJob_whenDuplicate_shouldThrowException() {
        Job job = new Job();
        User student = new User();

        when(jobApplicationRepository.existsByJobAndStudent(job, student)).thenReturn(true);

        assertThrows(DuplicateApplicationException.class,
                () -> applicationService.applyToJob(job, student));
    }
}
