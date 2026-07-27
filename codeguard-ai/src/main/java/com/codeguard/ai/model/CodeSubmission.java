package com.codeguard.ai.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name="code_submission")
public class CodeSubmission {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false)
	private String title;
	
	@Lob
	@Column(columnDefinition = "TEXT", nullable=false)
	private String sourceCode;
	
	private String projectName;
	
	private LocalDateTime submittedAt;
	
	public CodeSubmission() {
		
	}
	
	public CodeSubmission(String title, String sourceCode, String projectName) {
		this.title=title;
		this.sourceCode=sourceCode;
		this.projectName=projectName;
		this.submittedAt=LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public LocalDateTime getSubmittedAt() {
		return submittedAt;
	}

	public void setSubmittedAt(LocalDateTime submittedAt) {
		this.submittedAt = submittedAt;
	}
	
	

}
