package com.placement.config;

import com.placement.entity.*;
import com.placement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Autowired
    private RecruiterProfileRepository recruiterProfileRepository;

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private JobApplicationRepository applicationRepository;

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            return; // Already initialized
        }

        System.out.println("Initializing sample seed data for Smart Placement & Recruitment Management System...");

        // 1. Admin
        User admin = new User(
                "Placement Director",
                "admin@placement.edu",
                passwordEncoder.encode("Admin@123"),
                "+91 9876543210",
                Role.ROLE_ADMIN
        );
        userRepository.save(admin);

        // 2. Recruiters
        User googleRecruiter = new User(
                "Sarah Jenkins",
                "recruiter@google.com",
                passwordEncoder.encode("Recruiter@123"),
                "+91 9811223344",
                Role.ROLE_RECRUITER
        );
        userRepository.save(googleRecruiter);

        RecruiterProfile googleProfile = new RecruiterProfile(googleRecruiter);
        googleProfile.setCompanyName("Google");
        googleProfile.setCompanyWebsite("https://careers.google.com");
        googleProfile.setCompanyLocation("Bangalore, India");
        googleProfile.setDesignation("Lead Technical Recruiter");
        googleProfile.setIndustryType("Technology & Cloud Computing");
        googleProfile.setCompanyDescription("Google's mission is to organize the world's information and make it universally accessible and useful.");
        recruiterProfileRepository.save(googleProfile);

        User msRecruiter = new User(
                "David Chen",
                "recruiter@microsoft.com",
                passwordEncoder.encode("Recruiter@123"),
                "+91 9822334455",
                Role.ROLE_RECRUITER
        );
        userRepository.save(msRecruiter);

        RecruiterProfile msProfile = new RecruiterProfile(msRecruiter);
        msProfile.setCompanyName("Microsoft");
        msProfile.setCompanyWebsite("https://careers.microsoft.com");
        msProfile.setCompanyLocation("Hyderabad, India");
        msProfile.setDesignation("University Talent Acquisition Partner");
        msProfile.setIndustryType("Software & Artificial Intelligence");
        msProfile.setCompanyDescription("Microsoft enables digital transformation for the era of an intelligent cloud and an intelligent edge.");
        recruiterProfileRepository.save(msProfile);

        // 3. Students
        User student1 = new User(
                "Alex Johnson",
                "alex.johnson@student.edu",
                passwordEncoder.encode("Student@123"),
                "+91 9900112233",
                Role.ROLE_STUDENT
        );
        userRepository.save(student1);

        StudentProfile s1Profile = new StudentProfile(student1);
        s1Profile.setRollNumber("CS2022045");
        s1Profile.setDepartment("Computer Science & Engineering");
        s1Profile.setDegree("B.Tech");
        s1Profile.setGraduationYear(2025);
        s1Profile.setCgpa(8.9);
        s1Profile.setSkills("Java, Spring Boot, React, TypeScript, Docker, SQL, Git");
        s1Profile.setBio("Passionate full stack developer with experience in microservices and modern web frameworks.");
        s1Profile.setGithubUrl("https://github.com/alexjohnson");
        s1Profile.setLinkedinUrl("https://linkedin.com/in/alexjohnson");
        studentProfileRepository.save(s1Profile);

        User student2 = new User(
                "Priya Sharma",
                "priya.sharma@student.edu",
                passwordEncoder.encode("Student@123"),
                "+91 9911223344",
                Role.ROLE_STUDENT
        );
        userRepository.save(student2);

        StudentProfile s2Profile = new StudentProfile(student2);
        s2Profile.setRollNumber("IT2022018");
        s2Profile.setDepartment("Information Technology");
        s2Profile.setDegree("B.Tech");
        s2Profile.setGraduationYear(2025);
        s2Profile.setCgpa(9.2);
        s2Profile.setSkills("Python, Machine Learning, React, AWS, FastAPI, PostgreSQL");
        s2Profile.setBio("AI enthusiast and software developer with research background in deep learning models.");
        s2Profile.setGithubUrl("https://github.com/priyasharma");
        s2Profile.setLinkedinUrl("https://linkedin.com/in/priyasharma");
        studentProfileRepository.save(s2Profile);

        User student3 = new User(
                "Rohan Verma",
                "rohan.verma@student.edu",
                passwordEncoder.encode("Student@123"),
                "+91 9922334455",
                Role.ROLE_STUDENT
        );
        userRepository.save(student3);

        StudentProfile s3Profile = new StudentProfile(student3);
        s3Profile.setRollNumber("EC2022072");
        s3Profile.setDepartment("Electronics & Communication");
        s3Profile.setDegree("B.Tech");
        s3Profile.setGraduationYear(2025);
        s3Profile.setCgpa(7.8);
        s3Profile.setSkills("Embedded C, IoT, Python, Computer Networks, Linux");
        s3Profile.setBio("Hardware and firmware enthusiast interested in IoT and distributed communications.");
        studentProfileRepository.save(s3Profile);

        // 4. Job Postings
        JobPosting job1 = new JobPosting();
        job1.setRecruiter(googleRecruiter);
        job1.setTitle("Software Engineer - Backend (Java / Cloud)");
        job1.setCompanyName("Google");
        job1.setJobType("FULL_TIME");
        job1.setLocation("Bangalore, Karnataka");
        job1.setSalaryPackage(24.5);
        job1.setMinCgpa(8.0);
        job1.setEligibleDepartments("Computer Science & Engineering, Information Technology");
        job1.setSkillsRequired("Java, Spring Boot, Microservices, Distributed Systems, SQL");
        job1.setDescription("Join Google's core infrastructure team building planetary-scale web services and distributed caching systems.");
        job1.setResponsibilities("Design, develop, test, deploy, maintain, and enhance large-scale distributed applications.");
        job1.setRequirements("Bachelor's degree in CS/IT. Strong computer science fundamentals and data structures algorithms mastery.");
        job1.setOpeningsCount(5);
        job1.setApplicationDeadline(LocalDate.now().plusDays(30));
        job1.setStatus("ACTIVE");
        jobPostingRepository.save(job1);

        JobPosting job2 = new JobPosting();
        job2.setRecruiter(googleRecruiter);
        job2.setTitle("Frontend Engineer (React / TypeScript)");
        job2.setCompanyName("Google");
        job2.setJobType("FULL_TIME");
        job2.setLocation("Bangalore, Karnataka");
        job2.setSalaryPackage(19.0);
        job2.setMinCgpa(7.5);
        job2.setEligibleDepartments("Computer Science & Engineering, Information Technology, Electronics & Communication");
        job2.setSkillsRequired("React, TypeScript, CSS3, Tailwind, Next.js, Web Performance");
        job2.setDescription("Work on user-facing applications with millions of active users, crafting intuitive and accessible user interfaces.");
        job2.setResponsibilities("Develop responsive, highly accessible web frontends and reusable component libraries.");
        job2.setRequirements("Proficiency in modern JavaScript/TypeScript, React state management, and modern browser standards.");
        job2.setOpeningsCount(3);
        job2.setApplicationDeadline(LocalDate.now().plusDays(25));
        job2.setStatus("ACTIVE");
        jobPostingRepository.save(job2);

        JobPosting job3 = new JobPosting();
        job3.setRecruiter(msRecruiter);
        job3.setTitle("AI & Machine Learning Solutions Engineer");
        job3.setCompanyName("Microsoft");
        job3.setJobType("FULL_TIME");
        job3.setLocation("Hyderabad, Telangana");
        job3.setSalaryPackage(26.0);
        job3.setMinCgpa(8.5);
        job3.setEligibleDepartments("Computer Science & Engineering, Information Technology");
        job3.setSkillsRequired("Python, PyTorch, Azure AI, OpenAI APIs, Vector Databases");
        job3.setDescription("Innovate and build next-generation Copilot extensions and intelligent agentic workflows across enterprise applications.");
        job3.setResponsibilities("Research and integrate foundational LLMs, evaluate model output metrics, build low-latency inference pipelines.");
        job3.setRequirements("Strong analytical background, applied machine learning experience, and solid coding foundations.");
        job3.setOpeningsCount(4);
        job3.setApplicationDeadline(LocalDate.now().plusDays(20));
        job3.setStatus("ACTIVE");
        jobPostingRepository.save(job3);

        JobPosting job4 = new JobPosting();
        job4.setRecruiter(msRecruiter);
        job4.setTitle("Cloud Associate Intern (Summer 2026)");
        job4.setCompanyName("Microsoft");
        job4.setJobType("INTERNSHIP");
        job4.setLocation("Remote / Hyderabad");
        job4.setSalaryPackage(12.0);
        job4.setMinCgpa(7.0);
        job4.setEligibleDepartments("Computer Science & Engineering, Information Technology, Electronics & Communication");
        job4.setSkillsRequired("Cloud Computing, Linux, Networking, Docker, Python");
        job4.setDescription("Exciting 3-month paid summer internship working on Microsoft Azure cloud architectural initiatives.");
        job4.setResponsibilities("Collaborate with senior cloud architects, automate infrastructure scripts, assist with telemetry dashboards.");
        job4.setRequirements("Enrolled in undergraduate degree, graduating 2025 or 2026. Curiosity and passion for cloud architecture.");
        job4.setOpeningsCount(8);
        job4.setApplicationDeadline(LocalDate.now().plusDays(15));
        job4.setStatus("ACTIVE");
        jobPostingRepository.save(job4);

        // 5. Job Applications
        JobApplication app1 = new JobApplication();
        app1.setJob(job1);
        app1.setStudent(student1);
        app1.setStatus(ApplicationStatus.INTERVIEW_SCHEDULED);
        app1.setCoverNote("I am passionate about backend architecture and high throughput systems. My experience with Spring Boot and distributed systems makes me an enthusiastic candidate for Google.");
        app1.setRecruiterFeedback("Strong academic record and impressive open source contributions.");
        applicationRepository.save(app1);

        Interview interview1 = new Interview();
        interview1.setApplication(app1);
        interview1.setInterviewDate(LocalDateTime.now().plusDays(2).withHour(14).withMinute(0));
        interview1.setDurationMinutes(60);
        interview1.setInterviewRound("Technical Round 1: Algorithms & Data Structures");
        interview1.setMode("ONLINE");
        interview1.setMeetingLink("https://meet.google.com/xyz-placement-tech1");
        interview1.setInstructions("Please have your IDE ready for live collaborative coding. Join 5 minutes before scheduled time.");
        interview1.setStatus("SCHEDULED");
        interviewRepository.save(interview1);

        JobApplication app2 = new JobApplication();
        app2.setJob(job3);
        app2.setStudent(student2);
        app2.setStatus(ApplicationStatus.SHORTLISTED);
        app2.setCoverNote("With a deep interest in generative AI and Azure technologies, I look forward to contributing to Microsoft's AI advancements.");
        app2.setRecruiterFeedback("Resume shortlisted for ML assessment round.");
        applicationRepository.save(app2);

        JobApplication app3 = new JobApplication();
        app3.setJob(job2);
        app3.setStudent(student1);
        app3.setStatus(ApplicationStatus.APPLIED);
        app3.setCoverNote("Applying for the Frontend Engineer position at Google.");
        applicationRepository.save(app3);

        System.out.println("Default seed accounts & sample data successfully populated!");
    }
}
