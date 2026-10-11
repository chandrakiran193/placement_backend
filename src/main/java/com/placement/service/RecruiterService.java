package com.placement.service;

import com.placement.dto.request.RecruiterProfileRequest;
import com.placement.dto.request.ScheduleInterviewRequest;
import com.placement.dto.request.UpdateStatusRequest;
import com.placement.dto.response.JobApplicantResponse;
import com.placement.entity.*;
import com.placement.exception.BadRequestException;
import com.placement.exception.ResourceNotFoundException;
import com.placement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RecruiterService {

    @Autowired
    private RecruiterProfileRepository recruiterProfileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private JobApplicationRepository applicationRepository;

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Autowired
    private InterviewRepository interviewRepository;

    @Transactional(readOnly = true)
    public RecruiterProfile getProfileByUserId(Long userId) {
        return recruiterProfileRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
                    RecruiterProfile profile = new RecruiterProfile(user);
                    return recruiterProfileRepository.save(profile);
                });
    }

    @Transactional
    public RecruiterProfile updateProfile(Long userId, RecruiterProfileRequest request) {
        RecruiterProfile profile = getProfileByUserId(userId);
        User user = profile.getUser();

        if (request.getName() != null && !request.getName().isBlank()) user.setName(request.getName().trim());
        if (request.getPhone() != null) user.setPhone(request.getPhone().trim());
        userRepository.save(user);

        if (request.getCompanyName() != null) profile.setCompanyName(request.getCompanyName().trim());
        if (request.getCompanyWebsite() != null) profile.setCompanyWebsite(request.getCompanyWebsite().trim());
        if (request.getCompanyLocation() != null) profile.setCompanyLocation(request.getCompanyLocation().trim());
        if (request.getDesignation() != null) profile.setDesignation(request.getDesignation().trim());
        if (request.getCompanyDescription() != null) profile.setCompanyDescription(request.getCompanyDescription().trim());
        if (request.getIndustryType() != null) profile.setIndustryType(request.getIndustryType().trim());

        return recruiterProfileRepository.save(profile);
    }

    @Transactional(readOnly = true)
    public List<JobPosting> getJobsByRecruiterId(Long recruiterId) {
        return jobPostingRepository.findByRecruiterIdOrderByCreatedAtDesc(recruiterId);
    }

    @Transactional(readOnly = true)
    public List<JobApplicantResponse> getApplicantsForJob(Long jobId, Long recruiterId) {
        JobPosting job = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job", "id", jobId));

        if (!job.getRecruiter().getId().equals(recruiterId)) {
            throw new BadRequestException("You are not authorized to view applicants for this job posting");
        }

        List<JobApplication> applications = applicationRepository.findByJobIdOrderByAppliedAtDesc(jobId);
        return applications.stream().map(this::mapToApplicantResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<JobApplicantResponse> getAllApplicantsForRecruiter(Long recruiterId) {
        List<JobApplication> applications = applicationRepository.findByRecruiterId(recruiterId);
        return applications.stream().map(this::mapToApplicantResponse).collect(Collectors.toList());
    }

    @Transactional
    public JobApplication updateApplicationStatus(Long applicationId, Long recruiterId, UpdateStatusRequest request) {
        JobApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application", "id", applicationId));

        if (!application.getJob().getRecruiter().getId().equals(recruiterId)) {
            throw new BadRequestException("You are not authorized to update this application");
        }

        try {
            ApplicationStatus newStatus = ApplicationStatus.valueOf(request.getStatus().toUpperCase());
            application.setStatus(newStatus);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid status: " + request.getStatus());
        }

        if (request.getFeedback() != null) {
            application.setRecruiterFeedback(request.getFeedback());
        }

        return applicationRepository.save(application);
    }

    @Transactional
    public Interview scheduleInterview(Long recruiterId, ScheduleInterviewRequest request) {
        JobApplication application = applicationRepository.findById(request.getApplicationId())
                .orElseThrow(() -> new ResourceNotFoundException("Application", "id", request.getApplicationId()));

        if (!application.getJob().getRecruiter().getId().equals(recruiterId)) {
            throw new BadRequestException("You are not authorized to schedule interviews for this application");
        }

        Interview interview = new Interview();
        interview.setApplication(application);
        interview.setInterviewDate(request.getInterviewDate());
        interview.setDurationMinutes(request.getDurationMinutes() != null ? request.getDurationMinutes() : 45);
        interview.setInterviewRound(request.getInterviewRound());
        interview.setMode(request.getMode() != null ? request.getMode() : "ONLINE");
        interview.setMeetingLink(request.getMeetingLink());
        interview.setLocation(request.getLocation());
        interview.setInstructions(request.getInstructions());
        interview.setStatus("SCHEDULED");

        // Automatically update application status to INTERVIEW_SCHEDULED
        application.setStatus(ApplicationStatus.INTERVIEW_SCHEDULED);
        applicationRepository.save(application);

        return interviewRepository.save(interview);
    }

    @Transactional(readOnly = true)
    public List<Interview> getInterviewsByRecruiterId(Long recruiterId) {
        return interviewRepository.findByRecruiterId(recruiterId);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getRecruiterStats(Long recruiterId) {
        Map<String, Object> stats = new HashMap<>();
        List<JobPosting> jobs = jobPostingRepository.findByRecruiterIdOrderByCreatedAtDesc(recruiterId);
        long totalJobs = jobs.size();
        long activeJobs = jobs.stream().filter(j -> "ACTIVE".equalsIgnoreCase(j.getStatus())).count();
        long totalApplicants = applicationRepository.countByRecruiterId(recruiterId);
        long shortlisted = applicationRepository.countByRecruiterIdAndStatus(recruiterId, ApplicationStatus.SHORTLISTED);
        long interviews = interviewRepository.countByRecruiterId(recruiterId);
        long selected = applicationRepository.countByRecruiterIdAndStatus(recruiterId, ApplicationStatus.SELECTED);

        stats.put("totalJobs", totalJobs);
        stats.put("activeJobs", activeJobs);
        stats.put("totalApplicants", totalApplicants);
        stats.put("shortlisted", shortlisted);
        stats.put("interviewsScheduled", interviews);
        stats.put("selected", selected);
        return stats;
    }

    private JobApplicantResponse mapToApplicantResponse(JobApplication app) {
        JobApplicantResponse resp = new JobApplicantResponse();
        resp.setApplicationId(app.getId());
        resp.setJobId(app.getJob().getId());
        resp.setJobTitle(app.getJob().getTitle());
        resp.setStudentId(app.getStudent().getId());
        resp.setStudentName(app.getStudent().getName());
        resp.setStudentEmail(app.getStudent().getEmail());
        resp.setStudentPhone(app.getStudent().getPhone());
        resp.setStatus(app.getStatus());
        resp.setCoverNote(app.getCoverNote());
        resp.setResumeUrl(app.getResumeUrl());
        resp.setRecruiterFeedback(app.getRecruiterFeedback());
        resp.setAppliedAt(app.getAppliedAt());

        studentProfileRepository.findByUserId(app.getStudent().getId()).ifPresent(profile -> {
            resp.setRollNumber(profile.getRollNumber());
            resp.setDepartment(profile.getDepartment());
            resp.setDegree(profile.getDegree());
            resp.setGraduationYear(profile.getGraduationYear());
            resp.setCgpa(profile.getCgpa());
            resp.setSkills(profile.getSkills());
            if (resp.getResumeUrl() == null) {
                resp.setResumeUrl(profile.getResumeUrl());
            }
            resp.setResumeOriginalName(profile.getResumeOriginalName());
        });

        return resp;
    }
}
