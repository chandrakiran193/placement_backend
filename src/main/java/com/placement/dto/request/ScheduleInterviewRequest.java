package com.placement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ScheduleInterviewRequest {
    @NotNull(message = "Application ID is required")
    private Long applicationId;

    @NotNull(message = "Interview date & time is required")
    private LocalDateTime interviewDate;

    private Integer durationMinutes = 45;

    @NotBlank(message = "Interview round is required")
    private String interviewRound; // "Technical Round 1", "HR Round"

    @NotBlank(message = "Mode is required (ONLINE, OFFLINE)")
    private String mode = "ONLINE";

    private String meetingLink;
    private String location;
    private String instructions;

    public ScheduleInterviewRequest() {}

    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }
    public LocalDateTime getInterviewDate() { return interviewDate; }
    public void setInterviewDate(LocalDateTime interviewDate) { this.interviewDate = interviewDate; }
    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
    public String getInterviewRound() { return interviewRound; }
    public void setInterviewRound(String interviewRound) { this.interviewRound = interviewRound; }
    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }
    public String getMeetingLink() { return meetingLink; }
    public void setMeetingLink(String meetingLink) { this.meetingLink = meetingLink; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
}
