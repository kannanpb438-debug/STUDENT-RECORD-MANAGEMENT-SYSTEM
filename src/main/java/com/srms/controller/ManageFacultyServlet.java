package com.srms.controller;

import com.srms.dao.AcademicDAO;
import com.srms.dao.FacultyDAO;
import com.srms.dao.impl.AcademicDAOImpl;
import com.srms.dao.impl.FacultyDAOImpl;
import com.srms.model.Department;
import com.srms.model.Faculty;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/faculty")
public class ManageFacultyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private FacultyDAO facultyDAO;
    private AcademicDAO academicDAO;

    @Override
    public void init() throws ServletException {
        this.facultyDAO = new FacultyDAOImpl();
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
                    int facultyId = Integer.parseInt(idStr);
                    facultyDAO.deleteFaculty(facultyId);
                    response.sendRedirect(request.getContextPath() + "/admin/faculty?msg=Faculty+deleted+successfully");
                    return;
                } catch (NumberFormatException e) {
                    response.sendRedirect(request.getContextPath() + "/admin/faculty?error=Invalid+Faculty+ID");
                    return;
                }
            }
        }

        List<Faculty> facultyList = facultyDAO.getAllFaculty();
        List<Department> departments = academicDAO.getAllDepartments();

        request.setAttribute("facultyList", facultyList);
        request.setAttribute("departments", departments);
        request.getRequestDispatcher("/manage-faculty.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("add".equalsIgnoreCase(action)) {
                Faculty f = new Faculty();
                f.setEmployeeId(request.getParameter("employeeId"));
                f.setName(request.getParameter("name"));
                f.setDeptId(parseIntOrDefault(request.getParameter("deptId"), 1));
                f.setDesignation(request.getParameter("designation"));
                f.setEmail(request.getParameter("email"));
                f.setPhone(request.getParameter("phone"));
                f.setUsername(request.getParameter("username"));

                String password = request.getParameter("password");
                if (password == null || password.isEmpty()) {
                    password = "faculty123";
                }

                boolean success = facultyDAO.addFacultyWithUser(f, password);
                if (success) {
                    response.sendRedirect(request.getContextPath() + "/admin/faculty?msg=Faculty+added+successfully");
                } else {
                    response.sendRedirect(request.getContextPath() + "/admin/faculty?error=Failed+to+add+faculty");
                }
            } else if ("edit".equalsIgnoreCase(action)) {
                int facultyId = Integer.parseInt(request.getParameter("facultyId"));
                Faculty f = facultyDAO.findById(facultyId);
                if (f != null) {
                    f.setName(request.getParameter("name"));
                    f.setDeptId(parseIntOrDefault(request.getParameter("deptId"), f.getDeptId()));
                    f.setDesignation(request.getParameter("designation"));
                    f.setEmail(request.getParameter("email"));
                    f.setPhone(request.getParameter("phone"));
                    facultyDAO.updateFaculty(f);
                }
                response.sendRedirect(request.getContextPath() + "/admin/faculty?msg=Faculty+updated+successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/faculty?error=Error+processing+request:+" + e.getMessage());
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
