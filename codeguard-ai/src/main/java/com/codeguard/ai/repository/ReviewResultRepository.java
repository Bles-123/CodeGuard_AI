package com.codeguard.ai.repository;


import com.codeguard.ai.model.ReviewResult;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewResultRepository extends JpaRepository<ReviewResult, Long> {
    List<ReviewResult> findBySubmissionId(Long submissionId);

}
