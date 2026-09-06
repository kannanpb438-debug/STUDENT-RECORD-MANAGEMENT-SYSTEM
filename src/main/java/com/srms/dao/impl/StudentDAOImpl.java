package com.srms.dao.impl;

import com.srms.dao.StudentDAO;
import com.srms.model.Student;
import com.srms.util.DBConnection;
import com.srms.util.PasswordUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAOImpl implements StudentDAO {

    @Override
    public Student findById(int studentId) {
        String sql = "SELECT s.*, d.dept_name, u.username, u.password_hash FROM student s " +
                     "LEFT JOIN department d ON s.dept_id = d.dept_id " +
                     "LEFT JOIN users u ON s.user_id = u.user_id " +
                     "WHERE s.student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapStudent(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Student findByUserId(int userId) {
        String sql = "SELECT s.*, d.dept_name, u.username, u.password_hash FROM student s " +
                     "LEFT JOIN department d ON s.dept_id = d.dept_id " +
                     "LEFT JOIN users u ON s.user_id = u.user_id " +
                     "WHERE s.user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapStudent(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Student findByRollNo(String rollNo) {
        String sql = "SELECT s.*, d.dept_name, u.username, u.password_hash FROM student s " +
                     "LEFT JOIN department d ON s.dept_id = d.dept_id " +
                     "LEFT JOIN users u ON s.user_id = u.user_id " +
                     "WHERE s.roll_no = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rollNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapStudent(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT s.*, d.dept_name, u.username, u.password_hash FROM student s " +
                     "LEFT JOIN department d ON s.dept_id = d.dept_id " +
                     "LEFT JOIN users u ON s.user_id = u.user_id " +
                     "ORDER BY s.student_id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapStudent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Student> getStudentsByDeptAndSemester(int deptId, int semester) {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT s.*, d.dept_name, u.username, u.password_hash FROM student s " +
                     "LEFT JOIN department d ON s.dept_id = d.dept_id " +
                     "LEFT JOIN users u ON s.user_id = u.user_id " +
                     "WHERE s.dept_id = ? AND s.semester = ? " +
                     "ORDER BY s.roll_no ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, deptId);
            ps.setInt(2, semester);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapStudent(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean addStudentWithUser(Student student, String initialPassword) {
        String userSql = "INSERT INTO users (username, password_hash, role) VALUES (?, ?, 'STUDENT')";
        String studentSql = "INSERT INTO student (user_id, roll_no, name, dept_id, semester, batch_year, email, phone, address, dob) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Begin Transaction

            int userId = -1;
            try (PreparedStatement psUser = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {
                psUser.setString(1, student.getUsername() != null ? student.getUsername() : student.getRollNo().toLowerCase());
                psUser.setString(2, PasswordUtil.hashPassword(initialPassword));
                psUser.executeUpdate();
                try (ResultSet rs = psUser.getGeneratedKeys()) {
                    if (rs.next()) {
                        userId = rs.getInt(1);
                    }
                }
            }

            if (userId <= 0) {
                conn.rollback();
                return false;
            }

            try (PreparedStatement psStudent = conn.prepareStatement(studentSql, Statement.RETURN_GENERATED_KEYS)) {
                psStudent.setInt(1, userId);
                psStudent.setString(2, student.getRollNo());
                psStudent.setString(3, student.getName());
                psStudent.setInt(4, student.getDeptId());
                psStudent.setInt(5, student.getSemester());
                psStudent.setInt(6, student.getBatchYear());
                psStudent.setString(7, student.getEmail());
                psStudent.setString(8, student.getPhone());
                psStudent.setString(9, student.getAddress());
                psStudent.setDate(10, student.getDob());
                psStudent.executeUpdate();
                try (ResultSet rs = psStudent.getGeneratedKeys()) {
                    if (rs.next()) {
                        student.setStudentId(rs.getInt(1));
                    }
                }
            }

            conn.commit(); // Commit Transaction
            return true;
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
    public boolean updateStudent(Student student) {
        String sql = "UPDATE student SET name = ?, dept_id = ?, semester = ?, batch_year = ?, email = ?, phone = ?, address = ?, dob = ? WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, student.getName());
            ps.setInt(2, student.getDeptId());
            ps.setInt(3, student.getSemester());
            ps.setInt(4, student.getBatchYear());
            ps.setString(5, student.getEmail());
            ps.setString(6, student.getPhone());
            ps.setString(7, student.getAddress());
            ps.setDate(8, student.getDob());
            ps.setInt(9, student.getStudentId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteStudent(int studentId) {
        // Cascade delete via User table
        Student student = findById(studentId);
        if (student == null) return false;

        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, student.getUserId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int getTotalStudentCount() {
        String sql = "SELECT COUNT(*) FROM student";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Student mapStudent(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setStudentId(rs.getInt("student_id"));
        s.setUserId(rs.getInt("user_id"));
        s.setRollNo(rs.getString("roll_no"));
        s.setName(rs.getString("name"));
        s.setDeptId(rs.getInt("dept_id"));
        s.setDeptName(rs.getString("dept_name"));
        s.setSemester(rs.getInt("semester"));
        s.setBatchYear(rs.getInt("batch_year"));
        s.setEmail(rs.getString("email"));
        s.setPhone(rs.getString("phone"));
        s.setAddress(rs.getString("address"));
        s.setDob(rs.getDate("dob"));
        s.setUsername(rs.getString("username"));
        s.setPasswordHash(rs.getString("password_hash"));
        return s;
    }
}
