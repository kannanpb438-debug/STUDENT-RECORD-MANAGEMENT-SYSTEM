<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Internal Marks & Results - SRMS Student</title>
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
                    <a href="${pageContext.request.contextPath}/student/marks" class="sidebar-link active">
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
                <div class="page-title">Academic Examination Marksheet</div>
                <div class="user-profile-badge">
                    <a href="${pageContext.request.contextPath}/export/pdf?type=marksheet" class="btn btn-primary btn-sm"><i class="bi bi-file-earmark-pdf"></i> Export Official Marksheet PDF</a>
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline btn-sm ms-2">Logout</a>
                </div>
            </div>

            <div class="content-body">
                <div class="card table-card">
                    <div class="table-header-bar">
                        <div class="table-title"><i class="bi bi-award me-2"></i> Semester ${student.semester} Evaluation Records</div>
                    </div>
                    <div class="table-responsive">
                        <table class="custom-table">
                            <thead>
                                <tr>
                                    <th>Subject Code</th>
                                    <th>Subject Name</th>
                                    <th>Exam Type</th>
                                    <th>Marks Obtained</th>
                                    <th>Max Marks</th>
                                    <th>Percentage</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${empty marksList}">
                                        <tr><td colspan="6" class="text-center text-muted">No internal test marks uploaded yet.</td></tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="m" items="${marksList}">
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
            </div>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
