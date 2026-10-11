package com.placement.controller;

import com.placement.dto.request.StudentProfileRequest;
import com.placement.dto.response.ApiResponse;
import com.placement.entity.Interview;
import com.placement.entity.JobApplication;
import com.placement.entity.StudentProfile;
import com.placement.entity.User;
import com.placement.service.AuthService;
import com.placement.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private AuthService authService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<StudentProfile>> getProfile() {
        User user = authService.getCurrentAuthenticatedUser();
        StudentProfile profile = studentService.getProfileByUserId(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Profile fetched successfully", profile));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<StudentProfile>> updateProfile(@RequestBody StudentProfileRequest request) {
        User user = authService.getCurrentAuthenticatedUser();
        StudentProfile updated = studentService.updateProfile(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", updated));
    }

    @PostMapping("/resume")
    public ResponseEntity<ApiResponse<StudentProfile>> uploadResume(@RequestParam("file") MultipartFile file) {
        User user = authService.getCurrentAuthenticatedUser();
        StudentProfile profile = studentService.uploadResume(user.getId(), file);
        return ResponseEntity.ok(ApiResponse.success("Resume uploaded successfully", profile));
    }

    @GetMapping("/applications")
    public ResponseEntity<ApiResponse<List<JobApplication>>> getApplications() {
        User user = authService.getCurrentAuthenticatedUser();
        List<JobApplication> applications = studentService.getApplicationsByStudentId(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Applications fetched successfully", applications));
    }

    @GetMapping("/interviews")
    public ResponseEntity<ApiResponse<List<Interview>>> getInterviews() {
        User user = authService.getCurrentAuthenticatedUser();
        List<Interview> interviews = studentService.getInterviewsByStudentId(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Interviews fetched successfully", interviews));
    }
}
