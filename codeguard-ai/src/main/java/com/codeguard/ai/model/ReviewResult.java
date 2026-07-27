package com.codeguard.ai.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;


@Entity
@Table(name="review_result")
public class ReviewResult {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne
	@JoinColumn(name="submission_id",nullable=false)
	private CodeSubmission submission;
	
	@Lob
	@Column(columnDefinition="TEXT")
	private String reviewText;
	
	private LocalDateTime createdAt;
	
	public ReviewResult() {
		
	}
	
	public ReviewResult(CodeSubmission submission, String reviewText) {
		this.submission = submission;
		this.reviewText = reviewText;
		this.createdAt = LocalDateTime.now();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public CodeSubmission getSubmission() {
		return submission;
	}

	public void setSubmission(CodeSubmission submission) {
		this.submission = submission;
	}

	public String getReviewText() {
		return reviewText;
	}

	public void setReviewText(String reviewText) {
		this.reviewText = reviewText;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	

}
