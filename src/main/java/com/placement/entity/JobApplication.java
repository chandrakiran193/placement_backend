package com.placement.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_applications", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"job_id", "student_id"})
})
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "job_id", nullable = false)
    private JobPosting job;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    @JsonIgnoreProperties({"password", "authorities"})
    private User student;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.APPLIED;

    @Column(name = "cover_note", columnDefinition = "TEXT")
    private String coverNote;

    @Column(name = "resume_url")
    private String resumeUrl;

    @Column(name = "recruiter_feedback", columnDefinition = "TEXT")
    private String recruiterFeedback;

    @Column(name = "applied_at", updatable = false)
    private LocalDateTime appliedAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public JobApplication() {
    }

    public JobApplication(JobPosting job, User student, String coverNote, String resumeUrl) {
        this.job = job;
        this.student = student;
        this.coverNote = coverNote;
        this.resumeUrl = resumeUrl;
        this.status = ApplicationStatus.APPLIED;
    }

    @PrePersist
    protected void onCreate() {
        this.appliedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) this.status = ApplicationStatus.APPLIED;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JobPosting getJob() {
        return job;
    }

    public void setJob(JobPosting job) {
        this.job = job;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public String getCoverNote() {
        return coverNote;
    }

    public void setCoverNote(String coverNote) {
        this.coverNote = coverNote;
    }

    public String getResumeUrl() {
        return resumeUrl;
    }

    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }

    public String getRecruiterFeedback() {
        return recruiterFeedback;
    }

    public void setRecruiterFeedback(String recruiterFeedback) {
        this.recruiterFeedback = recruiterFeedback;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
