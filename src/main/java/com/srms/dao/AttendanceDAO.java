package com.srms.dao;

import com.srms.model.Attendance;
import com.srms.model.AttendanceSummary;
import java.sql.Date;
import java.util.List;

public interface AttendanceDAO {
    boolean markAttendanceBatch(List<Attendance> attendanceList);
    List<Attendance> getAttendanceByStudent(int studentId);
    List<Attendance> getAttendanceByStudentAndSubject(int studentId, int subjectId);
    List<Attendance> getAttendanceBySubjectAndDate(int subjectId, Date date);
    double getOverallAttendancePercentage(int studentId);
    double getSubjectAttendancePercentage(int studentId, int subjectId);
    int getDefaultersCount(double thresholdPercentage);
    
    AttendanceSummary getStudentAttendanceSummary(int studentId);
    List<AttendanceSummary> getAtRiskStudentsForFaculty(int facultyId);
    List<AttendanceSummary> getAllStudentAttendanceSummariesForFaculty(int facultyId);
}

