<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Class Timetable - SRMS Student</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="app-container">
        <!-- Sidebar Navigation -->
        <div class="sidebar">
            <div class="sidebar-header">
                <i class="bi bi-building"></i>
                <a href="${pageContext.request.contextPath}/student/dashboard" class="sidebar-brand">SRMS STUDENT</a>
            </div>
            <ul class="sidebar-menu">
                <li class="sidebar-item">
                    <a href="${pageContext.request.contextPath}/student/dashboard" class="sidebar-link">
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
                    <a href="${pageContext.request.contextPath}/student/timetable" class="sidebar-link active">
                        <i class="bi bi-calendar-event"></i> Class Timetable
                    </a>
                </li>
            </ul>
        </div>

        <div class="main-wrapper">
            <div class="top-navbar">
                <div class="page-title">Weekly Class Schedule</div>
                <div class="user-profile-badge">
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline btn-sm">Logout</a>
                </div>
            </div>

            <div class="content-body">
                <div class="card table-card">
                    <div class="table-header-bar">
                        <div class="table-title"><i class="bi bi-clock me-2"></i> Semester ${student.semester} Class Timetable & Hall Allocation</div>
                    </div>
                    <div class="table-responsive">
                        <table class="custom-table">
                            <thead>
                                <tr>
                                    <th>Day</th>
                                    <th>Period</th>
                                    <th>Subject Code</th>
                                    <th>Subject Name</th>
                                    <th>Faculty</th>
                                    <th>Room No</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${empty timetable}">
                                        <tr><td colspan="6" class="text-center text-muted">No timetable entries published yet.</td></tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="t" items="${timetable}">
                                            <tr>
                                                <td><strong>${t.dayOfWeek}</strong></td>
                                                <td>Period ${t.periodNo}</td>
                                                <td><code>${t.subjectCode}</code></td>
                                                <td>${t.subjectName}</td>
                                                <td>${t.facultyName}</td>
                                                <td><span class="badge-pill badge-warning">${t.roomNo}</span></td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
