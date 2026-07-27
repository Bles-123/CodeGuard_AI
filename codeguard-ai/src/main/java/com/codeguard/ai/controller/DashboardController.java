package com.codeguard.ai.controller;

import com.codeguard.ai.model.*;
import com.codeguard.ai.repository.*;
import com.codeguard.ai.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private CodeSubmissionRepository submissionRepository;

    @Autowired
    private ReviewResultRepository reviewResultRepository;

    @Autowired
    private GeneratedTestRepository generatedTestRepository;

    @Autowired
    private TestExecutionResultRepository testExecutionResultRepository;

    @Autowired
    private CodeReviewService codeReviewService;

    @Autowired
    private TestGeneratorService testGeneratorService;

    @Autowired
    private SandboxExecutionService sandboxExecutionService;

    @GetMapping("/submit")
    public String showSubmitForm() {
        return "submit";
    }

    @PostMapping("/submit")
    public String handleSubmit(
            @RequestParam String title,
            @RequestParam String sourceCode,
            @RequestParam String projectName) {

        // 1. Save the submission
        CodeSubmission submission = new CodeSubmission(title, sourceCode, projectName);
        submissionRepository.save(submission);

        // 2. AI Code Review
        String review = codeReviewService.reviewCode(sourceCode);
        ReviewResult reviewResult = new ReviewResult(submission, review);
        reviewResultRepository.save(reviewResult);

        // 3. Derive a class name for test generation (strip .java extension if present)
        String className = title.replace(".java", "").trim();

        // 4. AI Test Generation
        String testCode = testGeneratorService.generateTest(sourceCode, className);
        GeneratedTest generatedTest = new GeneratedTest(submission, testCode);
        generatedTestRepository.save(generatedTest);

        // 5. Sandbox Execution
        SandboxExecutionService.ExecutionResult execResult = sandboxExecutionService.execute(testCode);
        TestExecutionResult testExecutionResult = new TestExecutionResult(
                generatedTest,
                execResult.getStatus(),
                execResult.getOutput(),
                execResult.getExecutionTimeMs()
        );
        testExecutionResultRepository.save(testExecutionResult);

        return "redirect:/review/" + submission.getId();
    }

    @GetMapping("/review/{id}")
    public String showReview(@PathVariable Long id, Model model) {
        CodeSubmission submission = submissionRepository.findById(id).orElseThrow();
        List<ReviewResult> reviews = reviewResultRepository.findBySubmissionId(id);
        List<GeneratedTest> tests = generatedTestRepository.findBySubmissionId(id);

        model.addAttribute("submission", submission);
        model.addAttribute("reviews", reviews);
        model.addAttribute("tests", tests);

        // Fetch execution results for each generated test
        if (!tests.isEmpty()) {
            List<TestExecutionResult> executionResults = testExecutionResultRepository
                    .findByGeneratedTestId(tests.get(0).getId());
            model.addAttribute("executionResults", executionResults);
        }

        return "review";
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        List<CodeSubmission> submissions = submissionRepository.findAll();
        model.addAttribute("submissions", submissions);
        return "dashboard";
    }
}