package com.srms.controller;

import com.srms.dao.*;
import com.srms.dao.impl.*;
import com.srms.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/parent/dashboard")
public class ParentDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ParentDAO parentDAO;
    private StudentDAO studentDAO;
    private AttendanceDAO attendanceDAO;
    private MarksDAO marksDAO;
    private AnnouncementDAO announcementDAO;

    @Override
    public void init() throws ServletException {
        this.parentDAO = new ParentDAOImpl();
        this.studentDAO = new StudentDAOImpl();
        this.attendanceDAO = new AttendanceDAOImpl();
        this.marksDAO = new MarksDAOImpl();
        this.announcementDAO = new AnnouncementDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User loggedUser = (User) session.getAttribute("loggedUser");

        Parent parent = parentDAO.findByUserId(loggedUser.getUserId());
        if (parent == null) {
            // Auto-create parent record if missing for a user with PARENT role
            parent = new Parent();
            parent.setUserId(loggedUser.getUserId());
            parent.setName(loggedUser.getUsername());
            parent.setEmail(loggedUser.getUsername() + "@parent.com");
            parent.setPhone("0000000000");
            parent.setOccupation("Parent/Guardian");
            
            // Insert parent record into DB
            String insertSql = "INSERT INTO parent (user_id, name, email, phone, occupation) VALUES (?, ?, ?, ?, ?)";
            try (java.sql.Connection conn = com.srms.util.DBConnection.getConnection();
                 java.sql.PreparedStatement ps = conn.prepareStatement(insertSql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, parent.getUserId());
                ps.setString(2, parent.getName());
                ps.setString(3, parent.getEmail());
                ps.setString(4, parent.getPhone());
                ps.setString(5, parent.getOccupation());
                ps.executeUpdate();
                try (java.sql.ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        parent.setParentId(rs.getInt(1));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            if (parent.getParentId() <= 0) {
                parent = parentDAO.findByUserId(loggedUser.getUserId());
            }
        }
        if (parent == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Parent profile record not found.");
            return;
        }

        List<Student> linkedStudents = parentDAO.getLinkedStudents(parent.getParentId());
        if (linkedStudents.isEmpty()) {
            request.setAttribute("parent", parent);
            request.setAttribute("linkedStudents", linkedStudents);
            request.getRequestDispatcher("/parent-dashboard.jsp").forward(request, response);
            return;
        }

        // Child Selection Logic & Access Control
        String studentIdParam = request.getParameter("studentId");
        Student selectedStudent = null;

        if (studentIdParam != null && !studentIdParam.trim().isEmpty()) {
            try {
                int requestedStudentId = Integer.parseInt(studentIdParam.trim());
                boolean isLinked = parentDAO.isStudentLinkedToParent(parent.getParentId(), requestedStudentId);
                if (!isLinked) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: You do not have permission to view information for this student.");
                    return;
                }
                selectedStudent = linkedStudents.stream()
                        .filter(s -> s.getStudentId() == requestedStudentId)
                        .findFirst().orElse(null);
            } catch (NumberFormatException e) {
                // Invalid param, fallback to first child
            }
        }

        if (selectedStudent == null) {
            selectedStudent = linkedStudents.get(0);
        }

        // Metrics & Marks breakdowns for selected child
        AttendanceSummary attendanceSummary = attendanceDAO.getStudentAttendanceSummary(selectedStudent.getStudentId());
        List<Attendance> attendanceRecords = attendanceDAO.getAttendanceByStudent(selectedStudent.getStudentId());
        List<Mark> allMarks = marksDAO.getMarksByStudent(selectedStudent.getStudentId());

        List<Mark> sessionMarks = allMarks.stream()
                .filter(m -> !"SEMESTER".equalsIgnoreCase(m.getExamType()))
                .collect(Collectors.toList());

        List<Mark> endExamMarks = allMarks.stream()
                .filter(m -> "SEMESTER".equalsIgnoreCase(m.getExamType()))
                .collect(Collectors.toList());

        double sessionAverage = marksDAO.getStudentSessionAverage(selectedStudent.getStudentId());
        double endExamAverage = marksDAO.getStudentEndExamAverage(selectedStudent.getStudentId());

        List<Announcement> announcements = announcementDAO.getAnnouncementsForUser(
                "STUDENT", selectedStudent.getDeptId(), String.valueOf(selectedStudent.getBatchYear()));

        request.setAttribute("parent", parent);
        request.setAttribute("linkedStudents", linkedStudents);
        request.setAttribute("selectedStudent", selectedStudent);
        request.setAttribute("attendanceSummary", attendanceSummary);
        request.setAttribute("attendanceRecords", attendanceRecords);
        request.setAttribute("allMarks", allMarks);
        request.setAttribute("sessionMarks", sessionMarks);
        request.setAttribute("endExamMarks", endExamMarks);
        request.setAttribute("sessionAverage", sessionAverage);
        request.setAttribute("endExamAverage", endExamAverage);
        request.setAttribute("announcements", announcements);

        request.getRequestDispatcher("/parent-dashboard.jsp").forward(request, response);
    }
}
