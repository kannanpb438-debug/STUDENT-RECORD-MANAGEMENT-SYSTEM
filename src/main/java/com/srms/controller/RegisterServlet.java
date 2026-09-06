package com.srms.controller;

import com.srms.dao.*;
import com.srms.dao.impl.*;
import com.srms.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private StudentDAO studentDAO;
    private FacultyDAO facultyDAO;
    private AcademicDAO academicDAO;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        this.studentDAO = new StudentDAOImpl();
        this.facultyDAO = new FacultyDAOImpl();
        this.academicDAO = new AcademicDAOImpl();
        this.userDAO = new UserDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Department> departments = academicDAO.getAllDepartments();
        request.setAttribute("departments", departments);
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String role = request.getParameter("role"); // STUDENT or FACULTY
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String deptIdStr = request.getParameter("deptId");
        int deptId = 0;
        if (deptIdStr != null && !deptIdStr.trim().isEmpty()) {
            try {
                deptId = Integer.parseInt(deptIdStr.trim());
            } catch (NumberFormatException ignored) {
                deptId = 0;
            }
        }
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Username and Password are required.");
            doGet(request, response);
            return;
        }

        if (userDAO.findByUsername(username.trim()) != null) {
            request.setAttribute("errorMessage", "Username '" + username + "' is already taken. Please choose another.");
            doGet(request, response);
            return;
        }

        boolean success = false;

        if ("FACULTY".equalsIgnoreCase(role)) {
            String empId = request.getParameter("employeeId");
            if (empId == null || empId.trim().isEmpty()) {
                empId = "FAC-" + username.trim().toUpperCase();
            }
            String designation = request.getParameter("designation");

            Faculty f = new Faculty();
            f.setUsername(username.trim());
            f.setEmployeeId(empId.trim());
            f.setName(name);
            f.setDeptId(deptId);
            f.setDesignation(designation != null ? designation : "Assistant Professor");
            f.setEmail(email);
            f.setPhone(phone);

            success = facultyDAO.addFacultyWithUser(f, password);

        } else if ("PARENT".equalsIgnoreCase(role)) {
            String studentRollNo = request.getParameter("studentRollNo");
            String occupation = request.getParameter("occupation");

            Parent p = new Parent();
            p.setUsername(username.trim());
            p.setName(name);
            p.setEmail(email);
            p.setPhone(phone);
            p.setOccupation(occupation != null ? occupation : "Parent/Guardian");

            ParentDAO parentDAO = new ParentDAOImpl();
            success = parentDAO.addParentWithUser(p, password, studentRollNo);

        } else { // STUDENT
            String rollNo = request.getParameter("rollNo");
            if (rollNo == null || rollNo.trim().isEmpty()) {
                rollNo = "STU-" + username.trim().toUpperCase();
            }

            String semStr = request.getParameter("semester");
            int semester = (semStr != null && !semStr.isEmpty()) ? Integer.parseInt(semStr) : 3;

            String batchStr = request.getParameter("batchYear");
            int batchYear = (batchStr != null && !batchStr.isEmpty()) ? Integer.parseInt(batchStr) : 2023;

            String address = request.getParameter("address");
            String dobStr = request.getParameter("dob");

            Student s = new Student();
            s.setUsername(username.trim());
            s.setRollNo(rollNo.trim());
            s.setName(name);
            s.setDeptId(deptId);
            s.setSemester(semester);
            s.setBatchYear(batchYear);
            s.setEmail(email);
            s.setPhone(phone);
            s.setAddress(address);
            if (dobStr != null && !dobStr.isEmpty()) {
                try {
                    s.setDob(Date.valueOf(dobStr));
                } catch (IllegalArgumentException e) {
                    // Ignore date parse error
                }
            }

            success = studentDAO.addStudentWithUser(s, password);
        }

        if (success) {
            response.sendRedirect(request.getContextPath() + "/login?registered=true");
        } else {
            request.setAttribute("errorMessage", "Failed to complete registration. Please check your inputs.");
            doGet(request, response);
        }
    }
}
