package com.srms.controller;

import com.srms.dao.*;
import com.srms.dao.impl.*;
import com.srms.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/student/dashboard")
public class StudentDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private StudentDAO studentDAO;
    private AttendanceDAO attendanceDAO;
    private MarksDAO marksDAO;
    private FeeDAO feeDAO;
    private AnnouncementDAO announcementDAO;

    @Override
    public void init() throws ServletException {
        this.studentDAO = new StudentDAOImpl();
        this.attendanceDAO = new AttendanceDAOImpl();
        this.marksDAO = new MarksDAOImpl();
        this.feeDAO = new FeeDAOImpl();
        this.announcementDAO = new AnnouncementDAOImpl();
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

        AttendanceSummary attendanceSummary = attendanceDAO.getStudentAttendanceSummary(student.getStudentId());
        double sessionAverage = marksDAO.getStudentSessionAverage(student.getStudentId());
        double endExamAverage = marksDAO.getStudentEndExamAverage(student.getStudentId());

        List<Mark> recentMarks = marksDAO.getMarksByStudent(student.getStudentId());
        List<FeeRecord> feeRecords = feeDAO.getFeeRecordsByStudent(student.getStudentId());
        List<Announcement> announcements = announcementDAO.getAnnouncementsForUser("STUDENT", student.getDeptId(), String.valueOf(student.getBatchYear()));

        boolean hasPendingFee = feeRecords.stream().anyMatch(f -> "PENDING".equalsIgnoreCase(f.getStatus()));

        request.setAttribute("student", student);
        request.setAttribute("attendancePercentage", attendanceSummary.getAttendancePercentage());
        request.setAttribute("attendanceSummary", attendanceSummary);
        request.setAttribute("sessionAverage", sessionAverage);
        request.setAttribute("endExamAverage", endExamAverage);
        request.setAttribute("recentMarks", recentMarks);
        request.setAttribute("feeRecords", feeRecords);
        request.setAttribute("hasPendingFee", hasPendingFee);
        request.setAttribute("announcements", announcements);

        request.getRequestDispatcher("/student-dashboard.jsp").forward(request, response);
    }
}
