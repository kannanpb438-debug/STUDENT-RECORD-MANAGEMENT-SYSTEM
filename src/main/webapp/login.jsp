<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="SRMS College ERP Login Portal — Student Record Management System for administrators, faculty, students, and parents.">
    <title>SRMS - Login Portal</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="login-body">

    <div class="login-card">
        <div class="login-header">
            <h2>SRMS COLLEGE ERP</h2>
            <p>Student Record Management System</p>
        </div>

        <div class="login-form-container">
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger" id="loginError">
                    <i class="bi bi-exclamation-triangle-fill"></i>
                    <span>${errorMessage}</span>
                </div>
            </c:if>

            <c:if test="${param.registered == 'true'}">
                <div class="alert alert-success" id="registerSuccess">
                    <i class="bi bi-check-circle-fill"></i>
                    <span>Account registered successfully! You can now log in.</span>
                </div>
            </c:if>

            <c:if test="${param.loggedOut == 'true'}">
                <div class="alert alert-success" id="logoutSuccess">
                    <i class="bi bi-check-circle-fill"></i>
                    <span>You have been logged out successfully.</span>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/login" method="post" id="loginForm">

                <div class="form-group">
                    <label for="username">Username / Roll No / Emp ID</label>
                    <input type="text" id="username" name="username" class="form-control" placeholder="Enter username..." required autocomplete="off">
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" class="form-control" placeholder="Enter password..." required>
                </div>

                <button type="submit" class="btn btn-primary" id="loginButton" style="width: 100%;">
                    <i class="bi bi-box-arrow-in-right"></i> Log In to Portal
                </button>
            </form>

            <div style="text-align: center; margin-top: 1rem; font-size: 0.85rem;">
                New user? <a href="${pageContext.request.contextPath}/register" id="registerLink" style="color: var(--accent); font-weight: 600;">Create an Account / Sign Up</a>
            </div>

            <div class="demo-credentials-box">
                <div class="mb-1"><strong>Quick Demo Credentials (1-Click Fill):</strong></div>
                <div style="display: flex; flex-wrap: wrap; gap: 0.35rem; margin-top: 0.35rem; align-items: center;">
                    <span>Admin:</span> <button type="button" class="quick-fill-btn" id="fillAdmin" onclick="fillDemoCredentials('admin', 'admin123', 'ADMIN')">Fill Admin</button>
                    <span style="margin-left: 0.5rem;">Faculty:</span> <button type="button" class="quick-fill-btn" id="fillFaculty" onclick="fillDemoCredentials('faculty1', 'faculty123', 'FACULTY')">Fill Faculty</button>
                    <span style="margin-left: 0.5rem;">Student:</span> <button type="button" class="quick-fill-btn" id="fillStudent" onclick="fillDemoCredentials('student1', 'student123', 'STUDENT')">Fill Student</button>
                    <span style="margin-left: 0.5rem;">Parent:</span> <button type="button" class="quick-fill-btn" id="fillParent" onclick="fillDemoCredentials('parent1', 'parent123', 'PARENT')">Fill Parent</button>
                </div>
            </div>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
