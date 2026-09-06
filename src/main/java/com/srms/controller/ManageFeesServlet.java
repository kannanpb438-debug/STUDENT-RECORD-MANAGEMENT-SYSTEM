package com.srms.controller;

import com.srms.dao.FeeDAO;
import com.srms.dao.StudentDAO;
import com.srms.dao.impl.FeeDAOImpl;
import com.srms.dao.impl.StudentDAOImpl;
import com.srms.model.FeeRecord;
import com.srms.model.Student;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/admin/fees")
public class ManageFeesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private FeeDAO feeDAO;
    private StudentDAO studentDAO;

    @Override
    public void init() throws ServletException {
        this.feeDAO = new FeeDAOImpl();
        this.studentDAO = new StudentDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("markPaid".equalsIgnoreCase(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                try {
                    int feeId = Integer.parseInt(idStr);
                    feeDAO.markAsPaid(feeId, "REC-" + System.currentTimeMillis());
                    response.sendRedirect(request.getContextPath() + "/admin/fees?msg=Fee+status+updated+to+PAID");
                    return;
                } catch (NumberFormatException e) {
                    response.sendRedirect(request.getContextPath() + "/admin/fees?error=Invalid+Fee+ID");
                    return;
                }
            }
        }

        List<FeeRecord> feeRecords = feeDAO.getAllFeeRecords();
        List<Student> students = studentDAO.getAllStudents();

        request.setAttribute("feeRecords", feeRecords);
        request.setAttribute("students", students);
        request.getRequestDispatcher("/manage-fees.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int studentId = Integer.parseInt(request.getParameter("studentId"));
            int semester = Integer.parseInt(request.getParameter("semester"));
            double amount = Double.parseDouble(request.getParameter("amount"));
            String dueDateStr = request.getParameter("dueDate");
            String status = request.getParameter("status");

            FeeRecord f = new FeeRecord();
            f.setStudentId(studentId);
            f.setSemester(semester);
            f.setAmount(amount);
            f.setStatus(status != null ? status : "PENDING");
            if (dueDateStr != null && !dueDateStr.isEmpty()) {
                try { f.setDueDate(Date.valueOf(dueDateStr)); } catch (IllegalArgumentException ignored) {}
            }

            if ("PAID".equalsIgnoreCase(status)) {
                f.setPaymentDate(new Date(System.currentTimeMillis()));
                f.setReceiptNo("REC-" + System.currentTimeMillis());
            }

            feeDAO.addFeeRecord(f);
            response.sendRedirect(request.getContextPath() + "/admin/fees?msg=Fee+record+added+successfully");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/fees?error=Error+adding+fee+record:+" + e.getMessage());
        }
    }
}
