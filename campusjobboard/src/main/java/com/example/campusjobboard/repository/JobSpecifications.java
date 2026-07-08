package com.example.campusjobboard.repository;

import com.example.campusjobboard.enums.JobStatus;
import com.example.campusjobboard.model.Job;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class JobSpecifications {

    private JobSpecifications() {}

    public static Specification<Job> hasStatus(JobStatus status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<Job> locationContains(String location) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("location")), "%" + location.toLowerCase() + "%");
    }

    public static Specification<Job> hasCategory(String category) {
        return (root, query, cb) ->
                cb.equal(cb.lower(root.get("category")), category.toLowerCase());
    }

    public static Specification<Job> salaryAtLeast(Double min) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("salary"), min);
    }

    public static Specification<Job> salaryAtMost(Double max) {
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("salary"), max);
    }

    public static Specification<Job> deadlineAfter(LocalDate date) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("deadline"), date);
    }
}
