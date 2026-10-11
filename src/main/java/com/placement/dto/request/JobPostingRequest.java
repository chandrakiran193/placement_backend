package com.placement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class JobPostingRequest {
    @NotBlank(message = "Job title is required")
    private String title;

    private String companyName;

    @NotBlank(message = "Job type is required (e.g. FULL_TIME, INTERNSHIP)")
    private String jobType;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Salary package is required")
    private Double salaryPackage;

    private Double minCgpa;
    private String eligibleDepartments;
    private String skillsRequired;
    private String description;
    private String responsibilities;
    private String requirements;
    private Integer openingsCount = 1;
    private LocalDate applicationDeadline;
    private String status = "ACTIVE";

    public JobPostingRequest() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getJobType() { return jobType; }
    public void setJobType(String jobType) { this.jobType = jobType; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Double getSalaryPackage() { return salaryPackage; }
    public void setSalaryPackage(Double salaryPackage) { this.salaryPackage = salaryPackage; }
    public Double getMinCgpa() { return minCgpa; }
    public void setMinCgpa(Double minCgpa) { this.minCgpa = minCgpa; }
    public String getEligibleDepartments() { return eligibleDepartments; }
    public void setEligibleDepartments(String eligibleDepartments) { this.eligibleDepartments = eligibleDepartments; }
    public String getSkillsRequired() { return skillsRequired; }
    public void setSkillsRequired(String skillsRequired) { this.skillsRequired = skillsRequired; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getResponsibilities() { return responsibilities; }
    public void setResponsibilities(String responsibilities) { this.responsibilities = responsibilities; }
    public String getRequirements() { return requirements; }
    public void setRequirements(String requirements) { this.requirements = requirements; }
    public Integer getOpeningsCount() { return openingsCount; }
    public void setOpeningsCount(Integer openingsCount) { this.openingsCount = openingsCount; }
    public LocalDate getApplicationDeadline() { return applicationDeadline; }
    public void setApplicationDeadline(LocalDate applicationDeadline) { this.applicationDeadline = applicationDeadline; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
