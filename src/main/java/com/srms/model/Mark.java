package com.srms.model;

import java.io.Serializable;

public class Mark implements Serializable {
    private static final long serialVersionUID = 1L;

    private int markId;
    private int studentId;
    private String studentName;
    private String rollNo;
    private int subjectId;
    private String subjectCode;
    private String subjectName;
    private String examType; // SERIES1, SERIES2, ASSIGNMENT, SEMESTER
    private double marksObtained;
    private double maxMarks;

    public Mark() {}

    public Mark(int markId, int studentId, int subjectId, String examType, double marksObtained, double maxMarks) {
        this.markId = markId;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.examType = examType;
        this.marksObtained = marksObtained;
        this.maxMarks = maxMarks;
    }

    public double getPercentage() {
        if (maxMarks <= 0) return 0.0;
        double pct = (marksObtained / maxMarks) * 100.0;
        return Math.round(pct * 100.0) / 100.0;
    }

    public String getGrade() {
        double pct = getPercentage();
        if (pct >= 90.0) return "S";
        if (pct >= 80.0) return "A";
        if (pct >= 70.0) return "B";
        if (pct >= 60.0) return "C";
        if (pct >= 50.0) return "D";
        if (pct >= 40.0) return "P";
        return "F";
    }

    public String getFormattedExamType() {
        if (examType == null) return "";
        switch (examType.toUpperCase()) {
            case "SERIES1": return "Series Test 1";
            case "SERIES2": return "Series Test 2";
            case "ASSIGNMENT": return "Assignment";
            case "SEMESTER": return "End Semester Exam";
            default: return examType;
        }
    }

    public int getMarkId() {
        return markId;
    }

    public void setMarkId(int markId) {
        this.markId = markId;
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

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public double getMarksObtained() {
        return marksObtained;
    }

    public void setMarksObtained(double marksObtained) {
        this.marksObtained = marksObtained;
    }

    public double getMaxMarks() {
        return maxMarks;
    }

    public void setMaxMarks(double maxMarks) {
        this.maxMarks = maxMarks;
    }
}
