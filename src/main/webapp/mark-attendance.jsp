<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Mark Attendance - SRMS Faculty</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="app-container">
        <!-- Sidebar Navigation -->
        <div class="sidebar">
            <div class="sidebar-header">
                <i class="bi bi-building"></i>
                <a href="${pageContext.request.contextPath}/faculty/dashboard" class="sidebar-brand">SRMS FACULTY</a>
            </div>
            <ul class="sidebar-menu">
                <li class="sidebar-item">
                    <a href="${pageContext.request.contextPath}/faculty/dashboard" class="sidebar-link">
                        <i class="bi bi-speedometer2"></i> Dashboard
                    </a>
                </li>
                <li class="sidebar-item">
                    <a href="${pageContext.request.contextPath}/faculty/attendance" class="sidebar-link active">
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
                <div class="page-title">Batch Attendance Entry</div>
                <div class="user-profile-badge">
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline btn-sm">Logout</a>
                </div>
            </div>

            <div class="content-body">
                <c:if test="${not empty param.msg}">
                    <div class="alert alert-success"><i class="bi bi-check-circle"></i> ${param.msg}</div>
                </c:if>

                <!-- Subject Selection Card -->
                <div class="card mb-4">
                    <form action="${pageContext.request.contextPath}/faculty/attendance" method="get">
                        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 1rem; align-items: end;">
                            <div class="form-group mb-0">
                                <label>Select Subject</label>
                                <select name="subjectId" class="form-select" required>
                                    <c:forEach var="s" items="${subjects}">
                                        <option value="${s.subjectId}" ${selectedSubject.subjectId == s.subjectId ? 'selected' : ''}>
                                            ${s.subjectCode} - ${s.subjectName} (S${s.semester})
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group mb-0">
                                <label>Date</label>
                                <input type="date" name="date" class="form-control" value="${empty selectedDate ? '2026-09-04' : selectedDate}" required>
                            </div>
                            <div class="form-group mb-0">
                                <label>Period / Hour</label>
                                <select name="period" class="form-select">
                                    <option value="1" ${selectedPeriod == 1 ? 'selected' : ''}>Period 1</option>
                                    <option value="2" ${selectedPeriod == 2 ? 'selected' : ''}>Period 2</option>
                                    <option value="3" ${selectedPeriod == 3 ? 'selected' : ''}>Period 3</option>
                                    <option value="4" ${selectedPeriod == 4 ? 'selected' : ''}>Period 4</option>
                                </select>
                            </div>
                            <div>
                                <button type="submit" class="btn btn-primary w-100"><i class="bi bi-search"></i> Load Student List</button>
                            </div>
                        </div>
                    </form>
                </div>

                <!-- Student Attendance Marking Grid -->
                <c:if test="${not empty selectedSubject}">
                    <div class="card table-card">
                        <div class="table-header-bar">
                            <div class="table-title">
                                <i class="bi bi-clipboard-check me-2"></i> ${selectedSubject.subjectCode} &bull; Semester ${selectedSubject.semester} &bull; Date: ${selectedDate} (Period ${selectedPeriod})
                            </div>
                        </div>
                        <form action="${pageContext.request.contextPath}/faculty/attendance" method="post">
                            <input type="hidden" name="subjectId" value="${selectedSubject.subjectId}">
                            <input type="hidden" name="attendanceDate" value="${selectedDate}">
                            <input type="hidden" name="periodNo" value="${selectedPeriod}">

                            <div class="table-responsive">
                                <table class="custom-table">
                                    <thead>
                                        <tr>
                                            <th>#</th>
                                            <th>Roll No</th>
                                            <th>Student Name</th>
                                            <th>Attendance Status</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="st" items="${students}" varStatus="loop">
                                            <tr>
                                                <td>${loop.index + 1}</td>
                                                <td><strong>${st.rollNo}</strong></td>
                                                <td>${st.name}</td>
                                                <td>
                                                    <input type="hidden" name="studentIds" value="${st.studentId}">
                                                    <label style="margin-right: 1.5rem; cursor: pointer;">
                                                        <input type="radio" name="status_${st.studentId}" value="PRESENT" checked> <span style="color: var(--success); font-weight: 600;">PRESENT</span>
                                                    </label>
                                                    <label style="cursor: pointer;">
                                                        <input type="radio" name="status_${st.studentId}" value="ABSENT"> <span style="color: var(--error); font-weight: 600;">ABSENT</span>
                                                    </label>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            <div style="padding: 1rem; text-align: right;">
                                <button type="submit" class="btn btn-primary"><i class="bi bi-save"></i> Submit Attendance Batch</button>
                            </div>
                        </form>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
