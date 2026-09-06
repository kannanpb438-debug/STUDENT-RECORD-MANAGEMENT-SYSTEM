package com.srms.controller;

import com.srms.dao.AcademicDAO;
import com.srms.dao.FacultyDAO;
import com.srms.dao.impl.AcademicDAOImpl;
import com.srms.dao.impl.FacultyDAOImpl;
import com.srms.model.Faculty;
import com.srms.model.Timetable;
import com.srms.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/faculty/timetable")
public class FacultyTimetableServlet extends HttpServlet {
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
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("loggedUser");
        Faculty faculty = facultyDAO.findByUserId(user.getUserId());

        List<Timetable> timetable = (faculty != null) ? academicDAO.getTimetableByFaculty(faculty.getFacultyId()) : List.of();

        request.setAttribute("faculty", faculty);
        request.setAttribute("timetable", timetable);
        request.getRequestDispatcher("/faculty-timetable.jsp").forward(request, response);
    }
}
