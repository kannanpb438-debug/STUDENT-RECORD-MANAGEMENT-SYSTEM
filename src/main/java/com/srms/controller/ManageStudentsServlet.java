package com.srms.controller;

import com.srms.dao.AcademicDAO;
import com.srms.dao.StudentDAO;
import com.srms.dao.impl.AcademicDAOImpl;
import com.srms.dao.impl.StudentDAOImpl;
import com.srms.model.Department;
import com.srms.model.Student;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/admin/students")
public class ManageStudentsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private StudentDAO studentDAO;
    private AcademicDAO academicDAO;

    @Override
    public void init() throws ServletException {
        this.studentDAO = new StudentDAOImpl();
        this.academicDAO = new AcademicDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("delete".equalsIgnoreCase(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                try {
                    int studentId = Integer.parseInt(idStr);
                    studentDAO.deleteStudent(studentId);
                    response.sendRedirect(request.getContextPath() + "/admin/students?msg=Student+deleted+successfully");
                    return;
                } catch (NumberFormatException e) {
                    response.sendRedirect(request.getContextPath() + "/admin/students?error=Invalid+Student+ID");
                    return;
                }
            }
        }

        List<Student> students = studentDAO.getAllStudents();
        List<Department> departments = academicDAO.getAllDepartments();

        request.setAttribute("students", students);
        request.setAttribute("departments", departments);
        request.getRequestDispatcher("/manage-students.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("add".equalsIgnoreCase(action)) {
                Student s = new Student();
                s.setRollNo(request.getParameter("rollNo"));
                s.setName(request.getParameter("name"));
                s.setDeptId(parseIntOrDefault(request.getParameter("deptId"), 1));
                s.setSemester(parseIntOrDefault(request.getParameter("semester"), 1));
                s.setBatchYear(parseIntOrDefault(request.getParameter("batchYear"), 2023));
                s.setEmail(request.getParameter("email"));
                s.setPhone(request.getParameter("phone"));
                s.setAddress(request.getParameter("address"));
                String dobStr = request.getParameter("dob");
                if (dobStr != null && !dobStr.isEmpty()) {
                    try { s.setDob(Date.valueOf(dobStr)); } catch (IllegalArgumentException ignored) {}
                }
                s.setUsername(request.getParameter("username"));

                String password = request.getParameter("password");
                if (password == null || password.isEmpty()) {
                    password = "student123";
                }

                boolean success = studentDAO.addStudentWithUser(s, password);
                if (success) {
                    response.sendRedirect(request.getContextPath() + "/admin/students?msg=Student+added+successfully");
                } else {
                    response.sendRedirect(request.getContextPath() + "/admin/students?error=Failed+to+add+student");
                }
            } else if ("edit".equalsIgnoreCase(action)) {
                int studentId = Integer.parseInt(request.getParameter("studentId"));
                Student s = studentDAO.findById(studentId);
                if (s != null) {
                    s.setName(request.getParameter("name"));
                    s.setDeptId(parseIntOrDefault(request.getParameter("deptId"), s.getDeptId()));
                    s.setSemester(parseIntOrDefault(request.getParameter("semester"), s.getSemester()));
                    s.setBatchYear(parseIntOrDefault(request.getParameter("batchYear"), s.getBatchYear()));
                    s.setEmail(request.getParameter("email"));
                    s.setPhone(request.getParameter("phone"));
                    s.setAddress(request.getParameter("address"));
                    String dobStr = request.getParameter("dob");
                    if (dobStr != null && !dobStr.isEmpty()) {
                        try { s.setDob(Date.valueOf(dobStr)); } catch (IllegalArgumentException ignored) {}
                    }
                    studentDAO.updateStudent(s);
                }
                response.sendRedirect(request.getContextPath() + "/admin/students?msg=Student+updated+successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/students?error=Error+processing+request:+" + e.getMessage());
        }
    }

    private int parseIntOrDefault(String val, int def) {
        if (val == null || val.trim().isEmpty()) return def;
        try {
            return Integer.parseInt(val.trim());
        } catch (NumberFormatException e) {
            return def;
        }
    }
}
