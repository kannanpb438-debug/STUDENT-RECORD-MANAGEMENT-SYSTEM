<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - SRMS</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="app-container">
        <!-- Sidebar Overlay -->
        <div class="sidebar-overlay" id="sidebarOverlay"></div>

        <!-- Sidebar Navigation -->
        <div class="sidebar">
            <div class="sidebar-header">
                <i class="bi bi-building" style="font-size: 1.35rem;"></i>
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="sidebar-brand">SRMS ERP</a>
            </div>
            <ul class="sidebar-menu">
                <li class="sidebar-item">
                    <a href="${pageContext.request.contextPath}/admin/dashboard" class="sidebar-link active">
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
            <div class="sidebar-footer">
                Campus7 ERP v1.0 &bull; KTU B.Tech
            </div>
        </div>

        <!-- Main Wrapper -->
        <div class="main-wrapper">
            <div class="top-navbar">
                <div class="navbar-left">
                    <button class="sidebar-toggle" id="sidebarToggle" aria-label="Toggle sidebar">
                        <i class="bi bi-list"></i>
                    </button>
                    <div class="page-title">Administrator Dashboard</div>
                </div>
                <div class="user-profile-badge">
                    <div class="user-avatar">A</div>
                    <div class="user-info">
                        <span class="user-name">${sessionScope.loggedUser.username}</span>
                        <span class="user-role">System Administrator</span>
                    </div>
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline btn-sm ms-3" id="logoutBtn">
                        <i class="bi bi-box-arrow-right"></i> Logout
                    </a>
                </div>
            </div>

            <div class="content-body">
                <!-- Stat Cards -->
                <div class="stat-grid">
                    <div class="card stat-card">
                        <div class="stat-icon primary"><i class="bi bi-people"></i></div>
                        <div class="stat-details">
                            <h4>Total Students</h4>
                            <div class="stat-value">${totalStudents}</div>
                        </div>
                    </div>
                    <div class="card stat-card">
                        <div class="stat-icon primary"><i class="bi bi-person-badge"></i></div>
                        <div class="stat-details">
                            <h4>Faculty Members</h4>
                            <div class="stat-value">${totalFaculty}</div>
                        </div>
                    </div>
                    <div class="card stat-card">
                        <div class="stat-icon danger"><i class="bi bi-exclamation-triangle"></i></div>
                        <div class="stat-details">
                            <h4>Attendance Defaulters</h4>
                            <div class="stat-value">${attendanceDefaulters}</div>
                        </div>
                    </div>
                    <div class="card stat-card">
                        <div class="stat-icon warning"><i class="bi bi-cash"></i></div>
                        <div class="stat-details">
                            <h4>Pending Fee Dues</h4>
                            <div class="stat-value">${pendingFeesCount}</div>
                        </div>
                    </div>
                </div>

                <!-- Announcements Section -->
                <div class="card table-card">
                    <div class="table-header-bar">
                        <div class="table-title"><i class="bi bi-megaphone"></i> System Announcements & Notices</div>
                        <a href="${pageContext.request.contextPath}/admin/announcements" class="btn btn-primary btn-sm" id="postAnnouncementBtn">
                            <i class="bi bi-plus-lg"></i> Post Announcement
                        </a>
                    </div>
                    <div style="padding: 1.25rem;">
                        <c:choose>
                            <c:when test="${empty announcements}">
                                <p class="text-muted">No announcements posted yet.</p>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="anc" items="${announcements}">
                                    <div class="announcement-item">
                                        <div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 0.5rem;">
                                            <strong class="ann-title">${anc.title}</strong>
                                            <span class="ann-meta">${anc.postedAt}</span>
                                        </div>
                                        <p class="ann-body">${anc.message}</p>
                                        <div class="ann-meta">
                                            Target: <span class="badge-pill badge-primary">${anc.targetRole}</span> | Posted By: ${anc.posterName}
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
