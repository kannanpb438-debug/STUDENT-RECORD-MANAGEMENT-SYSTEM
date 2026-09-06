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

@WebServlet("/faculty/marks")
public class EnterMarksServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private FacultyDAO facultyDAO;
    private AcademicDAO academicDAO;
    private StudentDAO studentDAO;
    private MarksDAO marksDAO;

    @Override
    public void init() throws ServletException {
        this.facultyDAO = new FacultyDAOImpl();
        this.academicDAO = new AcademicDAOImpl();
        this.studentDAO = new StudentDAOImpl();
        this.marksDAO = new MarksDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("loggedUser");
        Faculty faculty = facultyDAO.findByUserId(user.getUserId());

        List<Subject> subjects = (faculty != null) ? academicDAO.getSubjectsByFaculty(faculty.getFacultyId()) : academicDAO.getAllSubjects();

        String subjectIdStr = request.getParameter("subjectId");
        String examType = request.getParameter("examType");

        if (subjectIdStr != null && !subjectIdStr.trim().isEmpty() && examType != null && !examType.trim().isEmpty()) {
            try {
                int subjectId = Integer.parseInt(subjectIdStr.trim());
                Subject subject = academicDAO.getSubjectById(subjectId);
                if (subject != null) {
                    List<Student> students = studentDAO.getStudentsByDeptAndSemester(subject.getDeptId(), subject.getSemester());
                    List<Mark> existingMarks = marksDAO.getMarksBySubjectAndExam(subjectId, examType.trim());

                    request.setAttribute("selectedSubject", subject);
                    request.setAttribute("selectedExamType", examType.trim());
                    request.setAttribute("students", students);
                    request.setAttribute("existingMarks", existingMarks);
                }
            } catch (NumberFormatException ignored) {}
        }

        request.setAttribute("subjects", subjects);
        request.getRequestDispatcher("/enter-marks.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int subjectId = Integer.parseInt(request.getParameter("subjectId"));
            String examType = request.getParameter("examType");
            String maxMarksStr = request.getParameter("maxMarks");
            double maxMarks = 50.0;
            if (maxMarksStr != null && !maxMarksStr.trim().isEmpty()) {
                try {
                    maxMarks = Double.parseDouble(maxMarksStr.trim());
                } catch (NumberFormatException ignored) {}
            }

            if (maxMarks <= 0) maxMarks = 50.0;

            String[] studentIds = request.getParameterValues("studentIds");

            List<Mark> marksList = new ArrayList<>();
            if (studentIds != null) {
                for (String sId : studentIds) {
                    int studentId = Integer.parseInt(sId);
                    String markValStr = request.getParameter("mark_" + studentId);
                    double marksObtained = 0.0;
                    if (markValStr != null && !markValStr.trim().isEmpty()) {
                        try {
                            marksObtained = Double.parseDouble(markValStr.trim());
                        } catch (NumberFormatException ignored) {}
                    }

                    // Enforce range boundaries
                    if (marksObtained < 0) marksObtained = 0.0;
                    if (marksObtained > maxMarks) marksObtained = maxMarks;

                    Mark m = new Mark();
                    m.setStudentId(studentId);
                    m.setSubjectId(subjectId);
                    m.setExamType(examType);
                    m.setMarksObtained(marksObtained);
                    m.setMaxMarks(maxMarks);

                    marksList.add(m);
                }
            }

            boolean success = marksDAO.saveOrUpdateMarksBatch(marksList);
            if (success) {
                response.sendRedirect(request.getContextPath() + "/faculty/marks?subjectId=" + subjectId + "&examType=" + examType + "&msg=Marks+updated+successfully");
            } else {
                response.sendRedirect(request.getContextPath() + "/faculty/marks?subjectId=" + subjectId + "&examType=" + examType + "&error=Failed+to+save+marks");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/faculty/marks?error=Error+entering+marks:+" + e.getMessage());
        }
    }
}
