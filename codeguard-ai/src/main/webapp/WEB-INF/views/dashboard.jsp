<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Dashboard — CodeGuard AI</title>
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
        <p class="eyebrow">// submission log</p>
        <h1>Dashboard</h1>
        <p class="meta">All submitted code, reviewed and tested by CodeGuard AI.</p>

        <div style="margin: 24px 0;">
            <a class="btn" href="${pageContext.request.contextPath}/submit">+ New submission</a>
        </div>

        <c:choose>
            <c:when test="${empty submissions}">
                <div class="panel empty-state">No submissions yet. Submit your first class to see it here.</div>
            </c:when>
            <c:otherwise>
                <table>
                    <tr>
                        <th>ID</th>
                        <th>Title</th>
                        <th>Project</th>
                        <th>Submitted</th>
                    </tr>
                    <c:forEach var="s" items="${submissions}">
                        <tr>
                            <td class="id-cell">#${s.id}</td>
                            <td><a href="${pageContext.request.contextPath}/review/${s.id}">${s.title}</a></td>
                            <td>${s.projectName}</td>
                            <td class="id-cell">${s.submittedAt}</td>
                        </tr>
                    </c:forEach>
                </table>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>