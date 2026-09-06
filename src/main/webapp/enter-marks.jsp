<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Enter Marks - SRMS Faculty</title>
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
                    <a href="${pageContext.request.contextPath}/faculty/attendance" class="sidebar-link">
                        <i class="bi bi-check2-square"></i> Mark Attendance
                    </a>
                </li>
                <li class="sidebar-item">
                    <a href="${pageContext.request.contextPath}/faculty/marks" class="sidebar-link active">
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
                <div class="page-title">Internal Assessment Marks Entry</div>
                <div class="user-profile-badge">
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline btn-sm">Logout</a>
                </div>
            </div>

            <div class="content-body">
                <c:if test="${not empty param.msg}">
                    <div class="alert alert-success"><i class="bi bi-check-circle"></i> ${param.msg}</div>
                </c:if>

                <!-- Subject & Exam Selection Card -->
                <div class="card mb-4">
                    <form action="${pageContext.request.contextPath}/faculty/marks" method="get">
                        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 1rem; align-items: end;">
                            <div class="form-group mb-0">
                                <label>Select Subject</label>
                                <select name="subjectId" class="form-select" required>
                                    <c:forEach var="s" items="${subjects}">
                                        <option value="${s.subjectId}" ${selectedSubject.subjectId == s.subjectId ? 'selected' : ''}>
                                            ${s.subjectCode} - ${s.subjectName}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group mb-0">
                                <label>Examination Type</label>
                                <select name="examType" class="form-select" required>
                                    <option value="SERIES1" ${selectedExamType == 'SERIES1' ? 'selected' : ''}>Series Test 1</option>
                                    <option value="SERIES2" ${selectedExamType == 'SERIES2' ? 'selected' : ''}>Series Test 2</option>
                                    <option value="ASSIGNMENT" ${selectedExamType == 'ASSIGNMENT' ? 'selected' : ''}>Assignment</option>
                                    <option value="SEMESTER" ${selectedExamType == 'SEMESTER' ? 'selected' : ''}>Semester Exam</option>
                                </select>
                            </div>
                            <div>
                                <button type="submit" class="btn btn-primary w-100"><i class="bi bi-pencil"></i> Load Marks Table</button>
                            </div>
                        </div>
                    </form>
                </div>

                <!-- Marks Table Card -->
                <c:if test="${not empty selectedSubject}">
                    <div class="card table-card">
                        <div class="table-header-bar">
                            <div class="table-title">
                                <i class="bi bi-file-earmark-spreadsheet me-2"></i> ${selectedSubject.subjectCode} &bull; Exam: ${selectedExamType}
                            </div>
                        </div>
                        <form action="${pageContext.request.contextPath}/faculty/marks" method="post">
                            <input type="hidden" name="subjectId" value="${selectedSubject.subjectId}">
                            <input type="hidden" name="examType" value="${selectedExamType}">

                            <div style="padding: 1rem; background: #F8FAFC; border-bottom: 1px solid var(--border);" class="d-flex align-items-center gap-3">
                                <label style="font-weight: 600;">Maximum Marks:</label>
                                <input type="number" step="0.5" name="maxMarks" value="50.0" class="form-control" style="width: 120px;" required>
                            </div>

                            <div class="table-responsive">
                                <table class="custom-table">
                                    <thead>
                                        <tr>
                                            <th>#</th>
                                            <th>Roll No</th>
                                            <th>Student Name</th>
                                            <th>Marks Obtained</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="st" items="${students}" varStatus="loop">
                                            <c:set var="existingVal" value="" />
                                            <c:forEach var="m" items="${existingMarks}">
                                                <c:if test="${m.studentId == st.studentId}">
                                                    <c:set var="existingVal" value="${m.marksObtained}" />
                                                </c:if>
                                            </c:forEach>
                                            <tr>
                                                <td>${loop.index + 1}</td>
                                                <td><strong>${st.rollNo}</strong></td>
                                                <td>${st.name}</td>
                                                <td>
                                                    <input type="hidden" name="studentIds" value="${st.studentId}">
                                                    <input type="number" step="0.01" min="0" max="100" name="mark_${st.studentId}" class="form-control" value="${existingVal}" placeholder="Enter marks (0-100)" style="max-width: 180px;" required>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            <div style="padding: 1rem; text-align: right;">
                                <button type="submit" class="btn btn-primary"><i class="bi bi-save"></i> Save / Update Marks</button>
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
