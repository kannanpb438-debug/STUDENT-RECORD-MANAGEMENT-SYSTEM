package com.srms.controller;

import com.srms.dao.*;
import com.srms.dao.impl.*;
import com.srms.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/faculty/attendance")
public class MarkAttendanceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private FacultyDAO facultyDAO;
    private AcademicDAO academicDAO;
    private StudentDAO studentDAO;
    private AttendanceDAO attendanceDAO;

    @Override
    public void init() throws ServletException {
        this.facultyDAO = new FacultyDAOImpl();
        this.academicDAO = new AcademicDAOImpl();
        this.studentDAO = new StudentDAOImpl();
        this.attendanceDAO = new AttendanceDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("loggedUser");
        Faculty faculty = facultyDAO.findByUserId(user.getUserId());

        List<Subject> subjects = (faculty != null) ? academicDAO.getSubjectsByFaculty(faculty.getFacultyId()) : academicDAO.getAllSubjects();

        String subjectIdStr = request.getParameter("subjectId");
        String dateStr = request.getParameter("date");
        String periodStr = request.getParameter("period");

        if (subjectIdStr != null && !subjectIdStr.trim().isEmpty()) {
            try {
                int subjectId = Integer.parseInt(subjectIdStr.trim());
                Subject subject = academicDAO.getSubjectById(subjectId);

                if (subject != null) {
                    Date attendanceDate = new Date(System.currentTimeMillis());
                    if (dateStr != null && !dateStr.trim().isEmpty()) {
                        try { attendanceDate = Date.valueOf(dateStr.trim()); } catch (IllegalArgumentException ignored) {}
                    }
                    int periodNo = 1;
                    if (periodStr != null && !periodStr.trim().isEmpty()) {
                        try { periodNo = Integer.parseInt(periodStr.trim()); } catch (NumberFormatException ignored) {}
                    }

                    List<Student> students = studentDAO.getStudentsByDeptAndSemester(subject.getDeptId(), subject.getSemester());
                    List<Attendance> existingAttendance = attendanceDAO.getAttendanceBySubjectAndDate(subjectId, attendanceDate);

                    request.setAttribute("selectedSubject", subject);
                    request.setAttribute("selectedDate", attendanceDate);
                    request.setAttribute("selectedPeriod", periodNo);
                    request.setAttribute("students", students);
                    request.setAttribute("existingAttendance", existingAttendance);
                }
            } catch (NumberFormatException ignored) {}
        }

        request.setAttribute("subjects", subjects);
        request.getRequestDispatcher("/mark-attendance.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("loggedUser");
        Faculty faculty = facultyDAO.findByUserId(user.getUserId());

        try {
            int subjectId = Integer.parseInt(request.getParameter("subjectId"));
            
            String dateStr = request.getParameter("attendanceDate");
            Date attendanceDate = new Date(System.currentTimeMillis());
            if (dateStr != null && !dateStr.trim().isEmpty()) {
                try {
                    attendanceDate = Date.valueOf(dateStr.trim());
                } catch (IllegalArgumentException ignored) {}
            }

            String periodStr = request.getParameter("periodNo");
            int periodNo = 1;
            if (periodStr != null && !periodStr.trim().isEmpty()) {
                try {
                    periodNo = Integer.parseInt(periodStr.trim());
                } catch (NumberFormatException ignored) {}
            }

            String[] studentIds = request.getParameterValues("studentIds");

            List<Attendance> attendanceList = new ArrayList<>();
            if (studentIds != null) {
                for (String sId : studentIds) {
                    int studentId = Integer.parseInt(sId);
                    String status = request.getParameter("status_" + studentId);
                    if (status == null) status = "ABSENT";

                    Attendance a = new Attendance();
                    a.setStudentId(studentId);
                    a.setSubjectId(subjectId);
                    a.setAttendanceDate(attendanceDate);
                    a.setStatus(status);
                    a.setMarkedBy(faculty != null ? faculty.getFacultyId() : 1);
                    a.setPeriodNo(periodNo);

                    attendanceList.add(a);
                }
            }

            boolean success = attendanceDAO.markAttendanceBatch(attendanceList);
            if (success) {
                response.sendRedirect(request.getContextPath() + "/faculty/attendance?subjectId=" + subjectId + "&date=" + attendanceDate + "&msg=Attendance+marked+successfully");
            } else {
                response.sendRedirect(request.getContextPath() + "/faculty/attendance?subjectId=" + subjectId + "&error=Failed+to+mark+attendance");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/faculty/attendance?error=Error+marking+attendance:+" + e.getMessage());
        }
    }
}
