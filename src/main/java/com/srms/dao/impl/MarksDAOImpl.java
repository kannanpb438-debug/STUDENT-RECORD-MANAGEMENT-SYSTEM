package com.srms.dao.impl;

import com.srms.dao.MarksDAO;
import com.srms.model.Mark;
import com.srms.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MarksDAOImpl implements MarksDAO {

    @Override
    public boolean saveOrUpdateMarksBatch(List<Mark> marksList) {
        String deleteSql = "DELETE FROM marks WHERE student_id = ? AND subject_id = ? AND exam_type = ?";
        String insertSql = "INSERT INTO marks (student_id, subject_id, exam_type, marks_obtained, max_marks) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement psDel = conn.prepareStatement(deleteSql);
                 PreparedStatement psIns = conn.prepareStatement(insertSql)) {

                for (Mark m : marksList) {
                    psDel.setInt(1, m.getStudentId());
                    psDel.setInt(2, m.getSubjectId());
                    psDel.setString(3, m.getExamType());
                    psDel.addBatch();

                    psIns.setInt(1, m.getStudentId());
                    psIns.setInt(2, m.getSubjectId());
                    psIns.setString(3, m.getExamType());
                    psIns.setDouble(4, m.getMarksObtained());
                    psIns.setDouble(5, m.getMaxMarks() > 0 ? m.getMaxMarks() : 50.0);
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
    public List<Mark> getMarksByStudent(int studentId) {
        List<Mark> list = new ArrayList<>();
        String sql = "SELECT m.*, s.subject_code, s.subject_name FROM marks m " +
                     "JOIN subject s ON m.subject_id = s.subject_id " +
                     "WHERE m.student_id = ? ORDER BY s.subject_code ASC, m.exam_type ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapMark(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Mark> getMarksByStudentAndSubject(int studentId, int subjectId) {
        List<Mark> list = new ArrayList<>();
        String sql = "SELECT m.*, s.subject_code, s.subject_name FROM marks m " +
                     "JOIN subject s ON m.subject_id = s.subject_id " +
                     "WHERE m.student_id = ? AND m.subject_id = ? ORDER BY m.exam_type ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, subjectId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapMark(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Mark> getMarksBySubjectAndExam(int subjectId, String examType) {
        List<Mark> list = new ArrayList<>();
        String sql = "SELECT m.*, st.name AS student_name, st.roll_no, s.subject_code, s.subject_name FROM marks m " +
                     "JOIN student st ON m.student_id = st.student_id " +
                     "JOIN subject s ON m.subject_id = s.subject_id " +
                     "WHERE m.subject_id = ? AND m.exam_type = ? ORDER BY st.roll_no ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, subjectId);
            ps.setString(2, examType);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Mark m = mapMark(rs);
                    m.setStudentName(rs.getString("student_name"));
                    m.setRollNo(rs.getString("roll_no"));
                    list.add(m);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public double getAverageMarksBySubject(int subjectId, String examType) {
        String sql = "SELECT AVG(marks_obtained) FROM marks WHERE subject_id = ? AND exam_type = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, subjectId);
            ps.setString(2, examType);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    @Override
    public double getStudentSessionAverage(int studentId) {
        String sql = "SELECT marks_obtained, max_marks FROM marks WHERE student_id = ? AND exam_type <> 'SEMESTER'";
        double totalPct = 0.0;
        int count = 0;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    double obtained = rs.getDouble("marks_obtained");
                    double max = rs.getDouble("max_marks");
                    if (max > 0) {
                        totalPct += (obtained / max) * 100.0;
                        count++;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (count == 0) return 0.0;
        return Math.round((totalPct / count) * 100.0) / 100.0;
    }

    @Override
    public double getStudentEndExamAverage(int studentId) {
        String sql = "SELECT marks_obtained, max_marks FROM marks WHERE student_id = ? AND exam_type = 'SEMESTER'";
        double totalPct = 0.0;
        int count = 0;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    double obtained = rs.getDouble("marks_obtained");
                    double max = rs.getDouble("max_marks");
                    if (max > 0) {
                        totalPct += (obtained / max) * 100.0;
                        count++;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (count == 0) return 0.0;
        return Math.round((totalPct / count) * 100.0) / 100.0;
    }

    private Mark mapMark(ResultSet rs) throws SQLException {
        Mark m = new Mark();
        m.setMarkId(rs.getInt("mark_id"));
        m.setStudentId(rs.getInt("student_id"));
        m.setSubjectId(rs.getInt("subject_id"));
        m.setSubjectCode(rs.getString("subject_code"));
        m.setSubjectName(rs.getString("subject_name"));
        m.setExamType(rs.getString("exam_type"));
        m.setMarksObtained(rs.getDouble("marks_obtained"));
        m.setMaxMarks(rs.getDouble("max_marks"));
        return m;
    }
}
