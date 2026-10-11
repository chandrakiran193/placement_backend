package com.placement.controller;

import com.placement.dto.request.LoginRequest;
import com.placement.dto.request.RegisterRequest;
import com.placement.dto.response.ApiResponse;
import com.placement.dto.response.AuthResponse;
import com.placement.entity.User;
import com.placement.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        AuthResponse response = authService.register(registerRequest);
        return ResponseEntity.ok(ApiResponse.success("Registration successful", response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<User>> getCurrentUser() {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(ApiResponse.success("User fetched successfully", user));
    }
}
