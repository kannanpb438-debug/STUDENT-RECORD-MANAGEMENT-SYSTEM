package com.srms.model;

/**
 * Concrete Subclass: Faculty
 * Extends User, demonstrating Polymorphism & Encapsulation.
 */
public class Faculty extends User {
    private static final long serialVersionUID = 1L;

    private int facultyId;
    private String employeeId;
    private String name;
    private int deptId;
    private String deptName;
    private String designation;
    private String email;
    private String phone;

    public Faculty() {
        super();
        setRole("FACULTY");
    }

    public Faculty(int userId, String username, String passwordHash, int facultyId, String employeeId, String name, int deptId, String designation, String email, String phone) {
        super(userId, username, passwordHash, "FACULTY");
        this.facultyId = facultyId;
        this.employeeId = employeeId;
        this.name = name;
        this.deptId = deptId;
        this.designation = designation;
        this.email = email;
        this.phone = phone;
    }

    @Override
    public String getDashboardSummary() {
        return "Faculty Portal - Welcome " + name + " (" + designation + "). Manage attendance and internal marks.";
    }

    // Encapsulation Getters & Setters
    public int getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
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
}
