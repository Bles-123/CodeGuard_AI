<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>New Submission — CodeGuard AI</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="topbar">
        <div class="topbar-inner">
            <a class="wordmark" href="${pageContext.request.contextPath}/dashboard">CODE<span>GUARD</span>_AI</a>
            <div class="nav-links">
                <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
                <a href="${pageContext.request.contextPath}/submit">New Submission</a>
            </div>
        </div>
    </div>

    <div class="shell">
        <p class="eyebrow">// new submission</p>
        <h1>Submit code for review</h1>
        <p class="meta">Runs an AI code review, generates tests, and executes them in an isolated sandbox.</p>

        <form action="${pageContext.request.contextPath}/submit" method="post">
            <label>Title</label>
            <input type="text" name="title" placeholder="e.g. Divide.java" required>

            <label>Project</label>
            <input type="text" name="projectName" placeholder="e.g. TestProject" required>

            <label>Source code</label>
            <textarea name="sourceCode" placeholder="Paste your Java class here..." required></textarea>

            <div style="margin-top: 28px;">
                <button type="submit" class="btn">Run analysis →</button>
            </div>
        </form>
    </div>
</body>
</html>