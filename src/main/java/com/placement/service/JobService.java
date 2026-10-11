package com.placement.service;

import com.placement.dto.request.ApplyJobRequest;
import com.placement.dto.request.JobPostingRequest;
import com.placement.entity.*;
import com.placement.exception.BadRequestException;
import com.placement.exception.ResourceNotFoundException;
import com.placement.repository.JobApplicationRepository;
import com.placement.repository.JobPostingRepository;
import com.placement.repository.StudentProfileRepository;
import com.placement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class JobService {

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private JobApplicationRepository applicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Transactional(readOnly = true)
    public List<JobPosting> searchJobs(String keyword, String jobType, String location, Double minSalary) {
        String cleanKeyword = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
        String cleanType = (jobType != null && !jobType.trim().isEmpty() && !jobType.equalsIgnoreCase("ALL")) ? jobType.trim() : null;
        String cleanLoc = (location != null && !location.trim().isEmpty()) ? location.trim() : null;

        return jobPostingRepository.searchJobs(cleanKeyword, cleanType, cleanLoc, minSalary);
    }

    @Transactional(readOnly = true)
    public JobPosting getJobById(Long id) {
        return jobPostingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job", "id", id));
    }

    @Transactional
    public JobPosting createJob(Long recruiterId, JobPostingRequest request) {
        User recruiter = userRepository.findById(recruiterId)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter", "id", recruiterId));

        JobPosting job = new JobPosting();
        job.setRecruiter(recruiter);
        job.setTitle(request.getTitle().trim());
        job.setCompanyName(request.getCompanyName() != null && !request.getCompanyName().isBlank()
                ? request.getCompanyName().trim() : recruiter.getName());
        job.setJobType(request.getJobType());
        job.setLocation(request.getLocation().trim());
        job.setSalaryPackage(request.getSalaryPackage());
        job.setMinCgpa(request.getMinCgpa());
        job.setEligibleDepartments(request.getEligibleDepartments());
        job.setSkillsRequired(request.getSkillsRequired());
        job.setDescription(request.getDescription());
        job.setResponsibilities(request.getResponsibilities());
        job.setRequirements(request.getRequirements());
        job.setOpeningsCount(request.getOpeningsCount() != null ? request.getOpeningsCount() : 1);
        job.setApplicationDeadline(request.getApplicationDeadline());
        job.setStatus(request.getStatus() != null ? request.getStatus() : "ACTIVE");

        return jobPostingRepository.save(job);
    }

    @Transactional
    public JobPosting updateJob(Long jobId, Long requesterId, boolean isAdmin, JobPostingRequest request) {
        JobPosting job = getJobById(jobId);

        if (!isAdmin && !job.getRecruiter().getId().equals(requesterId)) {
            throw new BadRequestException("You do not have permission to update this job");
        }

        if (request.getTitle() != null) job.setTitle(request.getTitle().trim());
        if (request.getCompanyName() != null) job.setCompanyName(request.getCompanyName().trim());
        if (request.getJobType() != null) job.setJobType(request.getJobType());
        if (request.getLocation() != null) job.setLocation(request.getLocation().trim());
        if (request.getSalaryPackage() != null) job.setSalaryPackage(request.getSalaryPackage());
        if (request.getMinCgpa() != null) job.setMinCgpa(request.getMinCgpa());
        if (request.getEligibleDepartments() != null) job.setEligibleDepartments(request.getEligibleDepartments());
        if (request.getSkillsRequired() != null) job.setSkillsRequired(request.getSkillsRequired());
        if (request.getDescription() != null) job.setDescription(request.getDescription());
        if (request.getResponsibilities() != null) job.setResponsibilities(request.getResponsibilities());
        if (request.getRequirements() != null) job.setRequirements(request.getRequirements());
        if (request.getOpeningsCount() != null) job.setOpeningsCount(request.getOpeningsCount());
        if (request.getApplicationDeadline() != null) job.setApplicationDeadline(request.getApplicationDeadline());
        if (request.getStatus() != null) job.setStatus(request.getStatus());

        return jobPostingRepository.save(job);
    }

    @Transactional
    public void deleteJob(Long jobId, Long requesterId, boolean isAdmin) {
        JobPosting job = getJobById(jobId);

        if (!isAdmin && !job.getRecruiter().getId().equals(requesterId)) {
            throw new BadRequestException("You do not have permission to delete this job");
        }

        jobPostingRepository.delete(job);
    }

    @Transactional
    public JobApplication applyForJob(Long jobId, Long studentId, ApplyJobRequest request) {
        JobPosting job = getJobById(jobId);

        if (!"ACTIVE".equalsIgnoreCase(job.getStatus())) {
            throw new BadRequestException("This job posting is no longer active for applications.");
        }

        if (job.getApplicationDeadline() != null && job.getApplicationDeadline().isBefore(LocalDate.now())) {
            throw new BadRequestException("The application deadline for this job has passed.");
        }

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));

        if (applicationRepository.existsByJobIdAndStudentId(jobId, studentId)) {
            throw new BadRequestException("You have already applied for this job.");
        }

        // Student Profile validation: CGPA check if minimum requirement is set
        StudentProfile profile = studentProfileRepository.findByUserId(studentId).orElse(null);
        if (profile != null && job.getMinCgpa() != null && profile.getCgpa() != null) {
            if (profile.getCgpa() < job.getMinCgpa()) {
                throw new BadRequestException("Your CGPA (" + profile.getCgpa() + ") does not meet the minimum eligibility requirement (" + job.getMinCgpa() + ") for this role.");
            }
        }

        String resumeUrl = (request.getResumeUrl() != null && !request.getResumeUrl().isBlank())
                ? request.getResumeUrl()
                : (profile != null ? profile.getResumeUrl() : null);

        JobApplication application = new JobApplication();
        application.setJob(job);
        application.setStudent(student);
        application.setCoverNote(request.getCoverNote());
        application.setResumeUrl(resumeUrl);
        application.setStatus(ApplicationStatus.APPLIED);

        return applicationRepository.save(application);
    }
}
