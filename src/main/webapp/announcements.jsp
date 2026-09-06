<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Announcements & Notices - SRMS</title>
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
                    <a href="${pageContext.request.contextPath}/admin/announcements" class="sidebar-link active">
                        <i class="bi bi-megaphone"></i> Announcements
                    </a>
                </li>
            </ul>
        </div>

        <div class="main-wrapper">
            <div class="top-navbar">
                <div class="page-title">Manage System Announcements</div>
                <div class="user-profile-badge">
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline btn-sm">Logout</a>
                </div>
            </div>

            <div class="content-body">
                <c:if test="${not empty param.msg}">
                    <div class="alert alert-success"><i class="bi bi-check-circle"></i> ${param.msg}</div>
                </c:if>

                <!-- Create Announcement Form -->
                <div class="card mb-4">
                    <div style="font-weight: 600; font-size: 1.1rem; margin-bottom: 1rem;"><i class="bi bi-megaphone me-1"></i> Post New Announcement</div>
                    <form action="${pageContext.request.contextPath}/admin/announcements" method="post">
                        <div class="form-group">
                            <label>Announcement Title</label>
                            <input type="text" name="title" class="form-control" placeholder="e.g. KTU S3 Exam Registration Deadline" required>
                        </div>
                        <div class="form-group">
                            <label>Detailed Message</label>
                            <textarea name="message" class="form-control" rows="4" placeholder="Enter detailed notice message..." required></textarea>
                        </div>
                        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 1rem;">
                            <div class="form-group">
                                <label>Target Role</label>
                                <select name="targetRole" class="form-select">
                                    <option value="ALL" selected>ALL Users (Students & Faculty)</option>
                                    <option value="STUDENT">Students Only</option>
                                    <option value="FACULTY">Faculty Only</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label>Target Department</label>
                                <select name="targetDept" class="form-select">
                                    <option value="0">ALL Departments</option>
                                    <c:forEach var="d" items="${departments}">
                                        <option value="${d.deptId}">${d.deptCode} - ${d.deptName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group">
                                <label>Target Batch Year</label>
                                <input type="text" name="targetBatch" class="form-control" value="ALL">
                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary mt-2"><i class="bi bi-send"></i> Publish Announcement</button>
                    </form>
                </div>

                <!-- Announcements Feed -->
                <div class="card">
                    <div style="font-weight: 600; font-size: 1.1rem; margin-bottom: 1rem;"><i class="bi bi-broadcast me-1"></i> Active Announcements History</div>
                    <c:forEach var="a" items="${announcements}">
                        <div style="border-left: 4px solid var(--secondary); padding: 1rem; margin-bottom: 1rem; background: #F8FAFC; border-radius: 4px;">
                            <div style="display: flex; justify-content: space-between; align-items: center;">
                                <h4 style="color: var(--primary); font-size: 1.1rem;">${a.title}</h4>
                                <div>
                                    <button type="button" class="btn btn-outline btn-sm me-1" onclick="openEditModal(${a.announcementId}, '${fn:escapeXml(a.title)}', '${fn:escapeXml(a.message)}', '${a.targetRole}', ${a.targetDept}, '${a.targetBatch}')"><i class="bi bi-pencil"></i> Edit</button>
                                    <a href="${pageContext.request.contextPath}/admin/announcements?action=delete&id=${a.announcementId}" class="btn btn-danger btn-sm" onclick="return confirm('Delete announcement?');"><i class="bi bi-trash"></i> Delete</a>
                                </div>
                            </div>
                            <p style="margin-top: 0.5rem; font-size: 0.95rem;">${a.message}</p>
                            <div style="font-size: 0.8rem; color: var(--text-muted); margin-top: 0.5rem;">
                                Target: <span class="badge-pill badge-primary">${a.targetRole}</span> | Posted At: ${a.postedAt} | By: ${a.posterName}
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>

    <!-- Edit Announcement Modal -->
    <div id="editAnnModal" style="display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); z-index: 9999; align-items: center; justify-content: center;">
        <div style="background: white; padding: 2rem; border-radius: 8px; width: 90%; max-width: 550px;">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem;">
                <h3 style="margin: 0; font-size: 1.2rem;"><i class="bi bi-pencil-square me-1"></i> Edit Announcement</h3>
                <button type="button" onclick="closeEditModal()" style="border: none; background: none; font-size: 1.2rem; cursor: pointer;">&times;</button>
            </div>
            <form action="${pageContext.request.contextPath}/admin/announcements" method="post">
                <input type="hidden" name="action" value="edit">
                <input type="hidden" id="editAnnId" name="announcementId">
                <div class="form-group">
                    <label>Title</label>
                    <input type="text" id="editTitle" name="title" class="form-control" required>
                </div>
                <div class="form-group">
                    <label>Message</label>
                    <textarea id="editMessage" name="message" class="form-control" rows="4" required></textarea>
                </div>
                <div class="form-group">
                    <label>Target Role</label>
                    <select id="editTargetRole" name="targetRole" class="form-select">
                        <option value="ALL">ALL Users</option>
                        <option value="STUDENT">Students Only</option>
                        <option value="FACULTY">Faculty Only</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Target Department</label>
                    <select id="editTargetDept" name="targetDept" class="form-select">
                        <option value="0">ALL Departments</option>
                        <c:forEach var="d" items="${departments}">
                            <option value="${d.deptId}">${d.deptCode} - ${d.deptName}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label>Target Batch</label>
                    <input type="text" id="editTargetBatch" name="targetBatch" class="form-control">
                </div>
                <div style="display: flex; justify-content: flex-end; gap: 0.5rem;">
                    <button type="button" onclick="closeEditModal()" class="btn btn-outline">Cancel</button>
                    <button type="submit" class="btn btn-primary">Save Changes</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        function openEditModal(id, title, message, role, dept, batch) {
            document.getElementById('editAnnId').value = id;
            document.getElementById('editTitle').value = title;
            document.getElementById('editMessage').value = message;
            document.getElementById('editTargetRole').value = role;
            document.getElementById('editTargetDept').value = dept;
            document.getElementById('editTargetBatch').value = batch;
            document.getElementById('editAnnModal').style.display = 'flex';
        }
        function closeEditModal() {
            document.getElementById('editAnnModal').style.display = 'none';
        }
    </script>
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
