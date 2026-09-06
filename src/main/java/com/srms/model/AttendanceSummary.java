package com.srms.model;

import java.io.Serializable;

public class AttendanceSummary implements Serializable {
    private static final long serialVersionUID = 1L;

    private int studentId;
    private String studentName;
    private String rollNo;
    private int deptId;
    private String deptName;
    private int totalClasses;
    private int classesAttended;
    private int classesMissed;
    private double attendancePercentage;
    private boolean belowThreshold; // true if < 75.0%
    private int classesNeededFor75; // Number of additional consecutive classes needed to reach 75%
    private String status; // "Safe" or "At Risk"

    public AttendanceSummary() {}

    public AttendanceSummary(int studentId, String studentName, String rollNo, int totalClasses, int classesAttended) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.rollNo = rollNo;
        this.totalClasses = totalClasses;
        this.classesAttended = classesAttended;
        this.classesMissed = totalClasses - classesAttended;

        if (totalClasses == 0) {
            this.attendancePercentage = 100.0;
            this.belowThreshold = false;
            this.classesNeededFor75 = 0;
            this.status = "Safe";
        } else {
            this.attendancePercentage = ((double) classesAttended / totalClasses) * 100.0;
            // Round to 2 decimal places
            this.attendancePercentage = Math.round(this.attendancePercentage * 100.0) / 100.0;
            if (this.attendancePercentage < 75.0) {
                this.belowThreshold = true;
                this.status = "At Risk";
                // Math: (attended + X) / (total + X) >= 0.75 => 4*attended + 4X >= 3*total + 3X => X >= 3*total - 4*attended
                int needed = (3 * totalClasses) - (4 * classesAttended);
                this.classesNeededFor75 = Math.max(needed, 1);
            } else {
                this.belowThreshold = false;
                this.status = "Safe";
                this.classesNeededFor75 = 0;
            }
        }
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

    public int getTotalClasses() {
        return totalClasses;
    }

    public void setTotalClasses(int totalClasses) {
        this.totalClasses = totalClasses;
    }

    public int getClassesAttended() {
        return classesAttended;
    }

    public void setClassesAttended(int classesAttended) {
        this.classesAttended = classesAttended;
    }

    public int getClassesMissed() {
        return classesMissed;
    }

    public void setClassesMissed(int classesMissed) {
        this.classesMissed = classesMissed;
    }

    public double getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(double attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }

    public boolean isBelowThreshold() {
        return belowThreshold;
    }

    public void setBelowThreshold(boolean belowThreshold) {
        this.belowThreshold = belowThreshold;
    }

    public int getClassesNeededFor75() {
        return classesNeededFor75;
    }

    public void setClassesNeededFor75(int classesNeededFor75) {
        this.classesNeededFor75 = classesNeededFor75;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
