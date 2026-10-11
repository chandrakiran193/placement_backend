package com.placement.dto.response;

import java.util.Map;

public class DashboardStatsResponse {
    private long totalStudents;
    private long totalRecruiters;
    private long totalJobs;
    private long activeJobs;
    private long totalApplications;
    private long placedStudents;
    private double placementRate;
    private double averageSalary;
    private double highestSalary;
    private Map<String, Long> applicationsByStatus;
    private Map<String, Long> studentsByDepartment;

    public DashboardStatsResponse() {}

    public long getTotalStudents() { return totalStudents; }
    public void setTotalStudents(long totalStudents) { this.totalStudents = totalStudents; }
    public long getTotalRecruiters() { return totalRecruiters; }
    public void setTotalRecruiters(long totalRecruiters) { this.totalRecruiters = totalRecruiters; }
    public long getTotalJobs() { return totalJobs; }
    public void setTotalJobs(long totalJobs) { this.totalJobs = totalJobs; }
    public long getActiveJobs() { return activeJobs; }
    public void setActiveJobs(long activeJobs) { this.activeJobs = activeJobs; }
    public long getTotalApplications() { return totalApplications; }
    public void setTotalApplications(long totalApplications) { this.totalApplications = totalApplications; }
    public long getPlacedStudents() { return placedStudents; }
    public void setPlacedStudents(long placedStudents) { this.placedStudents = placedStudents; }
    public double getPlacementRate() { return placementRate; }
    public void setPlacementRate(double placementRate) { this.placementRate = placementRate; }
    public double getAverageSalary() { return averageSalary; }
    public void setAverageSalary(double averageSalary) { this.averageSalary = averageSalary; }
    public double getHighestSalary() { return highestSalary; }
    public void setHighestSalary(double highestSalary) { this.highestSalary = highestSalary; }
    public Map<String, Long> getApplicationsByStatus() { return applicationsByStatus; }
    public void setApplicationsByStatus(Map<String, Long> applicationsByStatus) { this.applicationsByStatus = applicationsByStatus; }
    public Map<String, Long> getStudentsByDepartment() { return studentsByDepartment; }
    public void setStudentsByDepartment(Map<String, Long> studentsByDepartment) { this.studentsByDepartment = studentsByDepartment; }
}
