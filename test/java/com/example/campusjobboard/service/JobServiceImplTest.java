package com.example.campusjobboard.service;

import com.example.campusjobboard.enums.JobStatus;
import com.example.campusjobboard.exception.JobNotFoundException;
import com.example.campusjobboard.model.Job;
import com.example.campusjobboard.model.User;
import com.example.campusjobboard.repository.JobRepository;
import com.example.campusjobboard.service.impl.JobServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JobServiceImplTest {

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private JobServiceImpl jobService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createJob_shouldSetEmployerAndPendingStatus() {
        User employer = new User();
        employer.setUserId(1L);

        Job job = new Job();
        job.setTitle("Test Job");

        when(jobRepository.save(any(Job.class))).thenAnswer(inv -> inv.getArgument(0));

        Job saved = jobService.createJob(job, employer);

        assertEquals(employer, saved.getEmployer());
        assertEquals(JobStatus.PENDING, saved.getStatus());
        verify(jobRepository).save(saved);
    }

    @Test
    void findById_whenNotFound_shouldThrowException() {
        when(jobRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(JobNotFoundException.class,
                () -> jobService.findById(99L));
    }
}
