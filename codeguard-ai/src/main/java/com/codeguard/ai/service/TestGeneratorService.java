package com.codeguard.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class TestGeneratorService {

    private final ChatClient chatClient;

    public TestGeneratorService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String generateTest(String sourceCode, String className) {
        String prompt = """
                You are a Java code generator. Given the Java class below, generate ONE complete,
                compilable Java file that tests it. Follow this EXACT template structure — only change
                the class name, method calls, and test values to match the given code. Do not deviate
                from this structure in any way.

                TEMPLATE (copy this structure exactly, using System.out.println for every result):

                public class %sTest {
                    public static void main(String[] args) {
                        %s obj = new %s();

                        int result1 = obj.someMethod(10, 2);
                        if (result1 == 5) {
                            System.out.println("PASS: someMethod(10, 2) returned 5");
                        } else {
                            System.out.println("FAIL: someMethod(10, 2) - expected 5 but got " + result1);
                        }
                    }
                }

                class %s {
                    // original class body goes here, copied exactly, WITHOUT the "public" keyword
                }

                RULES:
                1. Every test result MUST use System.out.println("PASS: ...") or System.out.println("FAIL: ...") — never write PASS/FAIL as bare text.
                2. Write 3 to 5 test cases covering normal input, edge cases, and one error case if relevant (e.g. division by zero using try/catch).
                3. Do NOT include a package statement.
                4. Do NOT use JUnit, @Test annotations, or any import from org.junit.
                5. Only ONE public class allowed per file: the test class (%sTest). The original class must NOT have the public keyword.
                6. Output ONLY raw Java code. No markdown fences, no explanations, no text before or after the code.

                Original class to test:
                %s
                """.formatted(className, className, className, className, className, sourceCode);

        String response = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        return cleanCodeResponse(response);
    }

    private String cleanCodeResponse(String response) {
        String cleaned = response.trim();

        // Strip markdown code fences if present
        if (cleaned.startsWith("```")) {
            cleaned = cleaned.replaceAll("^```[a-zA-Z]*\\n", "");
            cleaned = cleaned.replaceAll("```$", "");
        }

        // Defensively strip any package declaration the model adds despite instructions
        cleaned = cleaned.replaceAll("(?m)^\\s*package\\s+[\\w.]+;\\s*\\n", "");

        return cleaned.trim();
    }
}