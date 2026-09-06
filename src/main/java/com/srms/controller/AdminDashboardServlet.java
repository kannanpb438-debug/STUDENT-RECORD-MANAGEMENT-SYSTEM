package com.srms.controller;

import com.srms.dao.*;
import com.srms.dao.impl.*;
import com.srms.model.Announcement;
import com.srms.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private StudentDAO studentDAO;
    private FacultyDAO facultyDAO;
    private AttendanceDAO attendanceDAO;
    private FeeDAO feeDAO;
    private AnnouncementDAO announcementDAO;

    @Override
    public void init() throws ServletException {
        this.studentDAO = new StudentDAOImpl();
        this.facultyDAO = new FacultyDAOImpl();
        this.attendanceDAO = new AttendanceDAOImpl();
        this.feeDAO = new FeeDAOImpl();
        this.announcementDAO = new AnnouncementDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int totalStudents = studentDAO.getTotalStudentCount();
        int totalFaculty = facultyDAO.getTotalFacultyCount();
        int attendanceDefaulters = attendanceDAO.getDefaultersCount(75.0);
        int pendingFeesCount = feeDAO.getPendingFeeCount();
        double pendingFeesAmount = feeDAO.getTotalPendingAmount();

        List<Announcement> announcements = announcementDAO.getAllAnnouncements();

        request.setAttribute("totalStudents", totalStudents);
        request.setAttribute("totalFaculty", totalFaculty);
        request.setAttribute("attendanceDefaulters", attendanceDefaulters);
        request.setAttribute("pendingFeesCount", pendingFeesCount);
        request.setAttribute("pendingFeesAmount", pendingFeesAmount);
        request.setAttribute("announcements", announcements);

        request.getRequestDispatcher("/admin-dashboard.jsp").forward(request, response);
    }
}
