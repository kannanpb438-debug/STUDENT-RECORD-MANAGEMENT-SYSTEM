package com.srms.dao.impl;

import com.srms.dao.AttendanceDAO;
import com.srms.model.Attendance;
import com.srms.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAOImpl implements AttendanceDAO {

    @Override
    public boolean markAttendanceBatch(List<Attendance> attendanceList) {
        String sql = "INSERT INTO attendance (student_id, subject_id, attendance_date, status, marked_by, period_no) " +
                     "VALUES (?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE status = VALUES(status), marked_by = VALUES(marked_by)";
        // For H2 compatibility, fallback to MERGE or delete + insert if ON DUPLICATE fails
        String deleteSql = "DELETE FROM attendance WHERE student_id = ? AND subject_id = ? AND attendance_date = ? AND period_no = ?";
        String insertSql = "INSERT INTO attendance (student_id, subject_id, attendance_date, status, marked_by, period_no) VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement psDel = conn.prepareStatement(deleteSql);
                 PreparedStatement psIns = conn.prepareStatement(insertSql)) {
                
                for (Attendance a : attendanceList) {
                    psDel.setInt(1, a.getStudentId());
                    psDel.setInt(2, a.getSubjectId());
                    psDel.setDate(3, a.getAttendanceDate());
                    psDel.setInt(4, a.getPeriodNo() > 0 ? a.getPeriodNo() : 1);
                    psDel.addBatch();

                    psIns.setInt(1, a.getStudentId());
                    psIns.setInt(2, a.getSubjectId());
                    psIns.setDate(3, a.getAttendanceDate());
                    psIns.setString(4, a.getStatus());
                    if (a.getMarkedBy() > 0) {
                        psIns.setInt(5, a.getMarkedBy());
                    } else {
                        psIns.setNull(5, Types.INTEGER);
                    }
                    psIns.setInt(6, a.getPeriodNo() > 0 ? a.getPeriodNo() : 1);
                    psIns.addBatch();
                }

                psDel.executeBatch();
                psIns.executeBatch();
                conn.commit();
                return true;
            }
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
        return false;
    }

    @Override
    public List<Attendance> getAttendanceByStudent(int studentId) {
        List<Attendance> list = new ArrayList<>();
        String sql = "SELECT a.*, s.subject_code, s.subject_name FROM attendance a " +
                     "JOIN subject s ON a.subject_id = s.subject_id " +
                     "WHERE a.student_id = ? ORDER BY a.attendance_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapAttendance(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Attendance> getAttendanceByStudentAndSubject(int studentId, int subjectId) {
        List<Attendance> list = new ArrayList<>();
        String sql = "SELECT a.*, s.subject_code, s.subject_name FROM attendance a " +
                     "JOIN subject s ON a.subject_id = s.subject_id " +
                     "WHERE a.student_id = ? AND a.subject_id = ? ORDER BY a.attendance_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, subjectId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapAttendance(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Attendance> getAttendanceBySubjectAndDate(int subjectId, Date date) {
        List<Attendance> list = new ArrayList<>();
        String sql = "SELECT a.*, st.name AS student_name, st.roll_no, s.subject_code, s.subject_name FROM attendance a " +
                     "JOIN student st ON a.student_id = st.student_id " +
                     "JOIN subject s ON a.subject_id = s.subject_id " +
                     "WHERE a.subject_id = ? AND a.attendance_date = ? ORDER BY st.roll_no ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, subjectId);
            ps.setDate(2, date);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Attendance a = mapAttendance(rs);
                    a.setStudentName(rs.getString("student_name"));
                    a.setRollNo(rs.getString("roll_no"));
                    list.add(a);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public double getOverallAttendancePercentage(int studentId) {
        String sql = "SELECT COUNT(*) AS total, SUM(CASE WHEN status = 'PRESENT' THEN 1 ELSE 0 END) AS present " +
                     "FROM attendance WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int total = rs.getInt("total");
                    int present = rs.getInt("present");
                    if (total == 0) return 100.0;
                    return ((double) present / total) * 100.0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    @Override
    public double getSubjectAttendancePercentage(int studentId, int subjectId) {
        String sql = "SELECT COUNT(*) AS total, SUM(CASE WHEN status = 'PRESENT' THEN 1 ELSE 0 END) AS present " +
                     "FROM attendance WHERE student_id = ? AND subject_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, subjectId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int total = rs.getInt("total");
                    int present = rs.getInt("present");
                    if (total == 0) return 100.0;
                    return ((double) present / total) * 100.0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    @Override
    public int getDefaultersCount(double thresholdPercentage) {
        String sql = "SELECT student_id, COUNT(*) AS total, SUM(CASE WHEN status = 'PRESENT' THEN 1 ELSE 0 END) AS present " +
                     "FROM attendance GROUP BY student_id HAVING total > 0 AND ((SUM(CASE WHEN status = 'PRESENT' THEN 1 ELSE 0 END) * 100.0) / COUNT(*)) < ?";
        int count = 0;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, thresholdPercentage);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    count++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    private Attendance mapAttendance(ResultSet rs) throws SQLException {
        Attendance a = new Attendance();
        a.setAttendanceId(rs.getInt("attendance_id"));
        a.setStudentId(rs.getInt("student_id"));
        a.setSubjectId(rs.getInt("subject_id"));
        a.setSubjectCode(rs.getString("subject_code"));
        a.setSubjectName(rs.getString("subject_name"));
        a.setAttendanceDate(rs.getDate("attendance_date"));
        a.setStatus(rs.getString("status"));
        a.setMarkedBy(rs.getInt("marked_by"));
        a.setPeriodNo(rs.getInt("period_no"));
        return a;
    }

    @Override
    public com.srms.model.AttendanceSummary getStudentAttendanceSummary(int studentId) {
        String sql = "SELECT s.student_id, s.name, s.roll_no, s.dept_id, d.dept_name, " +
                     "COUNT(a.attendance_id) AS total_classes, " +
                     "SUM(CASE WHEN a.status = 'PRESENT' THEN 1 ELSE 0 END) AS attended_classes " +
                     "FROM student s " +
                     "LEFT JOIN department d ON s.dept_id = d.dept_id " +
                     "LEFT JOIN attendance a ON s.student_id = a.student_id " +
                     "WHERE s.student_id = ? " +
                     "GROUP BY s.student_id, s.name, s.roll_no, s.dept_id, d.dept_name";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    String rollNo = rs.getString("roll_no");
                    int total = rs.getInt("total_classes");
                    int attended = rs.getInt("attended_classes");
                    com.srms.model.AttendanceSummary summary = new com.srms.model.AttendanceSummary(studentId, name, rollNo, total, attended);
                    summary.setDeptId(rs.getInt("dept_id"));
                    summary.setDeptName(rs.getString("dept_name"));
                    return summary;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new com.srms.model.AttendanceSummary(studentId, "Unknown", "", 0, 0);
    }

    @Override
    public List<com.srms.model.AttendanceSummary> getAllStudentAttendanceSummariesForFaculty(int facultyId) {
        List<com.srms.model.AttendanceSummary> list = new ArrayList<>();
        String sql;
        if (facultyId > 0) {
            sql = "SELECT s.student_id, s.name, s.roll_no, s.dept_id, d.dept_name, " +
                  "COUNT(a.attendance_id) AS total_classes, " +
                  "SUM(CASE WHEN a.status = 'PRESENT' THEN 1 ELSE 0 END) AS attended_classes " +
                  "FROM student s " +
                  "LEFT JOIN department d ON s.dept_id = d.dept_id " +
                  "LEFT JOIN attendance a ON s.student_id = a.student_id " +
                  "WHERE s.dept_id IN (SELECT f.dept_id FROM faculty f WHERE f.faculty_id = ?) " +
                  "GROUP BY s.student_id, s.name, s.roll_no, s.dept_id, d.dept_name";
        } else {
            sql = "SELECT s.student_id, s.name, s.roll_no, s.dept_id, d.dept_name, " +
                  "COUNT(a.attendance_id) AS total_classes, " +
                  "SUM(CASE WHEN a.status = 'PRESENT' THEN 1 ELSE 0 END) AS attended_classes " +
                  "FROM student s " +
                  "LEFT JOIN department d ON s.dept_id = d.dept_id " +
                  "LEFT JOIN attendance a ON s.student_id = a.student_id " +
                  "GROUP BY s.student_id, s.name, s.roll_no, s.dept_id, d.dept_name";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (facultyId > 0) {
                ps.setInt(1, facultyId);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int studentId = rs.getInt("student_id");
                    String name = rs.getString("name");
                    String rollNo = rs.getString("roll_no");
                    int total = rs.getInt("total_classes");
                    int attended = rs.getInt("attended_classes");
                    com.srms.model.AttendanceSummary summary = new com.srms.model.AttendanceSummary(studentId, name, rollNo, total, attended);
                    summary.setDeptId(rs.getInt("dept_id"));
                    summary.setDeptName(rs.getString("dept_name"));
                    list.add(summary);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<com.srms.model.AttendanceSummary> getAtRiskStudentsForFaculty(int facultyId) {
        List<com.srms.model.AttendanceSummary> all = getAllStudentAttendanceSummariesForFaculty(facultyId);
        List<com.srms.model.AttendanceSummary> atRisk = new ArrayList<>();
        for (com.srms.model.AttendanceSummary s : all) {
            if (s.isBelowThreshold()) {
                atRisk.add(s);
            }
        }
        atRisk.sort((a, b) -> Double.compare(a.getAttendancePercentage(), b.getAttendancePercentage()));
        return atRisk;
    }
}
