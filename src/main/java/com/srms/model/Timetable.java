package com.srms.model;

import java.io.Serializable;

public class Timetable implements Serializable {
    private static final long serialVersionUID = 1L;

    private int timetableId;
    private int deptId;
    private String deptName;
    private int semester;
    private String dayOfWeek;
    private int periodNo;
    private int subjectId;
    private String subjectCode;
    private String subjectName;
    private int facultyId;
    private String facultyName;
    private String roomNo;

    public Timetable() {}

    public Timetable(int timetableId, int deptId, int semester, String dayOfWeek, int periodNo, int subjectId, int facultyId, String roomNo) {
        this.timetableId = timetableId;
        this.deptId = deptId;
        this.semester = semester;
        this.dayOfWeek = dayOfWeek;
        this.periodNo = periodNo;
        this.subjectId = subjectId;
        this.facultyId = facultyId;
        this.roomNo = roomNo;
    }

    public int getTimetableId() {
        return timetableId;
    }

    public void setTimetableId(int timetableId) {
        this.timetableId = timetableId;
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

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getPeriodNo() {
        return periodNo;
    }

    public void setPeriodNo(int periodNo) {
        this.periodNo = periodNo;
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

    public int getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }
}
