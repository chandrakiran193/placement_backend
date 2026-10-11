package com.placement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "interviews")
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "application_id", nullable = false)
    private JobApplication application;

    @Column(name = "interview_date", nullable = false)
    private LocalDateTime interviewDate;

    @Column(name = "duration_minutes")
    private Integer durationMinutes = 45;

    @Column(name = "interview_round", nullable = false)
    private String interviewRound; // "Technical Round 1", "HR Round", etc.

    @Column(nullable = false)
    private String mode = "ONLINE"; // ONLINE, OFFLINE

    @Column(name = "meeting_link")
    private String meetingLink;

    private String location;

    @Column(columnDefinition = "TEXT")
    private String instructions;

    @Column(nullable = false)
    private String status = "SCHEDULED"; // SCHEDULED, COMPLETED, CANCELLED

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Interview() {
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = "SCHEDULED";
        if (this.mode == null) this.mode = "ONLINE";
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JobApplication getApplication() {
        return application;
    }

    public void setApplication(JobApplication application) {
        this.application = application;
    }

    public LocalDateTime getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(LocalDateTime interviewDate) {
        this.interviewDate = interviewDate;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getInterviewRound() {
        return interviewRound;
    }

    public void setInterviewRound(String interviewRound) {
        this.interviewRound = interviewRound;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMeetingLink() {
        return meetingLink;
    }

    public void setMeetingLink(String meetingLink) {
        this.meetingLink = meetingLink;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
