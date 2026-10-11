package com.placement.controller;

import com.placement.dto.request.RecruiterProfileRequest;
import com.placement.dto.request.ScheduleInterviewRequest;
import com.placement.dto.request.UpdateStatusRequest;
import com.placement.dto.response.ApiResponse;
import com.placement.dto.response.JobApplicantResponse;
import com.placement.entity.Interview;
import com.placement.entity.JobApplication;
import com.placement.entity.JobPosting;
import com.placement.entity.RecruiterProfile;
import com.placement.entity.User;
import com.placement.service.AuthService;
import com.placement.service.RecruiterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recruiter")
public class RecruiterController {

    @Autowired
    private RecruiterService recruiterService;

    @Autowired
    private AuthService authService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<RecruiterProfile>> getProfile() {
        User user = authService.getCurrentAuthenticatedUser();
        RecruiterProfile profile = recruiterService.getProfileByUserId(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Profile fetched successfully", profile));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<RecruiterProfile>> updateProfile(@RequestBody RecruiterProfileRequest request) {
        User user = authService.getCurrentAuthenticatedUser();
        RecruiterProfile updated = recruiterService.updateProfile(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", updated));
    }

    @GetMapping("/jobs")
    public ResponseEntity<ApiResponse<List<JobPosting>>> getMyJobs() {
        User user = authService.getCurrentAuthenticatedUser();
        List<JobPosting> jobs = recruiterService.getJobsByRecruiterId(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Jobs fetched successfully", jobs));
    }

    @GetMapping("/jobs/{jobId}/applicants")
    public ResponseEntity<ApiResponse<List<JobApplicantResponse>>> getApplicantsForJob(@PathVariable Long jobId) {
        User user = authService.getCurrentAuthenticatedUser();
        List<JobApplicantResponse> applicants = recruiterService.getApplicantsForJob(jobId, user.getId());
        return ResponseEntity.ok(ApiResponse.success("Applicants fetched successfully", applicants));
    }

    @GetMapping("/applicants")
    public ResponseEntity<ApiResponse<List<JobApplicantResponse>>> getAllApplicants() {
        User user = authService.getCurrentAuthenticatedUser();
        List<JobApplicantResponse> applicants = recruiterService.getAllApplicantsForRecruiter(user.getId());
        return ResponseEntity.ok(ApiResponse.success("All applicants fetched successfully", applicants));
    }

    @PutMapping("/applications/{applicationId}/status")
    public ResponseEntity<ApiResponse<JobApplication>> updateStatus(
            @PathVariable Long applicationId,
            @Valid @RequestBody UpdateStatusRequest request) {
        User user = authService.getCurrentAuthenticatedUser();
        JobApplication updated = recruiterService.updateApplicationStatus(applicationId, user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Application status updated successfully", updated));
    }

    @PostMapping("/interviews")
    public ResponseEntity<ApiResponse<Interview>> scheduleInterview(@Valid @RequestBody ScheduleInterviewRequest request) {
        User user = authService.getCurrentAuthenticatedUser();
        Interview interview = recruiterService.scheduleInterview(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Interview scheduled successfully", interview));
    }

    @GetMapping("/interviews")
    public ResponseEntity<ApiResponse<List<Interview>>> getInterviews() {
        User user = authService.getCurrentAuthenticatedUser();
        List<Interview> interviews = recruiterService.getInterviewsByRecruiterId(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Interviews fetched successfully", interviews));
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats() {
        User user = authService.getCurrentAuthenticatedUser();
        Map<String, Object> stats = recruiterService.getRecruiterStats(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Recruiter stats fetched successfully", stats));
    }
}
