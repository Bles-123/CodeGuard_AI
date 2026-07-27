<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Review — ${submission.title} — CodeGuard AI</title>
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
        <p class="eyebrow">// submission #${submission.id}</p>
        <h1>${submission.title}</h1>
        <p class="meta"><strong>Project:</strong> ${submission.projectName} &nbsp;·&nbsp; <strong>Submitted:</strong> ${submission.submittedAt}</p>

        <h3>Submitted code</h3>
        <pre>${submission.sourceCode}</pre>

        <h3>AI code review</h3>
        <c:forEach var="r" items="${reviews}">
            <div class="review-box">${r.reviewText}</div>
        </c:forEach>

        <h3>AI-generated tests</h3>
        <c:forEach var="t" items="${tests}">
            <pre>${t.testCode}</pre>
        </c:forEach>

        <h3>Execution result</h3>
        <c:forEach var="e" items="${executionResults}">
            <div class="verdict-row">
                <span class="verdict ${e.status}">${e.status}</span>
                <span class="verdict-time">${e.executionTimeMs}ms</span>
            </div>
            <pre>${e.outputLog}</pre>
        </c:forEach>

        <div style="margin-top: 32px;">
            <a class="btn btn-outline" href="${pageContext.request.contextPath}/dashboard">← Back to dashboard</a>
        </div>
    </div>
</body>
</html>