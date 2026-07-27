package com.codeguard.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class CodeReviewService {
	private final ChatClient chatClient;
	
	public CodeReviewService(ChatClient.Builder chatClientBuilder) {
		this.chatClient=chatClientBuilder.build();
	}
	
	public String reviewCode(String sourceCode) {
		String prompt="""
				You are a senior Java code reviewer. Analyze the following Java code
                and provide a clear, concise review covering:
                1. Any bugs or potential runtime errors
                2. Bad practices or code smells
                3. Suggestions for improvement

                Keep your response well-organized with clear sections. Be specific about line-level issues where possible.

                Code to review:
                %s
				""".formatted(sourceCode);
		return chatClient.prompt()
				.user(prompt)
				.call()
				.content();
		
	}

}
