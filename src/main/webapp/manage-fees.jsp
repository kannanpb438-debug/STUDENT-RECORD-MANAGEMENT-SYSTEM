<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Manage Fee Records - SRMS</title>
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
                    <a href="${pageContext.request.contextPath}/admin/fees" class="sidebar-link active">
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
                <div class="page-title">Manage Student Fee Dues & Receipts</div>
                <div class="user-profile-badge">
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline btn-sm">Logout</a>
                </div>
            </div>

            <div class="content-body">
                <c:if test="${not empty param.msg}">
                    <div class="alert alert-success"><i class="bi bi-check-circle"></i> ${param.msg}</div>
                </c:if>

                <!-- Add Fee Record Form -->
                <div class="card mb-4">
                    <div style="font-weight: 600; font-size: 1.1rem; margin-bottom: 1rem;"><i class="bi bi-cash me-1"></i> Issue New Fee Invoice</div>
                    <form action="${pageContext.request.contextPath}/admin/fees" method="post">
                        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 1rem;">
                            <div class="form-group">
                                <label>Student</label>
                                <select name="studentId" class="form-select" required>
                                    <c:forEach var="s" items="${students}">
                                        <option value="${s.studentId}">${s.rollNo} - ${s.name} (${s.deptName})</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group">
                                <label>Semester</label>
                                <input type="number" name="semester" class="form-control" value="3" required>
                            </div>
                            <div class="form-group">
                                <label>Amount (INR)</label>
                                <input type="number" step="0.01" name="amount" class="form-control" placeholder="45000.00" required>
                            </div>
                            <div class="form-group">
                                <label>Due Date</label>
                                <input type="date" name="dueDate" class="form-control" required>
                            </div>
                            <div class="form-group">
                                <label>Status</label>
                                <select name="status" class="form-select" required>
                                    <option value="PENDING" selected>PENDING</option>
                                    <option value="PAID">PAID</option>
                                </select>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary mt-2"><i class="bi bi-file-earmark-plus"></i> Issue Fee Record</button>
                    </form>
                </div>

                <!-- Fee Table -->
                <div class="card table-card">
                    <div class="table-header-bar">
                        <div class="table-title"><i class="bi bi-receipt me-2"></i> Semester Fee Status Ledger</div>
                        <input type="text" class="form-control table-search-input" data-table="feesTable" placeholder="Search student name, roll no..." style="width: 280px;">
                    </div>
                    <div class="table-responsive">
                        <table class="custom-table" id="feesTable">
                            <thead>
                                <tr>
                                    <th>Fee ID</th>
                                    <th>Student</th>
                                    <th>Roll No</th>
                                    <th>Semester</th>
                                    <th>Amount (INR)</th>
                                    <th>Status</th>
                                    <th>Due Date</th>
                                    <th>Payment Date</th>
                                    <th>Receipt No</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="f" items="${feeRecords}">
                                    <tr>
                                        <td>#${f.feeId}</td>
                                        <td>${f.studentName}</td>
                                        <td><strong>${f.rollNo}</strong></td>
                                        <td>S${f.semester}</td>
                                        <td>INR ${f.amount}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${f.status == 'PAID'}"><span class="badge-pill badge-success">PAID</span></c:when>
                                                <c:otherwise><span class="badge-pill badge-warning">PENDING</span></c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>${f.dueDate}</td>
                                        <td>${empty f.paymentDate ? '-' : f.paymentDate}</td>
                                        <td><code>${empty f.receiptNo ? '-' : f.receiptNo}</code></td>
                                        <td>
                                            <c:if test="${f.status == 'PENDING'}">
                                                <a href="${pageContext.request.contextPath}/admin/fees?action=markPaid&id=${f.feeId}" class="btn btn-primary btn-sm"><i class="bi bi-check-lg"></i> Mark Paid</a>
                                            </c:if>
                                            <c:if test="${f.status == 'PAID'}">
                                                <a href="${pageContext.request.contextPath}/export/pdf?type=receipt&feeId=${f.feeId}" class="btn btn-outline btn-sm"><i class="bi bi-file-earmark-pdf"></i> Receipt PDF</a>
                                            </c:if>
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
