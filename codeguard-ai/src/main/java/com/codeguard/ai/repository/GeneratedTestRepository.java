package com.codeguard.ai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeguard.ai.model.GeneratedTest;

public interface GeneratedTestRepository extends JpaRepository<GeneratedTest, Long> {
	List<GeneratedTest> findBySubmissionId(Long submissionId);

}
