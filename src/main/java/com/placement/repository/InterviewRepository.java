package com.placement.repository;

import com.placement.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {
    List<Interview> findByApplicationIdOrderByInterviewDateAsc(Long applicationId);

    @Query("SELECT i FROM Interview i WHERE i.application.job.recruiter.id = :recruiterId ORDER BY i.interviewDate DESC")
    List<Interview> findByRecruiterId(@Param("recruiterId") Long recruiterId);

    @Query("SELECT i FROM Interview i WHERE i.application.student.id = :studentId ORDER BY i.interviewDate DESC")
    List<Interview> findByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT COUNT(i) FROM Interview i WHERE i.application.job.recruiter.id = :recruiterId")
    long countByRecruiterId(@Param("recruiterId") Long recruiterId);
}
