<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Portal - SRMS</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="app-container">
        <div class="sidebar-overlay" id="sidebarOverlay"></div>

        <!-- Sidebar Navigation -->
        <div class="sidebar">
            <div class="sidebar-header">
                <i class="bi bi-building" style="font-size: 1.35rem;"></i>
                <a href="${pageContext.request.contextPath}/student/dashboard" class="sidebar-brand">SRMS STUDENT</a>
            </div>
            <ul class="sidebar-menu">
                <li class="sidebar-item">
                    <a href="${pageContext.request.contextPath}/student/dashboard" class="sidebar-link active">
                        <i class="bi bi-speedometer2"></i> Dashboard
                    </a>
                </li>
                <li class="sidebar-item">
                    <a href="${pageContext.request.contextPath}/student/attendance" class="sidebar-link">
                        <i class="bi bi-pie-chart"></i> Attendance Report
                    </a>
                </li>
                <li class="sidebar-item">
                    <a href="${pageContext.request.contextPath}/student/marks" class="sidebar-link">
                        <i class="bi bi-journal-text"></i> Internal Marks
                    </a>
                </li>
                <li class="sidebar-item">
                    <a href="${pageContext.request.contextPath}/student/fees" class="sidebar-link">
                        <i class="bi bi-credit-card"></i> Fee Status & Receipts
                    </a>
                </li>
                <li class="sidebar-item">
                    <a href="${pageContext.request.contextPath}/student/timetable" class="sidebar-link">
                        <i class="bi bi-calendar-event"></i> Class Timetable
                    </a>
                </li>
            </ul>
        </div>

        <div class="main-wrapper">
            <div class="top-navbar">
                <div class="navbar-left">
                    <button class="sidebar-toggle" id="sidebarToggle" aria-label="Toggle sidebar">
                        <i class="bi bi-list"></i>
                    </button>
                    <div class="page-title">Student Portal Dashboard</div>
                </div>
                <div class="user-profile-badge">
                    <div class="user-avatar">S</div>
                    <div class="user-info">
                        <span class="user-name">${student.name}</span>
                        <span class="user-role">${student.rollNo} &bull; S${student.semester} ${student.deptName}</span>
                    </div>
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline btn-sm ms-3" id="logoutBtn">Logout</a>
                </div>
            </div>

            <div class="content-body">
                <!-- Welcome Banner -->
                <div class="card welcome-banner mb-4">
                    <h2>Welcome, ${student.name}!</h2>
                    <p>
                        KTU Roll No: <strong>${student.rollNo}</strong> &bull; Semester ${student.semester} B.Tech (${student.deptName}) &bull; Batch ${student.batchYear}
                    </p>
                </div>

                <!-- Attendance Warning Alert -->
                <c:if test="${attendanceSummary.belowThreshold}">
                    <div class="card attendance-alert-card mb-4">
                        <div style="display: flex; align-items: flex-start; gap: 1rem;">
                            <div style="font-size: 2rem; color: var(--error); line-height: 1;">
                                <i class="bi bi-exclamation-triangle-fill"></i>
                            </div>
                            <div style="flex: 1;">
                                <h3 class="attendance-alert-card h3">
                                    ⚠️ Attendance Warning: Low Attendance Alert
                                </h3>
                                <p>
                                    Your current attendance is <strong>${attendanceSummary.attendancePercentage}%</strong>. Your attendance is below the required <strong>75%</strong>. Please improve your attendance to avoid academic issues.
                                </p>
                                <div class="attendance-alert-action">
                                    <i class="bi bi-calculator"></i> Action Required: You need to attend at least <strong>${attendanceSummary.classesNeededFor75}</strong> consecutive class(es) to reach 75%.
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>

                <!-- Quick Stats -->
                <div class="stat-grid mb-4">
                    <div class="card stat-card">
                        <div class="stat-icon ${attendanceSummary.belowThreshold ? 'danger' : 'success'}">
                            <i class="bi bi-pie-chart"></i>
                        </div>
                        <div class="stat-details">
                            <h4>Attendance</h4>
                            <div class="stat-value" style="color: ${attendanceSummary.belowThreshold ? 'var(--error)' : 'var(--success)'};">
                                ${attendanceSummary.attendancePercentage}%
                            </div>
                            <span class="text-muted">
                                ${attendanceSummary.classesAttended} / ${attendanceSummary.totalClasses} Classes
                            </span>
                        </div>
                    </div>

                    <div class="card stat-card">
                        <div class="stat-icon ${attendanceSummary.belowThreshold ? 'danger' : 'success'}">
                            <i class="bi bi-shield-check"></i>
                        </div>
                        <div class="stat-details">
                            <h4>Attendance Status</h4>
                            <div class="stat-value" style="font-size: 1.1rem; margin-top: 0.4rem;">
                                <c:choose>
                                    <c:when test="${attendanceSummary.belowThreshold}">
                                        <span style="color: var(--error);"><i class="bi bi-exclamation-octagon-fill"></i> 🔴 Below Requirement</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span style="color: var(--success);"><i class="bi bi-check-circle-fill"></i> 🟢 Safe / Good</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <span class="text-muted">Threshold: 75%</span>
                        </div>
                    </div>

                    <div class="card stat-card">
                        <div class="stat-icon primary">
                            <i class="bi bi-journal-text"></i>
                        </div>
                        <div class="stat-details">
                            <h4>Session Average</h4>
                            <div class="stat-value" style="color: var(--accent);">
                                <c:choose>
                                    <c:when test="${sessionAverage > 0.0}">${sessionAverage}%</c:when>
                                    <c:otherwise><span style="font-size: 0.95rem; color: var(--text-muted);">No marks yet</span></c:otherwise>
                                </c:choose>
                            </div>
                            <span class="text-muted">Series Tests & Assignments</span>
                        </div>
                    </div>

                    <div class="card stat-card">
                        <div class="stat-icon warning">
                            <i class="bi bi-award"></i>
                        </div>
                        <div class="stat-details">
                            <h4>End Exam Average</h4>
                            <div class="stat-value" style="color: var(--accent-hover);">
                                <c:choose>
                                    <c:when test="${endExamAverage > 0.0}">${endExamAverage}%</c:when>
                                    <c:otherwise><span style="font-size: 0.95rem; color: var(--text-muted);">No marks yet</span></c:otherwise>
                                </c:choose>
                            </div>
                            <span class="text-muted">Semester Final Exam</span>
                        </div>
                    </div>
                </div>

                <!-- Recent Marks Preview -->
                <div class="card table-card mb-4">
                    <div class="table-header-bar">
                        <div class="table-title"><i class="bi bi-journal-text"></i> Internal Examination Scores</div>
                        <a href="${pageContext.request.contextPath}/student/marks" class="btn btn-primary btn-sm" id="fullMarksheetBtn">Full Marksheet</a>
                    </div>
                    <div class="table-responsive">
                        <table class="custom-table" id="marksTable">
                            <thead>
                                <tr>
                                    <th>Subject Code</th>
                                    <th>Subject Name</th>
                                    <th>Exam Type</th>
                                    <th>Marks Obtained</th>
                                    <th>Max Marks</th>
                                    <th>Score %</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${empty recentMarks}">
                                        <tr><td colspan="6" class="text-center text-muted">No examination marks published yet.</td></tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="m" items="${recentMarks}">
                                            <tr>
                                                <td><code>${m.subjectCode}</code></td>
                                                <td>${m.subjectName}</td>
                                                <td><span class="badge-pill badge-primary">${m.examType}</span></td>
                                                <td><strong>${m.marksObtained}</strong></td>
                                                <td>${m.maxMarks}</td>
                                                <td>
                                                    <span class="badge-pill ${m.percentage >= 50.0 ? 'badge-success' : 'badge-danger'}">
                                                        ${String.format("%.1f", m.percentage)}%
                                                    </span>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>

                <!-- Notices -->
                <div class="card">
                    <div class="section-heading"><i class="bi bi-megaphone"></i> Campus Announcements & Circulars</div>
                    <c:forEach var="a" items="${announcements}">
                        <div class="announcement-item">
                            <strong class="ann-title">${a.title}</strong>
                            <p class="ann-body">${a.message}</p>
                            <span class="ann-meta">${a.postedAt} &bull; Posted by Admin</span>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
