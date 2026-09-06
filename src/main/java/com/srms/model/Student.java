package com.srms.model;

import java.sql.Date;

/**
 * Concrete Subclass: Student
 * Extends User, demonstrating Polymorphism & Encapsulation.
 */
public class Student extends User {
    private static final long serialVersionUID = 1L;

    private int studentId;
    private String rollNo;
    private String name;
    private int deptId;
    private String deptName;
    private int semester;
    private int batchYear;
    private String email;
    private String phone;
    private String address;
    private Date dob;

    public Student() {
        super();
        setRole("STUDENT");
    }

    public Student(int userId, String username, String passwordHash, int studentId, String rollNo, String name, int deptId, int semester, int batchYear, String email, String phone) {
        super(userId, username, passwordHash, "STUDENT");
        this.studentId = studentId;
        this.rollNo = rollNo;
        this.name = name;
        this.deptId = deptId;
        this.semester = semester;
        this.batchYear = batchYear;
        this.email = email;
        this.phone = phone;
    }

    @Override
    public String getDashboardSummary() {
        return "Student Portal - Welcome " + name + " (Roll No: " + rollNo + "). Access your academic record, attendance, marks, and fees.";
    }

    // Encapsulation Getters & Setters
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getBatchYear() {
        return batchYear;
    }

    public void setBatchYear(int batchYear) {
        this.batchYear = batchYear;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }
}
