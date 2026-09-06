<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Manage Students - SRMS</title>
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
                    <a href="${pageContext.request.contextPath}/admin/students" class="sidebar-link active">
                        <i class="bi bi-people"></i> Manage Students
                    </a>
                </li>
                <li class="sidebar-item">
                    <a href="${pageContext.request.contextPath}/admin/faculty" class="sidebar-link">
                        <i class="bi bi-person-badge"></i> Manage Faculty
                    </a>
                </li>
                <li class="sidebar-item">
                    <a href="${pageContext.request.contextPath}/admin/courses" class="sidebar-link">
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

        <!-- Main Wrapper -->
        <div class="main-wrapper">
            <div class="top-navbar">
                <div class="page-title">Manage Student Records</div>
                <div class="user-profile-badge">
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline btn-sm">
                        <i class="bi bi-box-arrow-right"></i> Logout
                    </a>
                </div>
            </div>

            <div class="content-body">
                <c:if test="${not empty param.msg}">
                    <div class="alert alert-success"><i class="bi bi-check-circle"></i> ${param.msg}</div>
                </c:if>
                <c:if test="${not empty param.error}">
                    <div class="alert alert-danger"><i class="bi bi-exclamation-circle"></i> ${param.error}</div>
                </c:if>

                <!-- Add Student Form Card -->
                <div class="card mb-4">
                    <div style="font-weight: 600; font-size: 1.1rem; margin-bottom: 1rem;"><i class="bi bi-person-plus me-1"></i> Register New Student</div>
                    <form action="${pageContext.request.contextPath}/admin/students" method="post">
                        <input type="hidden" name="action" value="add">
                        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 1rem;">
                            <div class="form-group">
                                <label>Roll Number (KTU format)</label>
                                <input type="text" name="rollNo" class="form-control" placeholder="e.g. TVE23CS003" required>
                            </div>
                            <div class="form-group">
                                <label>Student Full Name</label>
                                <input type="text" name="name" class="form-control" placeholder="e.g. Rahul Sharma" required>
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
                                    <option value="7">Semester 7</option>
                                    <option value="8">Semester 8</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label>Batch Year</label>
                                <input type="number" name="batchYear" class="form-control" value="2023" required>
                            </div>
                            <div class="form-group">
                                <label>Username (Login)</label>
                                <input type="text" name="username" class="form-control" placeholder="e.g. student3" required>
                            </div>
                            <div class="form-group">
                                <label>Initial Password</label>
                                <input type="password" name="password" class="form-control" placeholder="Default: student123">
                            </div>
                            <div class="form-group">
                                <label>Email Address</label>
                                <input type="email" name="email" class="form-control" placeholder="student@college.edu">
                            </div>
                            <div class="form-group">
                                <label>Phone</label>
                                <input type="text" name="phone" class="form-control" placeholder="9876543210">
                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary mt-2"><i class="bi bi-save"></i> Save Student Record</button>
                    </form>
                </div>

                <!-- Students Table Card -->
                <div class="card table-card">
                    <div class="table-header-bar">
                        <div class="table-title"><i class="bi bi-table me-2"></i> Registered Student Records</div>
                        <input type="text" class="form-control table-search-input" data-table="studentsTable" placeholder="Search student by name, roll no, department..." style="width: 280px;">
                    </div>
                    <div class="table-responsive">
                        <table class="custom-table" id="studentsTable">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Roll No</th>
                                    <th>Name</th>
                                    <th>Department</th>
                                    <th>Semester</th>
                                    <th>Batch</th>
                                    <th>Username</th>
                                    <th>Email</th>
                                    <th>Phone</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="s" items="${students}">
                                    <tr>
                                        <td>#${s.studentId}</td>
                                        <td><strong>${s.rollNo}</strong></td>
                                        <td>${s.name}</td>
                                        <td><span class="badge-pill badge-primary">${s.deptName}</span></td>
                                        <td>S${s.semester}</td>
                                        <td>${s.batchYear}</td>
                                        <td><code>${s.username}</code></td>
                                        <td>${s.email}</td>
                                        <td>${s.phone}</td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/export/pdf?type=marksheet&studentId=${s.studentId}" class="btn btn-outline btn-sm" title="Download Marksheet PDF"><i class="bi bi-file-earmark-pdf"></i> PDF</a>
                                            <a href="${pageContext.request.contextPath}/admin/students?action=delete&id=${s.studentId}" class="btn btn-danger btn-sm" onclick="return confirm('Are you sure you want to delete this student?');"><i class="bi bi-trash"></i></a>
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
