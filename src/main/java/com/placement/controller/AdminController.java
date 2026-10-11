package com.placement.controller;

import com.placement.dto.response.ApiResponse;
import com.placement.dto.response.DashboardStatsResponse;
import com.placement.entity.JobPosting;
import com.placement.entity.User;
import com.placement.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = adminService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("Users fetched successfully", users));
    }

    @PutMapping("/users/{userId}/toggle-status")
    public ResponseEntity<ApiResponse<User>> toggleUserStatus(@PathVariable Long userId) {
        User updated = adminService.toggleUserStatus(userId);
        return ResponseEntity.ok(ApiResponse.success("User status toggled successfully", updated));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
    }

    @GetMapping("/jobs")
    public ResponseEntity<ApiResponse<List<JobPosting>>> getAllJobs() {
        List<JobPosting> jobs = adminService.getAllJobs();
        return ResponseEntity.ok(ApiResponse.success("Jobs fetched successfully", jobs));
    }

    @PutMapping("/jobs/{jobId}/status")
    public ResponseEntity<ApiResponse<JobPosting>> updateJobStatus(
            @PathVariable Long jobId,
            @RequestParam String status) {
        JobPosting updated = adminService.updateJobStatus(jobId, status);
        return ResponseEntity.ok(ApiResponse.success("Job status updated successfully", updated));
    }

    @DeleteMapping("/jobs/{jobId}")
    public ResponseEntity<ApiResponse<Void>> deleteJob(@PathVariable Long jobId) {
        adminService.deleteJob(jobId);
        return ResponseEntity.ok(ApiResponse.success("Job deleted successfully"));
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> getStats() {
        DashboardStatsResponse stats = adminService.getDashboardStats();
        return ResponseEntity.ok(ApiResponse.success("Statistics fetched successfully", stats));
    }

    @GetMapping("/report")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getPlacementReport() {
        Map<String, Object> report = adminService.getPlacementReport();
        return ResponseEntity.ok(ApiResponse.success("Report fetched successfully", report));
    }
}
