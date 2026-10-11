package com.placement.dto.response;

import com.placement.entity.ApplicationStatus;
import java.time.LocalDateTime;

public class JobApplicantResponse {
    private Long applicationId;
    private Long jobId;
    private String jobTitle;
    private Long studentId;
    private String studentName;
    private String studentEmail;
    private String studentPhone;
    private String rollNumber;
    private String department;
    private String degree;
    private Integer graduationYear;
    private Double cgpa;
    private String skills;
    private String resumeUrl;
    private String resumeOriginalName;
    private String coverNote;
    private ApplicationStatus status;
    private String recruiterFeedback;
    private LocalDateTime appliedAt;

    public JobApplicantResponse() {}

    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }
    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }
    public String getStudentPhone() { return studentPhone; }
    public void setStudentPhone(String studentPhone) { this.studentPhone = studentPhone; }
    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getDegree() { return degree; }
    public void setDegree(String degree) { this.degree = degree; }
    public Integer getGraduationYear() { return graduationYear; }
    public void setGraduationYear(Integer graduationYear) { this.graduationYear = graduationYear; }
    public Double getCgpa() { return cgpa; }
    public void setCgpa(Double cgpa) { this.cgpa = cgpa; }
    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }
    public String getResumeUrl() { return resumeUrl; }
    public void setResumeUrl(String resumeUrl) { this.resumeUrl = resumeUrl; }
    public String getResumeOriginalName() { return resumeOriginalName; }
    public void setResumeOriginalName(String resumeOriginalName) { this.resumeOriginalName = resumeOriginalName; }
    public String getCoverNote() { return coverNote; }
    public void setCoverNote(String coverNote) { this.coverNote = coverNote; }
    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }
    public String getRecruiterFeedback() { return recruiterFeedback; }
    public void setRecruiterFeedback(String recruiterFeedback) { this.recruiterFeedback = recruiterFeedback; }
    public LocalDateTime getAppliedAt() { return appliedAt; }
    public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; }
}
