package com.placement.service;

import com.placement.dto.request.StudentProfileRequest;
import com.placement.entity.Interview;
import com.placement.entity.JobApplication;
import com.placement.entity.StudentProfile;
import com.placement.entity.User;
import com.placement.exception.ResourceNotFoundException;
import com.placement.repository.InterviewRepository;
import com.placement.repository.JobApplicationRepository;
import com.placement.repository.StudentProfileRepository;
import com.placement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobApplicationRepository applicationRepository;

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Transactional(readOnly = true)
    public StudentProfile getProfileByUserId(Long userId) {
        return studentProfileRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
                    StudentProfile newProfile = new StudentProfile(user);
                    return studentProfileRepository.save(newProfile);
                });
    }

    @Transactional
    public StudentProfile updateProfile(Long userId, StudentProfileRequest request) {
        StudentProfile profile = getProfileByUserId(userId);
        User user = profile.getUser();

        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName().trim());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone().trim());
        }
        userRepository.save(user);

        if (request.getRollNumber() != null) profile.setRollNumber(request.getRollNumber().trim());
        if (request.getDepartment() != null) profile.setDepartment(request.getDepartment().trim());
        if (request.getDegree() != null) profile.setDegree(request.getDegree().trim());
        if (request.getGraduationYear() != null) profile.setGraduationYear(request.getGraduationYear());
        if (request.getCgpa() != null) profile.setCgpa(request.getCgpa());
        if (request.getSkills() != null) profile.setSkills(request.getSkills().trim());
        if (request.getBio() != null) profile.setBio(request.getBio().trim());
        if (request.getGithubUrl() != null) profile.setGithubUrl(request.getGithubUrl().trim());
        if (request.getLinkedinUrl() != null) profile.setLinkedinUrl(request.getLinkedinUrl().trim());
        if (request.getPortfolioUrl() != null) profile.setPortfolioUrl(request.getPortfolioUrl().trim());

        return studentProfileRepository.save(profile);
    }

    @Transactional
    public StudentProfile uploadResume(Long userId, MultipartFile file) {
        StudentProfile profile = getProfileByUserId(userId);

        // Delete old resume file if one exists
        if (profile.getResumeUrl() != null) {
            fileStorageService.deleteFile(profile.getResumeUrl());
        }

        String storedFileName = fileStorageService.storeResume(file);
        profile.setResumeUrl(storedFileName);
        profile.setResumeOriginalName(file.getOriginalFilename());
        profile.setResumeUploadedAt(LocalDateTime.now());

        return studentProfileRepository.save(profile);
    }

    @Transactional(readOnly = true)
    public List<JobApplication> getApplicationsByStudentId(Long studentId) {
        return applicationRepository.findByStudentIdOrderByAppliedAtDesc(studentId);
    }

    @Transactional(readOnly = true)
    public List<Interview> getInterviewsByStudentId(Long studentId) {
        return interviewRepository.findByStudentId(studentId);
    }
}
