package com.srms.controller;

import com.srms.dao.AcademicDAO;
import com.srms.dao.FacultyDAO;
import com.srms.dao.impl.AcademicDAOImpl;
import com.srms.dao.impl.FacultyDAOImpl;
import com.srms.model.Department;
import com.srms.model.Faculty;
import com.srms.model.Subject;
import com.srms.model.Timetable;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/courses")
public class ManageCoursesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private AcademicDAO academicDAO;
    private FacultyDAO facultyDAO;

    @Override
    public void init() throws ServletException {
        this.academicDAO = new AcademicDAOImpl();
        this.facultyDAO = new FacultyDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("delete".equalsIgnoreCase(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                try {
                    int subjectId = Integer.parseInt(idStr);
                    academicDAO.deleteSubject(subjectId);
                    response.sendRedirect(request.getContextPath() + "/admin/courses?msg=Course+deleted+successfully");
                    return;
                } catch (NumberFormatException e) {
                    response.sendRedirect(request.getContextPath() + "/admin/courses?error=Invalid+Course+ID");
                    return;
                }
            }
        }

        List<Subject> subjects = academicDAO.getAllSubjects();
        List<Department> departments = academicDAO.getAllDepartments();
        List<Faculty> facultyList = facultyDAO.getAllFaculty();

        request.setAttribute("subjects", subjects);
        request.setAttribute("departments", departments);
        request.setAttribute("facultyList", facultyList);

        request.getRequestDispatcher("/manage-courses.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("addSubject".equalsIgnoreCase(action)) {
                Subject s = new Subject();
                s.setSubjectCode(request.getParameter("subjectCode"));
                s.setSubjectName(request.getParameter("subjectName"));
                s.setSemester(parseIntOrDefault(request.getParameter("semester"), 1));
                s.setDeptId(parseIntOrDefault(request.getParameter("deptId"), 1));
                String facIdStr = request.getParameter("facultyId");
                if (facIdStr != null && !facIdStr.isEmpty()) {
                    s.setFacultyId(parseIntOrDefault(facIdStr, 0));
                }
                s.setCredits(parseIntOrDefault(request.getParameter("credits"), 3));

                academicDAO.addSubject(s);
                response.sendRedirect(request.getContextPath() + "/admin/courses?msg=Subject+added+successfully");

            } else if ("addTimetable".equalsIgnoreCase(action)) {
                Timetable t = new Timetable();
                t.setDeptId(parseIntOrDefault(request.getParameter("deptId"), 1));
                t.setSemester(parseIntOrDefault(request.getParameter("semester"), 1));
                t.setDayOfWeek(request.getParameter("dayOfWeek"));
                t.setPeriodNo(parseIntOrDefault(request.getParameter("periodNo"), 1));
                t.setSubjectId(parseIntOrDefault(request.getParameter("subjectId"), 1));
                String facIdStr = request.getParameter("facultyId");
                if (facIdStr != null && !facIdStr.isEmpty()) {
                    t.setFacultyId(parseIntOrDefault(facIdStr, 0));
                }
                t.setRoomNo(request.getParameter("roomNo"));

                academicDAO.addTimetableEntry(t);
                response.sendRedirect(request.getContextPath() + "/admin/courses?msg=Timetable+entry+added+successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/courses?error=Error+processing+request:+" + e.getMessage());
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
