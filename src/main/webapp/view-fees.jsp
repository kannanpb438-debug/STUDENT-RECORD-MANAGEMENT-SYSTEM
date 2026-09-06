<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Fee Status - SRMS Student</title>
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
                    <a href="${pageContext.request.contextPath}/student/marks" class="sidebar-link">
                        <i class="bi bi-journal-text"></i> Internal Marks
                    </a>
                </li>
                <li class="sidebar-item">
                    <a href="${pageContext.request.contextPath}/student/fees" class="sidebar-link active">
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
                <div class="page-title">Fee Payment Ledger & Downloadable Receipts</div>
                <div class="user-profile-badge">
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline btn-sm">Logout</a>
                </div>
            </div>

            <div class="content-body">
                <div class="card table-card">
                    <div class="table-header-bar">
                        <div class="table-title"><i class="bi bi-receipt me-2"></i> Semester Fee Statements</div>
                    </div>
                    <div class="table-responsive">
                        <table class="custom-table">
                            <thead>
                                <tr>
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
                                        <td><strong>Semester ${f.semester}</strong></td>
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
                                            <c:if test="${f.status == 'PAID'}">
                                                <a href="${pageContext.request.contextPath}/export/pdf?type=receipt&feeId=${f.feeId}" class="btn btn-outline btn-sm"><i class="bi bi-download"></i> Receipt PDF</a>
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
