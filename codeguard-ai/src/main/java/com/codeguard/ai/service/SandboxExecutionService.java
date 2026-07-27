package com.codeguard.ai.service;

import org.springframework.stereotype.Service;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SandboxExecutionService {

    private static final int TIMEOUT_SECONDS = 10;
    private static final Pattern CLASS_NAME_PATTERN = Pattern.compile("public\\s+class\\s+(\\w+)");

    public ExecutionResult execute(String testCode) {
        Path tempDir = null;
        try {
            // 1. Create an isolated temp directory for this run
            tempDir = Files.createTempDirectory("codeguard-sandbox-");

            // 2. Extract the public class name so the filename matches (Java requirement)
            String className = extractPublicClassName(testCode);
            if (className == null) {
                return new ExecutionResult("COMPILE_ERROR", "Could not find a public class in generated code.", 0L);
            }

            Path javaFile = tempDir.resolve(className + ".java");
            Files.writeString(javaFile, testCode);

            // 3. Compile
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            if (compiler == null) {
                return new ExecutionResult("ERROR", "No system Java compiler available. Ensure you're running on a JDK, not a JRE.", 0L);
            }

            StringWriter compilerOutput = new StringWriter();
            int compileResult = compiler.run(null, null, new WriterOutputStream(compilerOutput),
                    javaFile.toAbsolutePath().toString());

            if (compileResult != 0) {
                return new ExecutionResult("COMPILE_ERROR", compilerOutput.toString(), 0L);
            }

            // 4. Execute in a separate, resource-limited process
            long startTime = System.currentTimeMillis();

            ProcessBuilder pb = new ProcessBuilder(
                    "java",
                    "-Xmx128m",           // memory cap: 128MB
                    "-Xss4m",             // stack size cap
                    "-cp", tempDir.toAbsolutePath().toString(),
                    className
            );
            pb.redirectErrorStream(true);
            pb.directory(tempDir.toFile());

            Process process = pb.start();

            // Capture output while enforcing timeout
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            boolean finished = process.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            long executionTime = System.currentTimeMillis() - startTime;

            if (!finished) {
                process.destroyForcibly();
                return new ExecutionResult("TIMEOUT", "Execution exceeded " + TIMEOUT_SECONDS + " seconds and was terminated.", executionTime);
            }

            String status = determineStatus(output.toString());
            return new ExecutionResult(status, output.toString(), executionTime);

        } catch (Exception e) {
            return new ExecutionResult("ERROR", "Sandbox execution failed: " + e.getMessage(), 0L);
        } finally {
            // 5. Clean up temp files regardless of outcome
            if (tempDir != null) {
                cleanupDirectory(tempDir);
            }
        }
    }

    private String extractPublicClassName(String code) {
        Matcher matcher = CLASS_NAME_PATTERN.matcher(code);
        return matcher.find() ? matcher.group(1) : null;
    }

    private String determineStatus(String output) {
        if (output.contains("FAIL:")) {
            return output.contains("PASS:") ? "PARTIAL" : "FAILED";
        }
        if (output.contains("PASS:")) {
            return "PASSED";
        }
        return "UNKNOWN";
    }

    private void cleanupDirectory(Path dir) {
        try {
            Files.walk(dir)
                    .sorted((a, b) -> b.compareTo(a)) // delete files before directories
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException ignored) {
                        }
                    });
        } catch (IOException ignored) {
        }
    }

    // Simple output stream wrapper for compiler diagnostics
    private static class WriterOutputStream extends OutputStream {
        private final Writer writer;

        WriterOutputStream(Writer writer) {
            this.writer = writer;
        }

        @Override
        public void write(int b) throws IOException {
            writer.write(b);
        }
    }

    // Result holder
    public static class ExecutionResult {
        private final String status;
        private final String output;
        private final long executionTimeMs;

        public ExecutionResult(String status, String output, long executionTimeMs) {
            this.status = status;
            this.output = output;
            this.executionTimeMs = executionTimeMs;
        }

        public String getStatus() { return status; }
        public String getOutput() { return output; }
        public long getExecutionTimeMs() { return executionTimeMs; }
    }
}