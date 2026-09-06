<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="SRMS New User Registration — Create a Student, Parent, or Faculty account.">
    <title>SRMS - New User Sign Up</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="login-body">

    <div class="login-card" style="max-width: 520px;">
        <div class="login-header">
            <h2>SRMS ACCOUNT REGISTRATION</h2>
            <p>Create a new Student, Parent, or Faculty Account</p>
        </div>

        <div class="login-form-container">
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger" id="registerError">
                    <i class="bi bi-exclamation-triangle-fill"></i>
                    <span>${errorMessage}</span>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/register" method="post" id="registerForm">
                <div class="form-group">
                    <label>Account Role</label>
                    <div style="display: flex; gap: 1.5rem; margin-top: 0.25rem; flex-wrap: wrap;">
                        <label style="cursor: pointer;">
                            <input type="radio" name="role" value="STUDENT" checked onclick="toggleRoleFields('STUDENT')"> <strong>Student</strong>
                        </label>
                        <label style="cursor: pointer;">
                            <input type="radio" name="role" value="PARENT" onclick="toggleRoleFields('PARENT')"> <strong>Parent / Guardian</strong>
                        </label>
                        <label style="cursor: pointer;">
                            <input type="radio" name="role" value="FACULTY" onclick="toggleRoleFields('FACULTY')"> <strong>Faculty / Staff</strong>
                        </label>
                    </div>
                </div>

                <div class="register-grid-2col" style="display: grid; grid-template-columns: 1fr 1fr; gap: 0.85rem;">
                    <div class="form-group">
                        <label for="username">Username (Login)</label>
                        <input type="text" id="username" name="username" class="form-control" placeholder="e.g. parent_john" required>
                    </div>

                    <div class="form-group">
                        <label for="password">Password</label>
                        <input type="password" id="password" name="password" class="form-control" placeholder="Enter password..." required>
                    </div>
                </div>

                <div class="form-group">
                    <label for="name">Full Name</label>
                    <input type="text" id="name" name="name" class="form-control" placeholder="e.g. John Doe" required>
                </div>

                <div class="form-group" id="deptGroup">
                    <label for="deptId">Department</label>
                    <select id="deptId" name="deptId" class="form-select">
                        <c:forEach var="d" items="${departments}">
                            <option value="${d.deptId}">${d.deptCode} - ${d.deptName}</option>
                        </c:forEach>
                    </select>
                </div>

                <!-- Student Specific Fields -->
                <div id="studentFields">
                    <div class="register-grid-2col" style="display: grid; grid-template-columns: 1fr 1fr; gap: 0.85rem;">
                        <div class="form-group">
                            <label for="rollNo">KTU Roll Number</label>
                            <input type="text" id="rollNo" name="rollNo" class="form-control" placeholder="e.g. TVE23CS004">
                        </div>
                        <div class="form-group">
                            <label for="semester">Semester</label>
                            <select id="semester" name="semester" class="form-select">
                                <option value="1">Semester 1</option>
                                <option value="2">Semester 2</option>
                                <option value="3" selected>Semester 3 (S3)</option>
                                <option value="4">Semester 4</option>
                                <option value="5">Semester 5</option>
                                <option value="6">Semester 6</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="batchYear">Batch Admission Year</label>
                        <input type="number" id="batchYear" name="batchYear" class="form-control" value="2023">
                    </div>
                </div>

                <!-- Parent Specific Fields -->
                <div id="parentFields" style="display: none;">
                    <div class="register-grid-2col" style="display: grid; grid-template-columns: 1fr 1fr; gap: 0.85rem;">
                        <div class="form-group">
                            <label for="studentRollNo">Child's Roll Number (To Link)</label>
                            <input type="text" id="studentRollNo" name="studentRollNo" class="form-control" placeholder="e.g. TVE23CS001">
                        </div>
                        <div class="form-group">
                            <label for="occupation">Occupation</label>
                            <input type="text" id="occupation" name="occupation" class="form-control" placeholder="e.g. Engineer / Business">
                        </div>
                    </div>
                </div>

                <!-- Faculty Specific Fields -->
                <div id="facultyFields" style="display: none;">
                    <div class="register-grid-2col" style="display: grid; grid-template-columns: 1fr 1fr; gap: 0.85rem;">
                        <div class="form-group">
                            <label for="employeeId">Employee ID</label>
                            <input type="text" id="employeeId" name="employeeId" class="form-control" placeholder="e.g. FAC-CSE-004">
                        </div>
                        <div class="form-group">
                            <label for="designation">Designation</label>
                            <select id="designation" name="designation" class="form-select">
                                <option value="Assistant Professor" selected>Assistant Professor</option>
                                <option value="Associate Professor">Associate Professor</option>
                                <option value="Professor">Professor</option>
                            </select>
                        </div>
                    </div>
                </div>

                <div class="register-grid-2col" style="display: grid; grid-template-columns: 1fr 1fr; gap: 0.85rem;">
                    <div class="form-group">
                        <label for="email">Email Address</label>
                        <input type="email" id="email" name="email" class="form-control" placeholder="name@college.edu">
                    </div>
                    <div class="form-group">
                        <label for="phone">Phone Number</label>
                        <input type="text" id="phone" name="phone" class="form-control" placeholder="9876543210">
                    </div>
                </div>

                <button type="submit" class="btn btn-primary" id="registerButton" style="width: 100%; margin-top: 0.5rem;">
                    <i class="bi bi-person-check-fill"></i> Register Account
                </button>
            </form>

            <div style="text-align: center; margin-top: 1rem; font-size: 0.85rem;">
                Already have an account? <a href="${pageContext.request.contextPath}/login" id="loginLink" style="color: var(--accent); font-weight: 600;">Log In Here</a>
            </div>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
