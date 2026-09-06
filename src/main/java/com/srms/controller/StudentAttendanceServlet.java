package com.srms.controller;

import com.srms.dao.*;
import com.srms.dao.impl.*;
import com.srms.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/student/attendance")
public class StudentAttendanceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private StudentDAO studentDAO;
    private AttendanceDAO attendanceDAO;
    private AcademicDAO academicDAO;

    @Override
    public void init() throws ServletException {
        this.studentDAO = new StudentDAOImpl();
        this.attendanceDAO = new AttendanceDAOImpl();
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

        List<Attendance> attendanceList = attendanceDAO.getAttendanceByStudent(student.getStudentId());
        double overallPercentage = attendanceDAO.getOverallAttendancePercentage(student.getStudentId());
        AttendanceSummary attendanceSummary = attendanceDAO.getStudentAttendanceSummary(student.getStudentId());

        List<Subject> subjects = academicDAO.getSubjectsByDeptAndSemester(student.getDeptId(), student.getSemester());
        Map<Integer, Double> subjectPercentages = new HashMap<>();
        for (Subject s : subjects) {
            double pct = attendanceDAO.getSubjectAttendancePercentage(student.getStudentId(), s.getSubjectId());
            subjectPercentages.put(s.getSubjectId(), pct);
        }

        request.setAttribute("student", student);
        request.setAttribute("attendanceList", attendanceList);
        request.setAttribute("overallPercentage", overallPercentage);
        request.setAttribute("attendanceSummary", attendanceSummary);
        request.setAttribute("subjects", subjects);
        request.setAttribute("subjectPercentages", subjectPercentages);

        request.getRequestDispatcher("/view-attendance.jsp").forward(request, response);
    }
}
