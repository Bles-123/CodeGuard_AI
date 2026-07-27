package com.codeguard.ai.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name="generated_test")
public class GeneratedTest {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="submission_id",nullable=false)
	private CodeSubmission submission;
	
	@Lob
	@Column(columnDefinition="Text")
	private String testCode;
	
	private LocalDateTime createdAt;
	
	public GeneratedTest() {
		
	}

	public GeneratedTest(CodeSubmission submission, String testCode) {
		this.submission = submission;
		this.testCode = testCode;
		this.createdAt=LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CodeSubmission getSubmission() {
		return submission;
	}

	public void setSubmission(CodeSubmission submission) {
		this.submission = submission;
	}

	public String getTestCode() {
		return testCode;
	}

	public void setTestCode(String testCode) {
		this.testCode = testCode;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	
	
	

}
