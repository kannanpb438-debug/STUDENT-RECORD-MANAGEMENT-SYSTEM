<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Faculty Portal - SRMS</title>
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
                <a href="${pageContext.request.contextPath}/faculty/dashboard" class="sidebar-brand">SRMS FACULTY</a>
            </div>
            <ul class="sidebar-menu">
                <li class="sidebar-item">
                    <a href="${pageContext.request.contextPath}/faculty/dashboard" class="sidebar-link active">
                        <i class="bi bi-speedometer2"></i> Dashboard
                    </a>
                </li>
                <li class="sidebar-item">
                    <a href="${pageContext.request.contextPath}/faculty/attendance" class="sidebar-link">
                        <i class="bi bi-check2-square"></i> Mark Attendance
                    </a>
                </li>
                <li class="sidebar-item">
                    <a href="${pageContext.request.contextPath}/faculty/marks" class="sidebar-link">
                        <i class="bi bi-pencil-square"></i> Enter Marks
                    </a>
                </li>
                <li class="sidebar-item">
                    <a href="${pageContext.request.contextPath}/faculty/timetable" class="sidebar-link">
                        <i class="bi bi-calendar3"></i> Class Timetable
                    </a>
                </li>
                <li class="sidebar-item">
                    <a href="${pageContext.request.contextPath}/faculty/announcements" class="sidebar-link">
                        <i class="bi bi-megaphone"></i> Announcements
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
                    <div class="page-title">Faculty Portal Dashboard</div>
                </div>
                <div class="user-profile-badge">
                    <div class="user-avatar">F</div>
                    <div class="user-info">
                        <span class="user-name">${faculty.name}</span>
                        <span class="user-role">${faculty.designation} (${faculty.deptName})</span>
                    </div>
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline btn-sm ms-3" id="logoutBtn">Logout</a>
                </div>
            </div>

            <div class="content-body">
                <!-- Welcome Banner -->
                <div class="card welcome-banner mb-4">
                    <h2>Welcome, ${faculty.name}!</h2>
                    <p>Employee ID: ${faculty.employeeId} &bull; ${faculty.designation} &bull; ${faculty.deptName}</p>
                </div>

                <!-- Attendance Alert Banner -->
                <c:if test="${not empty atRiskStudents}">
                    <div class="card attendance-alert-card mb-4">
                        <div style="display: flex; align-items: flex-start; gap: 1rem;">
                            <div style="font-size: 2rem; color: var(--error); line-height: 1;">
                                <i class="bi bi-exclamation-triangle-fill"></i>
                            </div>
                            <div style="flex: 1;">
                                <h3>
                                    ⚠️ Attendance Alert: ${studentsBelow75Count} Student(s) Below 75% Threshold
                                </h3>
                                <p>
                                    <strong>Example:</strong> ${atRiskStudents[0].studentName}'s attendance has fallen below 75% (<strong>${atRiskStudents[0].attendancePercentage}%</strong>). Please monitor student attendance and issue academic warnings.
                                </p>
                            </div>
                        </div>
                    </div>
                </c:if>

                <!-- Dashboard Summary Cards -->
                <div class="stat-grid mb-4">
                    <div class="card stat-card">
                        <div class="stat-icon primary"><i class="bi bi-people-fill"></i></div>
                        <div class="stat-details">
                            <h4>Total Students</h4>
                            <div class="stat-value">${totalStudents}</div>
                            <span class="text-muted">Enrolled in Department</span>
                        </div>
                    </div>

                    <div class="card stat-card">
                        <div class="stat-icon ${studentsBelow75Count > 0 ? 'danger' : 'success'}">
                            <i class="bi bi-exclamation-octagon-fill"></i>
                        </div>
                        <div class="stat-details">
                            <h4>Students Below 75%</h4>
                            <div class="stat-value" style="color: ${studentsBelow75Count > 0 ? 'var(--error)' : 'var(--success)'};">
                                ${studentsBelow75Count}
                            </div>
                            <span class="text-muted">Require Defaulter Warning</span>
                        </div>
                    </div>

                    <div class="card stat-card">
                        <div class="stat-icon success"><i class="bi bi-pie-chart-fill"></i></div>
                        <div class="stat-details">
                            <h4>Average Class Attendance</h4>
                            <div class="stat-value" style="color: var(--success);">${avgClassAttendance}%</div>
                            <span class="text-muted">Department Overall</span>
                        </div>
                    </div>

                    <div class="card stat-card">
                        <div class="stat-icon warning"><i class="bi bi-shield-exclamation"></i></div>
                        <div class="stat-details">
                            <h4>Requiring Attention</h4>
                            <div class="stat-value" style="color: var(--warning);">${studentsBelow75Count}</div>
                            <span class="text-muted">At-Risk Students</span>
                        </div>
                    </div>
                </div>

                <!-- At-Risk Students -->
                <div class="card table-card at-risk-card mb-4">
                    <div class="table-header-bar at-risk-header">
                        <div class="table-title">
                            <i class="bi bi-exclamation-circle-fill" style="color: var(--error);"></i> At-Risk Students (Attendance &lt; 75%)
                        </div>
                        <span class="badge-pill badge-danger">${studentsBelow75Count} Student(s)</span>
                    </div>
                    <div class="table-responsive">
                        <table class="custom-table" id="atRiskTable">
                            <thead>
                                <tr>
                                    <th>Student Name</th>
                                    <th>Roll / Student ID</th>
                                    <th>Current Attendance %</th>
                                    <th>Total Classes</th>
                                    <th>Classes Attended</th>
                                    <th>Classes Missed</th>
                                    <th>Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${empty atRiskStudents}">
                                        <tr>
                                            <td colspan="7" class="text-center text-muted" style="padding: 1.5rem;">
                                                <i class="bi bi-check-circle" style="color: var(--success);"></i> Great news! All students meet the 75% attendance requirement.
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="st" items="${atRiskStudents}">
                                            <tr class="at-risk-row">
                                                <td><strong>${st.studentName}</strong></td>
                                                <td><code>${st.rollNo}</code></td>
                                                <td>
                                                    <span style="font-weight: 700; color: var(--error); font-size: 1.05rem;">
                                                        ${st.attendancePercentage}%
                                                    </span>
                                                </td>
                                                <td>${st.totalClasses}</td>
                                                <td><span style="color: var(--success); font-weight: 600;">${st.classesAttended}</span></td>
                                                <td><span style="color: var(--error); font-weight: 600;">${st.classesMissed}</span></td>
                                                <td>
                                                    <span class="badge-pill badge-danger">⚠️ At Risk</span>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>

                <!-- Assigned Subjects -->
                <div class="card table-card mb-4">
                    <div class="table-header-bar">
                        <div class="table-title"><i class="bi bi-journal-check"></i> Your Assigned Subjects</div>
                    </div>
                    <div class="table-responsive">
                        <table class="custom-table" id="subjectsTable">
                            <thead>
                                <tr>
                                    <th>Subject Code</th>
                                    <th>Subject Name</th>
                                    <th>Department</th>
                                    <th>Semester</th>
                                    <th>Credits</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="s" items="${assignedSubjects}">
                                    <tr>
                                        <td><strong>${s.subjectCode}</strong></td>
                                        <td>${s.subjectName}</td>
                                        <td><span class="badge-pill badge-primary">${s.deptName}</span></td>
                                        <td>Semester ${s.semester}</td>
                                        <td>${s.credits} Credits</td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/faculty/attendance?subjectId=${s.subjectId}" class="btn btn-primary btn-sm"><i class="bi bi-check2-square"></i> Attendance</a>
                                            <a href="${pageContext.request.contextPath}/faculty/marks?subjectId=${s.subjectId}&examType=SERIES1" class="btn btn-outline btn-sm"><i class="bi bi-pencil"></i> Marks</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>

                <!-- Notices -->
                <div class="card">
                    <div class="section-heading"><i class="bi bi-megaphone"></i> Faculty Notices & Announcements</div>
                    <c:forEach var="a" items="${announcements}">
                        <div class="announcement-item">
                            <strong class="ann-title">${a.title}</strong>
                            <p class="ann-body">${a.message}</p>
                            <span class="ann-meta">${a.postedAt}</span>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
