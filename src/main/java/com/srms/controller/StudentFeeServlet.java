package com.srms.controller;

import com.srms.dao.FeeDAO;
import com.srms.dao.StudentDAO;
import com.srms.dao.impl.FeeDAOImpl;
import com.srms.dao.impl.StudentDAOImpl;
import com.srms.model.FeeRecord;
import com.srms.model.Student;
import com.srms.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/student/fees")
public class StudentFeeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private StudentDAO studentDAO;
    private FeeDAO feeDAO;

    @Override
    public void init() throws ServletException {
        this.studentDAO = new StudentDAOImpl();
        this.feeDAO = new FeeDAOImpl();
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

        List<FeeRecord> feeRecords = feeDAO.getFeeRecordsByStudent(student.getStudentId());

        request.setAttribute("student", student);
        request.setAttribute("feeRecords", feeRecords);

        request.getRequestDispatcher("/view-fees.jsp").forward(request, response);
    }
}
