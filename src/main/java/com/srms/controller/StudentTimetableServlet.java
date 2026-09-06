package com.srms.controller;

import com.srms.dao.AcademicDAO;
import com.srms.dao.StudentDAO;
import com.srms.dao.impl.AcademicDAOImpl;
import com.srms.dao.impl.StudentDAOImpl;
import com.srms.model.Student;
import com.srms.model.Timetable;
import com.srms.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/student/timetable")
public class StudentTimetableServlet extends HttpServlet {
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
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("loggedUser");
        Student student = studentDAO.findByUserId(user.getUserId());

        if (student == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Student profile record not found.");
            return;
        }

        List<Timetable> timetable = academicDAO.getTimetableByDeptAndSemester(student.getDeptId(), student.getSemester());

        request.setAttribute("student", student);
        request.setAttribute("timetable", timetable);

        request.getRequestDispatcher("/view-timetable.jsp").forward(request, response);
    }
}
