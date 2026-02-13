package com.example.vehicleservice.jobcard.repository;

import com.example.vehicleservice.jobcard.model.JobCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JobCardRepository extends JpaRepository<JobCard, Integer> {

    @Query("FROM JobCard WHERE jcAptId = :jcAptId AND jcRecordStatus = 'approved'")
    JobCard findJobCardByJcAptId(Integer jcAptId);
}
