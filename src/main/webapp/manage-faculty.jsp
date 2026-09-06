<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Manage Faculty - SRMS</title>
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
                    <a href="${pageContext.request.contextPath}/admin/faculty" class="sidebar-link active">
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

        <div class="main-wrapper">
            <div class="top-navbar">
                <div class="page-title">Manage Faculty Members</div>
                <div class="user-profile-badge">
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline btn-sm">Logout</a>
                </div>
            </div>

            <div class="content-body">
                <c:if test="${not empty param.msg}">
                    <div class="alert alert-success"><i class="bi bi-check-circle"></i> ${param.msg}</div>
                </c:if>

                <!-- Add Faculty Form -->
                <div class="card mb-4">
                    <div style="font-weight: 600; font-size: 1.1rem; margin-bottom: 1rem;"><i class="bi bi-person-plus me-1"></i> Register New Faculty Member</div>
                    <form action="${pageContext.request.contextPath}/admin/faculty" method="post">
                        <input type="hidden" name="action" value="add">
                        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 1rem;">
                            <div class="form-group">
                                <label>Employee ID</label>
                                <input type="text" name="employeeId" class="form-control" placeholder="e.g. FAC-CSE-003" required>
                            </div>
                            <div class="form-group">
                                <label>Full Name</label>
                                <input type="text" name="name" class="form-control" placeholder="e.g. Dr. Sunitha Paul" required>
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
                                <label>Designation</label>
                                <select name="designation" class="form-select" required>
                                    <option value="Professor & HOD">Professor & HOD</option>
                                    <option value="Professor">Professor</option>
                                    <option value="Associate Professor" selected>Associate Professor</option>
                                    <option value="Assistant Professor">Assistant Professor</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label>Username (Login)</label>
                                <input type="text" name="username" class="form-control" placeholder="e.g. faculty3" required>
                            </div>
                            <div class="form-group">
                                <label>Initial Password</label>
                                <input type="password" name="password" class="form-control" placeholder="Default: faculty123">
                            </div>
                            <div class="form-group">
                                <label>Email Address</label>
                                <input type="email" name="email" class="form-control" placeholder="faculty@college.edu">
                            </div>
                            <div class="form-group">
                                <label>Phone</label>
                                <input type="text" name="phone" class="form-control" placeholder="9876543210">
                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary mt-2"><i class="bi bi-save"></i> Register Faculty</button>
                    </form>
                </div>

                <!-- Faculty Table -->
                <div class="card table-card">
                    <div class="table-header-bar">
                        <div class="table-title"><i class="bi bi-table me-2"></i> Faculty Directory</div>
                        <input type="text" class="form-control table-search-input" data-table="facultyTable" placeholder="Search faculty by name, ID..." style="width: 280px;">
                    </div>
                    <div class="table-responsive">
                        <table class="custom-table" id="facultyTable">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Emp ID</th>
                                    <th>Name</th>
                                    <th>Department</th>
                                    <th>Designation</th>
                                    <th>Username</th>
                                    <th>Email</th>
                                    <th>Phone</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="f" items="${facultyList}">
                                    <tr>
                                        <td>#${f.facultyId}</td>
                                        <td><strong>${f.employeeId}</strong></td>
                                        <td>${f.name}</td>
                                        <td><span class="badge-pill badge-primary">${f.deptName}</span></td>
                                        <td>${f.designation}</td>
                                        <td><code>${f.username}</code></td>
                                        <td>${f.email}</td>
                                        <td>${f.phone}</td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/admin/faculty?action=delete&id=${f.facultyId}" class="btn btn-danger btn-sm" onclick="return confirm('Delete this faculty member?');"><i class="bi bi-trash"></i></a>
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
