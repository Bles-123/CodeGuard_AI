package com.codeguard.ai.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name="test_execution_result")
public class TestExecutionResult {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="generated_test_id",nullable=false)
	private GeneratedTest generatedTest;
	
	private String status;
	
	@Lob
	@Column(columnDefinition="Text")
	private String outputLog;
	
	private Long executionTimeMs;
	
	private LocalDateTime executedAt;
	
	public TestExecutionResult() {
		
	}

	public TestExecutionResult(GeneratedTest generatedTest, String status, String outputLog, Long executionTimeMs) {
		this.generatedTest = generatedTest;
		this.status = status;
		this.outputLog = outputLog;
		this.executionTimeMs = executionTimeMs;
		this.executedAt = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public GeneratedTest getGeneratedTest() {
		return generatedTest;
	}

	public void setGeneratedTest(GeneratedTest generatedTest) {
		this.generatedTest = generatedTest;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOutputLog() {
		return outputLog;
	}

	public void setOutputLog(String outputLog) {
		this.outputLog = outputLog;
	}

	public Long getExecutionTimeMs() {
		return executionTimeMs;
	}

	public void setExecutionTimeMs(Long executionTimeMs) {
		this.executionTimeMs = executionTimeMs;
	}

	public LocalDateTime getExecutedAt() {
		return executedAt;
	}

	public void setExecutedAt(LocalDateTime executedAt) {
		this.executedAt = executedAt;
	}
	

}
