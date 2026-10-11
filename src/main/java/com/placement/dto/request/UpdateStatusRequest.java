package com.placement.dto.request;

import jakarta.validation.constraints.NotBlank;

public class UpdateStatusRequest {
    @NotBlank(message = "Status is required")
    private String status; // APPLIED, SHORTLISTED, INTERVIEW_SCHEDULED, SELECTED, REJECTED

    private String feedback;

    public UpdateStatusRequest() {}

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
}
