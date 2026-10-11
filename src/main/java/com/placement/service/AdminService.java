package com.placement.service;

import com.placement.dto.response.DashboardStatsResponse;
import com.placement.entity.*;
import com.placement.exception.BadRequestException;
import com.placement.exception.ResourceNotFoundException;
import com.placement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private JobApplicationRepository applicationRepository;

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User toggleUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (user.getRole() == Role.ROLE_ADMIN) {
            long adminCount = userRepository.countByRole(Role.ROLE_ADMIN);
            if (adminCount <= 1 && user.isEnabled()) {
                throw new BadRequestException("Cannot disable the sole Administrator account.");
            }
        }

        user.setEnabled(!user.isEnabled());
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (user.getRole() == Role.ROLE_ADMIN) {
            long adminCount = userRepository.countByRole(Role.ROLE_ADMIN);
            if (adminCount <= 1) {
                throw new BadRequestException("Cannot delete the only Administrator account.");
            }
        }

        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public List<JobPosting> getAllJobs() {
        return jobPostingRepository.findAll();
    }

    @Transactional
    public JobPosting updateJobStatus(Long jobId, String status) {
        JobPosting job = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job", "id", jobId));

        job.setStatus(status.toUpperCase());
        return jobPostingRepository.save(job);
    }

    @Transactional
    public void deleteJob(Long jobId) {
        JobPosting job = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job", "id", jobId));

        jobPostingRepository.delete(job);
    }

    @Transactional(readOnly = true)
    public DashboardStatsResponse getDashboardStats() {
        DashboardStatsResponse stats = new DashboardStatsResponse();

        long totalStudents = userRepository.countByRole(Role.ROLE_STUDENT);
        long totalRecruiters = userRepository.countByRole(Role.ROLE_RECRUITER);
        long totalJobs = jobPostingRepository.count();
        long activeJobs = jobPostingRepository.countByStatus("ACTIVE");
        long totalApplications = applicationRepository.count();
        long placedStudents = applicationRepository.countDistinctPlacedStudents();

        double placementRate = totalStudents > 0 ? ((double) placedStudents / totalStudents) * 100.0 : 0.0;

        List<JobPosting> allJobs = jobPostingRepository.findAll();
        double avgSalary = allJobs.stream()
                .filter(j -> j.getSalaryPackage() != null)
                .mapToDouble(JobPosting::getSalaryPackage)
                .average()
                .orElse(0.0);

        double maxSalary = allJobs.stream()
                .filter(j -> j.getSalaryPackage() != null)
                .mapToDouble(JobPosting::getSalaryPackage)
                .max()
                .orElse(0.0);

        Map<String, Long> statusCount = new HashMap<>();
        for (ApplicationStatus status : ApplicationStatus.values()) {
            statusCount.put(status.name(), applicationRepository.countByStatus(status));
        }

        Map<String, Long> deptCount = new HashMap<>();
        List<StudentProfile> profiles = studentProfileRepository.findAll();
        for (StudentProfile p : profiles) {
            String dept = (p.getDepartment() != null && !p.getDepartment().isBlank()) ? p.getDepartment() : "General";
            deptCount.put(dept, deptCount.getOrDefault(dept, 0L) + 1);
        }

        stats.setTotalStudents(totalStudents);
        stats.setTotalRecruiters(totalRecruiters);
        stats.setTotalJobs(totalJobs);
        stats.setActiveJobs(activeJobs);
        stats.setTotalApplications(totalApplications);
        stats.setPlacedStudents(placedStudents);
        stats.setPlacementRate(Math.round(placementRate * 10.0) / 10.0);
        stats.setAverageSalary(Math.round(avgSalary * 10.0) / 10.0);
        stats.setHighestSalary(Math.round(maxSalary * 10.0) / 10.0);
        stats.setApplicationsByStatus(statusCount);
        stats.setStudentsByDepartment(deptCount);

        return stats;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getPlacementReport() {
        Map<String, Object> report = new HashMap<>();
        report.put("stats", getDashboardStats());
        report.put("recentApplications", applicationRepository.findAll().stream().limit(50).collect(Collectors.toList()));
        report.put("activeJobs", jobPostingRepository.findByStatusOrderByCreatedAtDesc("ACTIVE"));
        return report;
    }
}
