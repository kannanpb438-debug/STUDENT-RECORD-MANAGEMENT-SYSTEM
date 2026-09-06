package com.srms.controller;

import com.srms.dao.MarksDAO;
import com.srms.dao.StudentDAO;
import com.srms.dao.impl.MarksDAOImpl;
import com.srms.dao.impl.StudentDAOImpl;
import com.srms.model.Mark;
import com.srms.model.Student;
import com.srms.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/student/marks")
public class StudentMarksServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private StudentDAO studentDAO;
    private MarksDAO marksDAO;

    @Override
    public void init() throws ServletException {
        this.studentDAO = new StudentDAOImpl();
        this.marksDAO = new MarksDAOImpl();
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

        List<Mark> marksList = marksDAO.getMarksByStudent(student.getStudentId());

        request.setAttribute("student", student);
        request.setAttribute("marksList", marksList);

        request.getRequestDispatcher("/view-marks.jsp").forward(request, response);
    }
}
