package com.codeguard.ai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeguard.ai.model.TestExecutionResult;

public interface TestExecutionResultRepository extends JpaRepository<TestExecutionResult, Long> {
	List<TestExecutionResult> findByGeneratedTestId(Long generatedTestId);

}
