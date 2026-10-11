package com.placement.repository;

import com.placement.entity.ApplicationStatus;
import com.placement.entity.JobApplication;
import com.placement.entity.JobPosting;
import com.placement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByStudentOrderByAppliedAtDesc(User student);
    List<JobApplication> findByStudentIdOrderByAppliedAtDesc(Long studentId);
    List<JobApplication> findByJobOrderByAppliedAtDesc(JobPosting job);
    List<JobApplication> findByJobIdOrderByAppliedAtDesc(Long jobId);
    
    boolean existsByJobIdAndStudentId(Long jobId, Long studentId);
    Optional<JobApplication> findByJobIdAndStudentId(Long jobId, Long studentId);

    @Query("SELECT a FROM JobApplication a WHERE a.job.recruiter.id = :recruiterId ORDER BY a.appliedAt DESC")
    List<JobApplication> findByRecruiterId(@Param("recruiterId") Long recruiterId);

    long countByStatus(ApplicationStatus status);

    @Query("SELECT COUNT(a) FROM JobApplication a WHERE a.job.recruiter.id = :recruiterId")
    long countByRecruiterId(@Param("recruiterId") Long recruiterId);

    @Query("SELECT COUNT(a) FROM JobApplication a WHERE a.job.recruiter.id = :recruiterId AND a.status = :status")
    long countByRecruiterIdAndStatus(@Param("recruiterId") Long recruiterId, @Param("status") ApplicationStatus status);

    @Query("SELECT COUNT(DISTINCT a.student.id) FROM JobApplication a WHERE a.status = 'SELECTED'")
    long countDistinctPlacedStudents();
}
