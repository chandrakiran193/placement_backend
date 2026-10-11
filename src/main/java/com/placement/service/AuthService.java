package com.placement.service;

import com.placement.config.JwtTokenProvider;
import com.placement.dto.request.LoginRequest;
import com.placement.dto.request.RegisterRequest;
import com.placement.dto.response.AuthResponse;
import com.placement.entity.RecruiterProfile;
import com.placement.entity.Role;
import com.placement.entity.StudentProfile;
import com.placement.entity.User;
import com.placement.exception.BadRequestException;
import com.placement.repository.RecruiterProfileRepository;
import com.placement.repository.StudentProfileRepository;
import com.placement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Autowired
    private RecruiterProfileRepository recruiterProfileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Transactional
    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail().trim().toLowerCase(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        User user = (User) authentication.getPrincipal();

        return new AuthResponse(
                jwt,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                user.getPhone()
        );
    }

    @Transactional
    public AuthResponse register(RegisterRequest registerRequest) {
        String email = registerRequest.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("An account with this email already exists: " + email);
        }

        Role userRole;
        try {
            String roleStr = registerRequest.getRole().trim().toUpperCase();
            if (!roleStr.startsWith("ROLE_")) {
                roleStr = "ROLE_" + roleStr;
            }
            userRole = Role.valueOf(roleStr);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid role provided. Valid roles are: STUDENT, RECRUITER, ADMIN");
        }

        User user = new User();
        user.setName(registerRequest.getName().trim());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setPhone(registerRequest.getPhone());
        user.setRole(userRole);
        user.setEnabled(true);

        User savedUser = userRepository.save(user);

        // Create associated profile based on role
        if (userRole == Role.ROLE_STUDENT) {
            StudentProfile profile = new StudentProfile(savedUser);
            profile.setDepartment(registerRequest.getDepartment());
            profile.setRollNumber(registerRequest.getRollNumber());
            profile.setCgpa(registerRequest.getCgpa());
            studentProfileRepository.save(profile);
        } else if (userRole == Role.ROLE_RECRUITER) {
            RecruiterProfile profile = new RecruiterProfile(savedUser);
            profile.setCompanyName(registerRequest.getCompanyName());
            recruiterProfileRepository.save(profile);
        }

        String jwt = tokenProvider.generateTokenFromEmail(savedUser.getEmail());

        return new AuthResponse(
                jwt,
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole().name(),
                savedUser.getPhone()
        );
    }

    public User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof User)) {
            throw new BadRequestException("No authenticated user found in session");
        }
        return (User) authentication.getPrincipal();
    }
}
