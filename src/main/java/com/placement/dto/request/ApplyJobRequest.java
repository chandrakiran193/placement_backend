package com.placement.dto.request;

public class ApplyJobRequest {
    private String coverNote;
    private String resumeUrl;

    public ApplyJobRequest() {}

    public ApplyJobRequest(String coverNote, String resumeUrl) {
        this.coverNote = coverNote;
        this.resumeUrl = resumeUrl;
    }

    public String getCoverNote() { return coverNote; }
    public void setCoverNote(String coverNote) { this.coverNote = coverNote; }
    public String getResumeUrl() { return resumeUrl; }
    public void setResumeUrl(String resumeUrl) { this.resumeUrl = resumeUrl; }
}
