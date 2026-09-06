<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Parent Portal - SRMS</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="app-container">
        <div class="sidebar-overlay" id="sidebarOverlay"></div>

        <div class="sidebar">
            <div class="sidebar-header">
                <i class="bi bi-building" style="font-size: 1.35rem;"></i>
                <a href="${pageContext.request.contextPath}/parent/dashboard" class="sidebar-brand">SRMS PARENT</a>
            </div>
            <ul class="sidebar-menu">
                <li class="sidebar-item">
                    <a href="${pageContext.request.contextPath}/parent/dashboard" class="sidebar-link active">
                        <i class="bi bi-speedometer2"></i> Parent Dashboard
                    </a>
                </li>
                <li class="sidebar-item">
                    <a href="#attendance" onclick="scrollToSection('sec-attendance')" class="sidebar-link">
                        <i class="bi bi-pie-chart"></i> Attendance
                    </a>
                </li>
                <li class="sidebar-item">
                    <a href="#session-marks" onclick="scrollToSection('sec-session-marks')" class="sidebar-link">
                        <i class="bi bi-journal-text"></i> Session Marks
                    </a>
                </li>
                <li class="sidebar-item">
                    <a href="#end-exam" onclick="scrollToSection('sec-end-exam')" class="sidebar-link">
                        <i class="bi bi-award"></i> End Exam Marks
                    </a>
                </li>
                <li class="sidebar-item">
                    <a href="#performance" onclick="scrollToSection('sec-performance')" class="sidebar-link">
                        <i class="bi bi-bar-chart"></i> Overall Performance
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
                    <div class="page-title">Parent Academic Portal</div>
                </div>
                <div class="user-profile-badge">
                    <div class="user-avatar" style="background: linear-gradient(135deg, #7C3AED 0%, #6D28D9 100%);">P</div>
                    <div class="user-info">
                        <span class="user-name">${parent.name}</span>
                        <span class="user-role">Parent Account</span>
                    </div>
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline btn-sm ms-3" id="logoutBtn">Logout</a>
                </div>
            </div>

            <div class="content-body">
                <!-- Welcome Header -->
                <div class="card welcome-banner purple mb-4">
                    <h2>Welcome, ${parent.name}!</h2>
                    <p>Parent & Guardian Portal &bull; Academic Performance & Attendance Monitoring System</p>
                </div>

                <c:choose>
                    <c:when test="${empty linkedStudents}">
                        <div class="card p-4 text-center">
                            <i class="bi bi-info-circle text-muted" style="font-size: 2.5rem;"></i>
                            <h3 style="margin-top: 0.75rem;">No Students Linked</h3>
                            <p class="text-muted">There are currently no student records linked to your parent account. Please contact the institution administrator.</p>
                        </div>
                    </c:when>
                    <c:otherwise>

                        <!-- Multi-Child Switcher -->
                        <div class="child-selector-bar">
                            <div style="display: flex; align-items: center; gap: 0.5rem; flex-wrap: wrap;">
                                <i class="bi bi-people-fill" style="font-size: 1.25rem; color: var(--accent);"></i>
                                <span style="font-weight: 700; color: var(--text);">Linked Children:</span>
                                <div style="display: flex; gap: 0.5rem; flex-wrap: wrap;">
                                    <c:forEach var="child" items="${linkedStudents}">
                                        <a href="${pageContext.request.contextPath}/parent/dashboard?studentId=${child.studentId}"
                                           class="child-pill ${child.studentId == selectedStudent.studentId ? 'active' : ''}">
                                            <i class="bi bi-person me-1"></i> ${child.name} (${child.rollNo})
                                        </a>
                                    </c:forEach>
                                </div>
                            </div>
                            <span class="badge-pill badge-primary">Showing: ${selectedStudent.name}</span>
                        </div>

                        <!-- Attendance Alert -->
                        <c:if test="${attendanceSummary.belowThreshold}">
                            <div class="card attendance-alert-card mb-4">
                                <div style="display: flex; align-items: flex-start; gap: 1rem;">
                                    <div style="font-size: 2.2rem; color: var(--error); line-height: 1;">
                                        <i class="bi bi-exclamation-triangle-fill"></i>
                                    </div>
                                    <div style="flex: 1;">
                                        <h3>⚠️ Attendance Alert for ${selectedStudent.name}</h3>
                                        <p>
                                            Your child's attendance is currently <strong>${attendanceSummary.attendancePercentage}%</strong>, which is below the required <strong>75%</strong>. Please monitor their attendance.
                                        </p>
                                        <div class="attendance-alert-action">
                                            <i class="bi bi-calculator"></i> Required Action: ${selectedStudent.name} needs to attend at least <strong>${attendanceSummary.classesNeededFor75}</strong> consecutive class(es) to reach 75%.
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:if>

                        <!-- Student Overview -->
                        <div class="card student-overview-card mb-4">
                            <div class="section-heading"><i class="bi bi-person-badge"></i> Student Overview</div>
                            <div class="overview-grid">
                                <div><span class="text-muted">Student Name:</span><br><strong>${selectedStudent.name}</strong></div>
                                <div><span class="text-muted">Student ID / Roll No:</span><br><code>${selectedStudent.rollNo}</code></div>
                                <div><span class="text-muted">Class / Department:</span><br><strong>Semester ${selectedStudent.semester} (${selectedStudent.deptName})</strong></div>
                                <div><span class="text-muted">Attendance Status:</span><br>
                                    <c:choose>
                                        <c:when test="${attendanceSummary.belowThreshold}">
                                            <span class="badge-pill badge-danger">🔴 Below Requirement (${attendanceSummary.attendancePercentage}%)</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge-pill badge-success">🟢 Safe / Good (${attendanceSummary.attendancePercentage}%)</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>

                        <!-- Navigation Tabs -->
                        <div class="nav-tabs-custom">
                            <a href="#sec-overview" class="nav-tab-item active" onclick="switchTab(this, 'sec-overview')">
                                <i class="bi bi-grid-1x2-fill"></i> Overview
                            </a>
                            <a href="#sec-attendance" class="nav-tab-item" onclick="switchTab(this, 'sec-attendance')">
                                <i class="bi bi-pie-chart-fill"></i> Attendance
                            </a>
                            <a href="#sec-session-marks" class="nav-tab-item" onclick="switchTab(this, 'sec-session-marks')">
                                <i class="bi bi-journal-text"></i> Session Marks
                            </a>
                            <a href="#sec-end-exam" class="nav-tab-item" onclick="switchTab(this, 'sec-end-exam')">
                                <i class="bi bi-award-fill"></i> End Exam
                            </a>
                            <a href="#sec-performance" class="nav-tab-item" onclick="switchTab(this, 'sec-performance')">
                                <i class="bi bi-bar-chart-fill"></i> Performance
                            </a>
                        </div>

                        <!-- Stat Cards -->
                        <div class="stat-grid mb-4">
                            <div class="card stat-card">
                                <div class="stat-icon ${attendanceSummary.belowThreshold ? 'danger' : 'success'}">
                                    <i class="bi bi-pie-chart"></i>
                                </div>
                                <div class="stat-details">
                                    <h4>Child Attendance</h4>
                                    <div class="stat-value" style="color: ${attendanceSummary.belowThreshold ? 'var(--error)' : 'var(--success)'};">
                                        ${attendanceSummary.attendancePercentage}%
                                    </div>
                                    <span class="text-muted">${attendanceSummary.classesAttended} of ${attendanceSummary.totalClasses} Attended</span>
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
                                                <span style="color: var(--error);">🔴 Below Requirement</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span style="color: var(--success);">🟢 Safe / Good</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <span class="text-muted">Requirement: 75%</span>
                                </div>
                            </div>

                            <div class="card stat-card">
                                <div class="stat-icon primary"><i class="bi bi-journal-check"></i></div>
                                <div class="stat-details">
                                    <h4>Session Average</h4>
                                    <div class="stat-value" style="color: var(--accent);">
                                        <c:choose>
                                            <c:when test="${sessionAverage > 0.0}">${sessionAverage}%</c:when>
                                            <c:otherwise><span style="font-size: 0.9rem; color: var(--text-muted);">No marks yet</span></c:otherwise>
                                        </c:choose>
                                    </div>
                                    <span class="text-muted">Series Tests & Assignments</span>
                                </div>
                            </div>

                            <div class="card stat-card">
                                <div class="stat-icon warning"><i class="bi bi-award"></i></div>
                                <div class="stat-details">
                                    <h4>End Exam Average</h4>
                                    <div class="stat-value" style="color: var(--accent-hover);">
                                        <c:choose>
                                            <c:when test="${endExamAverage > 0.0}">${endExamAverage}%</c:when>
                                            <c:otherwise><span style="font-size: 0.9rem; color: var(--text-muted);">No marks yet</span></c:otherwise>
                                        </c:choose>
                                    </div>
                                    <span class="text-muted">Semester Final Marks</span>
                                </div>
                            </div>
                        </div>

                        <!-- Attendance Details -->
                        <div id="sec-attendance" class="tab-section card table-card mb-4">
                            <div class="table-header-bar">
                                <div class="table-title"><i class="bi bi-pie-chart"></i> Attendance Log - ${selectedStudent.name}</div>
                                <span class="badge-pill ${attendanceSummary.belowThreshold ? 'badge-danger' : 'badge-success'}">
                                    Overall: ${attendanceSummary.attendancePercentage}%
                                </span>
                            </div>
                            <div class="table-responsive">
                                <table class="custom-table" id="attendanceTable">
                                    <thead>
                                        <tr>
                                            <th>Date</th>
                                            <th>Period</th>
                                            <th>Subject Code</th>
                                            <th>Subject Name</th>
                                            <th>Status</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:choose>
                                            <c:when test="${empty attendanceRecords}">
                                                <tr><td colspan="5" class="text-center text-muted" style="padding: 1.5rem;">No attendance records recorded yet.</td></tr>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="att" items="${attendanceRecords}">
                                                    <tr>
                                                        <td>${att.attendanceDate}</td>
                                                        <td>Period ${att.periodNo}</td>
                                                        <td><code>${att.subjectCode}</code></td>
                                                        <td>${att.subjectName}</td>
                                                        <td>
                                                            <span class="badge-pill ${'PRESENT' eq att.status ? 'badge-success' : 'badge-danger'}">
                                                                ${att.status}
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

                        <!-- Session Marks -->
                        <div id="sec-session-marks" class="tab-section card table-card mb-4">
                            <div class="table-header-bar">
                                <div class="table-title"><i class="bi bi-journal-text"></i> Session Marks (Series Tests & Assignments)</div>
                                <span class="badge-pill badge-primary">Session Average: ${sessionAverage}%</span>
                            </div>
                            <div class="table-responsive">
                                <table class="custom-table" id="sessionMarksTable">
                                    <thead>
                                        <tr>
                                            <th>Subject</th>
                                            <th>Exam / Session</th>
                                            <th>Maximum Marks</th>
                                            <th>Marks Obtained</th>
                                            <th>Percentage</th>
                                            <th>Grade</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:choose>
                                            <c:when test="${empty sessionMarks}">
                                                <tr><td colspan="6" class="text-center text-muted" style="padding: 1.5rem;">No session marks available yet.</td></tr>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="sm" items="${sessionMarks}">
                                                    <tr>
                                                        <td><strong>${sm.subjectName}</strong> (<code>${sm.subjectCode}</code>)</td>
                                                        <td><span class="badge-pill badge-primary">${sm.formattedExamType}</span></td>
                                                        <td>${sm.maxMarks}</td>
                                                        <td><strong>${sm.marksObtained}</strong></td>
                                                        <td>
                                                            <span class="badge-pill ${sm.percentage >= 50.0 ? 'badge-success' : 'badge-danger'}">
                                                                ${sm.percentage}%
                                                            </span>
                                                        </td>
                                                        <td><strong>${sm.grade}</strong></td>
                                                    </tr>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <!-- End Exam Marks -->
                        <div id="sec-end-exam" class="tab-section card table-card mb-4">
                            <div class="table-header-bar table-header-purple">
                                <div class="table-title"><i class="bi bi-award-fill"></i> End Examination Marks</div>
                                <span class="badge-pill badge-purple">Final Exam Average: ${endExamAverage}%</span>
                            </div>
                            <div class="table-responsive">
                                <table class="custom-table" id="endExamTable">
                                    <thead>
                                        <tr>
                                            <th>Subject</th>
                                            <th>Maximum Marks</th>
                                            <th>Marks Obtained</th>
                                            <th>Percentage</th>
                                            <th>Grade</th>
                                            <th>Result / Status</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:choose>
                                            <c:when test="${empty endExamMarks}">
                                                <tr><td colspan="6" class="text-center text-muted" style="padding: 1.5rem;">No end examination marks published yet.</td></tr>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="em" items="${endExamMarks}">
                                                    <tr>
                                                        <td><strong>${em.subjectName}</strong> (<code>${em.subjectCode}</code>)</td>
                                                        <td>${em.maxMarks}</td>
                                                        <td><strong>${em.marksObtained}</strong></td>
                                                        <td>
                                                            <span class="badge-pill ${em.percentage >= 40.0 ? 'badge-success' : 'badge-danger'}">
                                                                ${em.percentage}%
                                                            </span>
                                                        </td>
                                                        <td><strong>${em.grade}</strong></td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${em.percentage >= 40.0}">
                                                                    <span class="badge-pill badge-success">PASSED</span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span class="badge-pill badge-danger">FAILED</span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <!-- Performance Summary -->
                        <div id="sec-performance" class="tab-section card mb-4">
                            <div class="section-heading"><i class="bi bi-bar-chart-line-fill"></i> Overall Academic Performance Summary</div>
                            <div class="performance-grid">
                                <div class="perf-card">
                                    <h4>Attendance Performance</h4>
                                    <p>
                                        Total Classes Conducted: <strong>${attendanceSummary.totalClasses}</strong><br>
                                        Classes Attended: <strong>${attendanceSummary.classesAttended}</strong><br>
                                        Overall Rate: <strong style="color: ${attendanceSummary.belowThreshold ? 'var(--error)' : 'var(--success)'};">${attendanceSummary.attendancePercentage}%</strong>
                                    </p>
                                </div>

                                <div class="perf-card">
                                    <h4>Internal Session Performance</h4>
                                    <p>
                                        Session Marks Average: <strong style="color: var(--accent);">${sessionAverage}%</strong><br>
                                        Status: <span class="badge-pill ${sessionAverage >= 50.0 ? 'badge-success' : 'badge-warning'}">${sessionAverage >= 50.0 ? 'Satisfactory' : 'Needs Improvement'}</span>
                                    </p>
                                </div>

                                <div class="perf-card">
                                    <h4>Final Examination Performance</h4>
                                    <p>
                                        End Exam Average: <strong style="color: var(--accent-hover);">${endExamAverage}%</strong><br>
                                        Final Standing: <span class="badge-pill ${endExamAverage >= 40.0 ? 'badge-success' : 'badge-danger'}">${endExamAverage >= 40.0 ? 'Good Academic Standing' : 'Academic Warning'}</span>
                                    </p>
                                </div>
                            </div>
                        </div>

                    </c:otherwise>
                </c:choose>

            </div>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
