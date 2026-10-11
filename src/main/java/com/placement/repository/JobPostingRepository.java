package com.placement.repository;

import com.placement.entity.JobPosting;
import com.placement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    List<JobPosting> findByRecruiterOrderByCreatedAtDesc(User recruiter);
    List<JobPosting> findByRecruiterIdOrderByCreatedAtDesc(Long recruiterId);
    List<JobPosting> findByStatusOrderByCreatedAtDesc(String status);

    @Query("SELECT j FROM JobPosting j WHERE j.status = 'ACTIVE' " +
           "AND (:keyword IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(j.companyName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(j.skillsRequired) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:jobType IS NULL OR j.jobType = :jobType) " +
           "AND (:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) " +
           "AND (:minSalary IS NULL OR j.salaryPackage >= :minSalary) " +
           "ORDER BY j.createdAt DESC")
    List<JobPosting> searchJobs(
            @Param("keyword") String keyword,
            @Param("jobType") String jobType,
            @Param("location") String location,
            @Param("minSalary") Double minSalary
    );

    long countByStatus(String status);
}
