package com.placement.controller;

import com.placement.dto.request.ApplyJobRequest;
import com.placement.dto.request.JobPostingRequest;
import com.placement.dto.response.ApiResponse;
import com.placement.entity.JobApplication;
import com.placement.entity.JobPosting;
import com.placement.entity.Role;
import com.placement.entity.User;
import com.placement.service.AuthService;
import com.placement.service.JobService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private AuthService authService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<JobPosting>>> searchJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String jobType,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Double minSalary) {
        List<JobPosting> jobs = jobService.searchJobs(keyword, jobType, location, minSalary);
        return ResponseEntity.ok(ApiResponse.success("Jobs fetched successfully", jobs));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobPosting>> getJobById(@PathVariable Long id) {
        JobPosting job = jobService.getJobById(id);
        return ResponseEntity.ok(ApiResponse.success("Job details fetched successfully", job));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<ApiResponse<JobPosting>> createJob(@Valid @RequestBody JobPostingRequest request) {
        User user = authService.getCurrentAuthenticatedUser();
        JobPosting created = jobService.createJob(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Job posted successfully", created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<ApiResponse<JobPosting>> updateJob(
            @PathVariable Long id,
            @Valid @RequestBody JobPostingRequest request) {
        User user = authService.getCurrentAuthenticatedUser();
        boolean isAdmin = user.getRole() == Role.ROLE_ADMIN;
        JobPosting updated = jobService.updateJob(id, user.getId(), isAdmin, request);
        return ResponseEntity.ok(ApiResponse.success("Job updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteJob(@PathVariable Long id) {
        User user = authService.getCurrentAuthenticatedUser();
        boolean isAdmin = user.getRole() == Role.ROLE_ADMIN;
        jobService.deleteJob(id, user.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success("Job deleted successfully"));
    }

    @PostMapping("/{id}/apply")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<JobApplication>> applyForJob(
            @PathVariable Long id,
            @RequestBody(required = false) ApplyJobRequest request) {
        User user = authService.getCurrentAuthenticatedUser();
        if (request == null) {
            request = new ApplyJobRequest();
        }
        JobApplication application = jobService.applyForJob(id, user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Application submitted successfully", application));
    }
}
