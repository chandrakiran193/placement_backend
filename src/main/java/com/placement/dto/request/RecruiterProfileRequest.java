package com.placement.dto.request;

public class RecruiterProfileRequest {
    private String name;
    private String phone;
    private String companyName;
    private String companyWebsite;
    private String companyLocation;
    private String designation;
    private String companyDescription;
    private String industryType;

    public RecruiterProfileRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getCompanyWebsite() { return companyWebsite; }
    public void setCompanyWebsite(String companyWebsite) { this.companyWebsite = companyWebsite; }
    public String getCompanyLocation() { return companyLocation; }
    public void setCompanyLocation(String companyLocation) { this.companyLocation = companyLocation; }
    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }
    public String getCompanyDescription() { return companyDescription; }
    public void setCompanyDescription(String companyDescription) { this.companyDescription = companyDescription; }
    public String getIndustryType() { return industryType; }
    public void setIndustryType(String industryType) { this.industryType = industryType; }
}
