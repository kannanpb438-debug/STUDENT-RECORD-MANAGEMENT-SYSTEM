package com.srms.controller;

import com.srms.dao.*;
import com.srms.dao.impl.*;
import com.srms.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/faculty/dashboard")
public class FacultyDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private FacultyDAO facultyDAO;
    private AcademicDAO academicDAO;
    private AnnouncementDAO announcementDAO;
    private AttendanceDAO attendanceDAO;

    @Override
    public void init() throws ServletException {
        this.facultyDAO = new FacultyDAOImpl();
        this.academicDAO = new AcademicDAOImpl();
        this.announcementDAO = new AnnouncementDAOImpl();
        this.attendanceDAO = new AttendanceDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("loggedUser");

        Faculty faculty = facultyDAO.findByUserId(user.getUserId());
        int facultyId = (faculty != null) ? faculty.getFacultyId() : 0;
        List<Subject> assignedSubjects = (faculty != null) ? academicDAO.getSubjectsByFaculty(faculty.getFacultyId()) : List.of();
        List<Announcement> announcements = announcementDAO.getAnnouncementsForUser("FACULTY", faculty != null ? faculty.getDeptId() : 0, "ALL");

        List<AttendanceSummary> allSummaries = attendanceDAO.getAllStudentAttendanceSummariesForFaculty(facultyId);
        List<AttendanceSummary> atRiskStudents = attendanceDAO.getAtRiskStudentsForFaculty(facultyId);

        int totalStudents = allSummaries.size();
        int studentsBelow75Count = atRiskStudents.size();
        double avgClassAttendance = allSummaries.isEmpty() ? 0.0 : allSummaries.stream().mapToDouble(AttendanceSummary::getAttendancePercentage).average().orElse(0.0);
        avgClassAttendance = Math.round(avgClassAttendance * 100.0) / 100.0;

        request.setAttribute("faculty", faculty);
        request.setAttribute("assignedSubjects", assignedSubjects);
        request.setAttribute("announcements", announcements);
        request.setAttribute("totalStudents", totalStudents);
        request.setAttribute("studentsBelow75Count", studentsBelow75Count);
        request.setAttribute("avgClassAttendance", avgClassAttendance);
        request.setAttribute("atRiskStudents", atRiskStudents);

        request.getRequestDispatcher("/faculty-dashboard.jsp").forward(request, response);
    }
}
