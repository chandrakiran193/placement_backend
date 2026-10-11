-- =======================================================================
-- Smart Placement & Recruitment Management System - MySQL Schema Reference
-- Note: Spring Boot JPA automatically creates and updates these tables
-- with `spring.jpa.hibernate.ddl-auto=update`.
-- This script is provided as a standalone reference.
-- =======================================================================

CREATE DATABASE IF NOT EXISTS `placement_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `placement_db`;

-- 1. Users Table
CREATE TABLE IF NOT EXISTS `users` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `phone` VARCHAR(50),
    `role` VARCHAR(50) NOT NULL,
    `enabled` BOOLEAN NOT NULL DEFAULT TRUE,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 2. Student Profiles
CREATE TABLE IF NOT EXISTS `student_profiles` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL UNIQUE,
    `roll_number` VARCHAR(100),
    `department` VARCHAR(255),
    `degree` VARCHAR(100),
    `graduation_year` INT,
    `cgpa` DOUBLE,
    `skills` TEXT,
    `bio` TEXT,
    `github_url` VARCHAR(255),
    `linkedin_url` VARCHAR(255),
    `portfolio_url` VARCHAR(255),
    `resume_url` VARCHAR(255),
    `resume_original_name` VARCHAR(255),
    `resume_uploaded_at` DATETIME,
    CONSTRAINT `fk_student_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
);

-- 3. Recruiter Profiles
CREATE TABLE IF NOT EXISTS `recruiter_profiles` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL UNIQUE,
    `company_name` VARCHAR(255),
    `company_website` VARCHAR(255),
    `company_location` VARCHAR(255),
    `designation` VARCHAR(255),
    `company_description` TEXT,
    `industry_type` VARCHAR(255),
    CONSTRAINT `fk_recruiter_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
);

-- 4. Job Postings
CREATE TABLE IF NOT EXISTS `job_postings` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `recruiter_id` BIGINT NOT NULL,
    `title` VARCHAR(255) NOT NULL,
    `company_name` VARCHAR(255) NOT NULL,
    `job_type` VARCHAR(50) NOT NULL,
    `location` VARCHAR(255) NOT NULL,
    `salary_package` DOUBLE,
    `min_cgpa` DOUBLE,
    `eligible_departments` VARCHAR(500),
    `skills_required` VARCHAR(500),
    `description` TEXT,
    `responsibilities` TEXT,
    `requirements` TEXT,
    `openings_count` INT DEFAULT 1,
    `application_deadline` DATE,
    `status` VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `fk_job_recruiter` FOREIGN KEY (`recruiter_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
);

-- 5. Job Applications
CREATE TABLE IF NOT EXISTS `job_applications` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `job_id` BIGINT NOT NULL,
    `student_id` BIGINT NOT NULL,
    `status` VARCHAR(50) NOT NULL DEFAULT 'APPLIED',
    `cover_note` TEXT,
    `resume_url` VARCHAR(255),
    `recruiter_feedback` TEXT,
    `applied_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_job_student` (`job_id`, `student_id`),
    CONSTRAINT `fk_app_job` FOREIGN KEY (`job_id`) REFERENCES `job_postings` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_app_student` FOREIGN KEY (`student_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
);

-- 6. Interviews
CREATE TABLE IF NOT EXISTS `interviews` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `application_id` BIGINT NOT NULL,
    `interview_date` DATETIME NOT NULL,
    `duration_minutes` INT DEFAULT 45,
    `interview_round` VARCHAR(255) NOT NULL,
    `mode` VARCHAR(50) NOT NULL DEFAULT 'ONLINE',
    `meeting_link` VARCHAR(500),
    `location` VARCHAR(255),
    `instructions` TEXT,
    `status` VARCHAR(50) NOT NULL DEFAULT 'SCHEDULED',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_interview_app` FOREIGN KEY (`application_id`) REFERENCES `job_applications` (`id`) ON DELETE CASCADE
);
