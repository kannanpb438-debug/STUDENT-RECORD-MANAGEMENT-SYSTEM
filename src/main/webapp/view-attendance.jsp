<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Attendance Report - SRMS Student</title>
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
                    <a href="${pageContext.request.contextPath}/student/attendance" class="sidebar-link active">
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
                <div class="page-title">Subject-Wise & Daily Attendance Report</div>
                <div class="user-profile-badge">
                    <a href="${pageContext.request.contextPath}/export/pdf?type=attendance" class="btn btn-primary btn-sm"><i class="bi bi-download"></i> Download PDF Report</a>
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline btn-sm ms-2">Logout</a>
                </div>
            </div>

            <div class="content-body">
                <!-- Prominent Attendance Warning Alert Banner if < 75% -->
                <c:if test="${attendanceSummary.belowThreshold}">
                    <div class="card mb-4 attendance-alert-card" style="background: #FEF2F2; border-left: 6px solid #EF4444; border-radius: 8px; padding: 1.25rem;">
                        <div style="display: flex; align-items: flex-start; gap: 1rem;">
                            <div style="font-size: 2rem; color: #DC2626; line-height: 1;">
                                <i class="bi bi-exclamation-triangle-fill"></i>
                            </div>
                            <div style="flex: 1;">
                                <h3 style="color: #991B1B; margin: 0 0 0.35rem 0; font-size: 1.15rem; font-weight: 700;">
                                    ⚠️ Attendance Warning: Low Attendance Alert
                                </h3>
                                <p style="color: #B91C1C; margin: 0; font-size: 0.95rem; line-height: 1.5;">
                                    Your current attendance is <strong>${attendanceSummary.attendancePercentage}%</strong>. Your attendance is below the required <strong>75%</strong>. Please improve your attendance to avoid academic issues.
                                </p>
                                <div style="margin-top: 0.65rem; padding: 0.5rem 0.75rem; background: #FEE2E2; border-radius: 6px; display: inline-block; color: #7F1D1D; font-size: 0.9rem; font-weight: 600;">
                                    <i class="bi bi-calculator me-1"></i> Action Required: You need to attend at least <strong>${attendanceSummary.classesNeededFor75}</strong> consecutive class(es) to reach 75%.
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>

                <!-- Subject Percentages Grid -->
                <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 1rem; margin-bottom: 1.5rem;">
                    <c:forEach var="sub" items="${subjects}">
                        <c:set var="pct" value="${subjectPercentages[sub.subjectId]}" />
                        <div class="card" style="border-top: 4px solid ${pct >= 75.0 ? 'var(--success)' : 'var(--error)'};">
                            <div style="font-size: 0.8rem; color: var(--text-muted); font-weight: 600;">${sub.subjectCode}</div>
                            <div style="font-weight: 600; font-size: 0.95rem; margin-top: 0.2rem;">${sub.subjectName}</div>
                            <div style="font-size: 1.4rem; font-weight: 700; margin-top: 0.5rem; color: ${pct >= 75.0 ? 'var(--success)' : 'var(--error)'};">
                                ${String.format("%.1f", pct)}%
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <!-- Attendance Log Table -->
                <div class="card table-card">
                    <div class="table-header-bar">
                        <div class="table-title"><i class="bi bi-calendar-check me-2"></i> Attendance Log History</div>
                    </div>
                    <div class="table-responsive">
                        <table class="custom-table">
                            <thead>
                                <tr>
                                    <th>Date</th>
                                    <th>Subject Code</th>
                                    <th>Subject Name</th>
                                    <th>Period No</th>
                                    <th>Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="a" items="${attendanceList}">
                                    <tr>
                                        <td><strong>${a.attendanceDate}</strong></td>
                                        <td><code>${a.subjectCode}</code></td>
                                        <td>${a.subjectName}</td>
                                        <td>Period ${a.periodNo}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${a.status == 'PRESENT'}"><span class="badge-pill badge-success">PRESENT</span></c:when>
                                                <c:otherwise><span class="badge-pill badge-danger">ABSENT</span></c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
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
