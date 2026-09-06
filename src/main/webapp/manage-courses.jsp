<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Manage Courses & Timetable - SRMS</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="app-container">
        <!-- Sidebar Navigation -->
        <div class="sidebar">
            <div class="sidebar-header">
                <i class="bi bi-building"></i>
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="sidebar-brand">SRMS ERP</a>
            </div>
            <ul class="sidebar-menu">
                <li class="sidebar-item">
                    <a href="${pageContext.request.contextPath}/admin/dashboard" class="sidebar-link">
                        <i class="bi bi-speedometer2"></i> Dashboard
                    </a>
                </li>
                <li class="sidebar-item">
                    <a href="${pageContext.request.contextPath}/admin/students" class="sidebar-link">
                        <i class="bi bi-people"></i> Manage Students
                    </a>
                </li>
                <li class="sidebar-item">
                    <a href="${pageContext.request.contextPath}/admin/faculty" class="sidebar-link">
                        <i class="bi bi-person-badge"></i> Manage Faculty
                    </a>
                </li>
                <li class="sidebar-item">
                    <a href="${pageContext.request.contextPath}/admin/courses" class="sidebar-link active">
                        <i class="bi bi-journal-bookmark"></i> Courses & Timetable
                    </a>
                </li>
                <li class="sidebar-item">
                    <a href="${pageContext.request.contextPath}/admin/fees" class="sidebar-link">
                        <i class="bi bi-cash-stack"></i> Fee Records
                    </a>
                </li>
                <li class="sidebar-item">
                    <a href="${pageContext.request.contextPath}/admin/announcements" class="sidebar-link">
                        <i class="bi bi-megaphone"></i> Announcements
                    </a>
                </li>
            </ul>
        </div>

        <div class="main-wrapper">
            <div class="top-navbar">
                <div class="page-title">Manage Subjects & Timetable Allocation</div>
                <div class="user-profile-badge">
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline btn-sm">Logout</a>
                </div>
            </div>

            <div class="content-body">
                <c:if test="${not empty param.msg}">
                    <div class="alert alert-success"><i class="bi bi-check-circle"></i> ${param.msg}</div>
                </c:if>

                <!-- Add Subject Form -->
                <div class="card mb-4">
                    <div style="font-weight: 600; font-size: 1.1rem; margin-bottom: 1rem;"><i class="bi bi-journal-plus me-1"></i> Create New Subject / Course</div>
                    <form action="${pageContext.request.contextPath}/admin/courses" method="post">
                        <input type="hidden" name="action" value="addSubject">
                        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 1rem;">
                            <div class="form-group">
                                <label>Subject Code</label>
                                <input type="text" name="subjectCode" class="form-control" placeholder="e.g. CST205" required>
                            </div>
                            <div class="form-group">
                                <label>Subject Name</label>
                                <input type="text" name="subjectName" class="form-control" placeholder="e.g. Object Oriented Programming" required>
                            </div>
                            <div class="form-group">
                                <label>Department</label>
                                <select name="deptId" class="form-select" required>
                                    <c:forEach var="dept" items="${departments}">
                                        <option value="${dept.deptId}">${dept.deptCode} - ${dept.deptName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group">
                                <label>Semester</label>
                                <select name="semester" class="form-select" required>
                                    <option value="1">Semester 1</option>
                                    <option value="2">Semester 2</option>
                                    <option value="3" selected>Semester 3 (S3)</option>
                                    <option value="4">Semester 4</option>
                                    <option value="5">Semester 5</option>
                                    <option value="6">Semester 6</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label>Assigned Faculty</label>
                                <select name="facultyId" class="form-select">
                                    <option value="">-- Unassigned --</option>
                                    <c:forEach var="f" items="${facultyList}">
                                        <option value="${f.facultyId}">${f.name} (${f.deptName})</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group">
                                <label>Credits</label>
                                <input type="number" name="credits" class="form-control" value="4" required>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary mt-2"><i class="bi bi-save"></i> Save Subject</button>
                    </form>
                </div>

                <!-- Subjects List Table -->
                <div class="card table-card">
                    <div class="table-header-bar">
                        <div class="table-title"><i class="bi bi-list-ul me-2"></i> Course Catalog</div>
                        <input type="text" class="form-control table-search-input" data-table="subjectsTable" placeholder="Search subject code, name..." style="width: 280px;">
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
                                    <th>Assigned Faculty</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="s" items="${subjects}">
                                    <tr>
                                        <td><strong>${s.subjectCode}</strong></td>
                                        <td>${s.subjectName}</td>
                                        <td><span class="badge-pill badge-primary">${s.deptName}</span></td>
                                        <td>S${s.semester}</td>
                                        <td>${s.credits} Credits</td>
                                        <td>${empty s.facultyName ? '<span class="text-muted">Not Assigned</span>' : s.facultyName}</td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/admin/courses?action=delete&id=${s.subjectId}" class="btn btn-danger btn-sm" onclick="return confirm('Delete this subject?');"><i class="bi bi-trash"></i></a>
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
