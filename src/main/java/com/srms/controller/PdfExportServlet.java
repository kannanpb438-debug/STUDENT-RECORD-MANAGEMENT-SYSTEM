package com.srms.controller;

import com.srms.dao.*;
import com.srms.dao.impl.*;
import com.srms.model.*;
import com.srms.service.ReportPdfService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@WebServlet("/export/pdf")
public class PdfExportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private StudentDAO studentDAO;
    private MarksDAO marksDAO;
    private AttendanceDAO attendanceDAO;
    private FeeDAO feeDAO;
    private ReportPdfService pdfService;

    @Override
    public void init() throws ServletException {
        this.studentDAO = new StudentDAOImpl();
        this.marksDAO = new MarksDAOImpl();
        this.attendanceDAO = new AttendanceDAOImpl();
        this.feeDAO = new FeeDAOImpl();
        this.pdfService = new ReportPdfService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User loggedUser = (session != null) ? (User) session.getAttribute("loggedUser") : null;

        if (loggedUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String type = request.getParameter("type");
        int studentId = 0;

        if ("STUDENT".equalsIgnoreCase(loggedUser.getRole())) {
            Student s = studentDAO.findByUserId(loggedUser.getUserId());
            if (s != null) {
                studentId = s.getStudentId();
            }
        } else if ("PARENT".equalsIgnoreCase(loggedUser.getRole())) {
            ParentDAO parentDAO = new ParentDAOImpl();
            Parent parent = parentDAO.findByUserId(loggedUser.getUserId());
            String studentIdStr = request.getParameter("studentId");
            if (studentIdStr != null && !studentIdStr.isEmpty()) {
                try {
                    int requestedStudentId = Integer.parseInt(studentIdStr);
                    if (parent != null && parentDAO.isStudentLinkedToParent(parent.getParentId(), requestedStudentId)) {
                        studentId = requestedStudentId;
                    } else {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: You are not authorized to access reports for this student.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid student ID parameter.");
                    return;
                }
            } else if (parent != null) {
                List<Student> linked = parentDAO.getLinkedStudents(parent.getParentId());
                if (!linked.isEmpty()) {
                    studentId = linked.get(0).getStudentId();
                }
            }
        } else {
            String studentIdStr = request.getParameter("studentId");
            if (studentIdStr != null && !studentIdStr.isEmpty()) {
                try {
                    studentId = Integer.parseInt(studentIdStr);
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid student ID parameter.");
                    return;
                }
            }
        }

        Student student = studentDAO.findById(studentId);
        if (student == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Student record not found for PDF generation.");
            return;
        }

        try {
            byte[] pdfBytes = null;
            String fileName = "report.pdf";

            if ("marksheet".equalsIgnoreCase(type) || "marks".equalsIgnoreCase(type)) {
                List<Mark> marksList = marksDAO.getMarksByStudent(studentId);
                pdfBytes = pdfService.generateMarksheetPdf(student, marksList);
                fileName = "Marksheet_" + student.getRollNo() + ".pdf";

            } else if ("attendance".equalsIgnoreCase(type)) {
                List<Attendance> attendanceList = attendanceDAO.getAttendanceByStudent(studentId);
                double overallPct = attendanceDAO.getOverallAttendancePercentage(studentId);
                pdfBytes = pdfService.generateAttendanceReportPdf(student, attendanceList, overallPct);
                fileName = "Attendance_" + student.getRollNo() + ".pdf";

            } else if ("receipt".equalsIgnoreCase(type)) {
                String feeIdStr = request.getParameter("feeId");
                FeeRecord fee = null;
                if (feeIdStr != null && !feeIdStr.isEmpty()) {
                    fee = feeDAO.getFeeRecordById(Integer.parseInt(feeIdStr));
                } else {
                    List<FeeRecord> fees = feeDAO.getFeeRecordsByStudent(studentId);
                    if (!fees.isEmpty()) fee = fees.get(0);
                }

                if (fee != null) {
                    pdfBytes = pdfService.generateFeeReceiptPdf(student, fee);
                    fileName = "FeeReceipt_" + (fee.getReceiptNo() != null ? fee.getReceiptNo() : student.getRollNo()) + ".pdf";
                }
            }

            if (pdfBytes != null) {
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
                response.setContentLength(pdfBytes.length);

                try (OutputStream os = response.getOutputStream()) {
                    os.write(pdfBytes);
                    os.flush();
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid PDF export type requested.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "PDF Generation Error: " + e.getMessage());
        }
    }
}
