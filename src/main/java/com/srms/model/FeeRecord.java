package com.srms.model;

import java.io.Serializable;
import java.sql.Date;

public class FeeRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private int feeId;
    private int studentId;
    private String studentName;
    private String rollNo;
    private int semester;
    private double amount;
    private String status; // PAID, PENDING
    private Date dueDate;
    private Date paymentDate;
    private String receiptNo;

    public FeeRecord() {}

    public FeeRecord(int feeId, int studentId, int semester, double amount, String status, Date dueDate, Date paymentDate, String receiptNo) {
        this.feeId = feeId;
        this.studentId = studentId;
        this.semester = semester;
        this.amount = amount;
        this.status = status;
        this.dueDate = dueDate;
        this.paymentDate = paymentDate;
        this.receiptNo = receiptNo;
    }

    public int getFeeId() {
        return feeId;
    }

    public void setFeeId(int feeId) {
        this.feeId = feeId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }
}
