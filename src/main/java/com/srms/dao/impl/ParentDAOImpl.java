package com.srms.dao.impl;

import com.srms.dao.ParentDAO;
import com.srms.model.Parent;
import com.srms.model.Student;
import com.srms.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParentDAOImpl implements ParentDAO {

    @Override
    public Parent findByUserId(int userId) {
        String sql = "SELECT p.*, u.username, u.password_hash FROM parent p " +
                     "JOIN users u ON p.user_id = u.user_id WHERE p.user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapParent(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Parent findById(int parentId) {
        String sql = "SELECT p.*, u.username, u.password_hash FROM parent p " +
                     "JOIN users u ON p.user_id = u.user_id WHERE p.parent_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, parentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapParent(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Student> getLinkedStudents(int parentId) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT s.*, d.dept_name, u.username, u.password_hash FROM student s " +
                     "JOIN parent_student ps ON s.student_id = ps.student_id " +
                     "LEFT JOIN department d ON s.dept_id = d.dept_id " +
                     "JOIN users u ON s.user_id = u.user_id " +
                     "WHERE ps.parent_id = ? ORDER BY s.roll_no ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, parentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Student st = new Student();
                    st.setStudentId(rs.getInt("student_id"));
                    st.setUserId(rs.getInt("user_id"));
                    st.setUsername(rs.getString("username"));
                    st.setPasswordHash(rs.getString("password_hash"));
                    st.setRollNo(rs.getString("roll_no"));
                    st.setName(rs.getString("name"));
                    st.setDeptId(rs.getInt("dept_id"));
                    st.setDeptName(rs.getString("dept_name"));
                    st.setSemester(rs.getInt("semester"));
                    st.setBatchYear(rs.getInt("batch_year"));
                    st.setEmail(rs.getString("email"));
                    st.setPhone(rs.getString("phone"));
                    st.setAddress(rs.getString("address"));
                    st.setDob(rs.getDate("dob"));
                    students.add(st);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    @Override
    public boolean isStudentLinkedToParent(int parentId, int studentId) {
        String sql = "SELECT 1 FROM parent_student WHERE parent_id = ? AND student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, parentId);
            ps.setInt(2, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean addParentWithUser(Parent parent, String initialPassword, String studentRollNo) {
        String userSql = "INSERT INTO users (username, password_hash, role) VALUES (?, ?, 'PARENT')";
        String parentSql = "INSERT INTO parent (user_id, name, email, phone, occupation) VALUES (?, ?, ?, ?, ?)";
        String linkSql = "INSERT INTO parent_student (parent_id, student_id) VALUES (?, ?)";
        String findStudentSql = "SELECT student_id FROM student WHERE roll_no = ?";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Check student exists if rollNo provided
            Integer studentId = null;
            if (studentRollNo != null && !studentRollNo.trim().isEmpty()) {
                try (PreparedStatement psSt = conn.prepareStatement(findStudentSql)) {
                    psSt.setString(1, studentRollNo.trim());
                    try (ResultSet rsSt = psSt.executeQuery()) {
                        if (rsSt.next()) {
                            studentId = rsSt.getInt("student_id");
                        }
                    }
                }
            }

            // 2. Insert User
            int userId = -1;
            try (PreparedStatement psUser = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {
                psUser.setString(1, parent.getUsername());
                psUser.setString(2, com.srms.util.PasswordUtil.hashPassword(initialPassword));
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

            // 3. Insert Parent
            int parentId = -1;
            try (PreparedStatement psParent = conn.prepareStatement(parentSql, Statement.RETURN_GENERATED_KEYS)) {
                psParent.setInt(1, userId);
                psParent.setString(2, parent.getName());
                psParent.setString(3, parent.getEmail());
                psParent.setString(4, parent.getPhone());
                psParent.setString(5, parent.getOccupation());
                psParent.executeUpdate();
                try (ResultSet rs = psParent.getGeneratedKeys()) {
                    if (rs.next()) {
                        parentId = rs.getInt(1);
                        parent.setParentId(parentId);
                    }
                }
            }

            if (parentId <= 0) {
                conn.rollback();
                return false;
            }

            // 4. Link Student if found
            if (studentId != null) {
                try (PreparedStatement psLink = conn.prepareStatement(linkSql)) {
                    psLink.setInt(1, parentId);
                    psLink.setInt(2, studentId);
                    psLink.executeUpdate();
                }
            }

            conn.commit();
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
    public boolean linkParentToStudent(int parentId, int studentId) {
        String sql = "INSERT INTO parent_student (parent_id, student_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, parentId);
            ps.setInt(2, studentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Parent mapParent(ResultSet rs) throws SQLException {
        Parent p = new Parent();
        p.setParentId(rs.getInt("parent_id"));
        p.setUserId(rs.getInt("user_id"));
        p.setUsername(rs.getString("username"));
        p.setPasswordHash(rs.getString("password_hash"));
        p.setName(rs.getString("name"));
        p.setEmail(rs.getString("email"));
        p.setPhone(rs.getString("phone"));
        p.setOccupation(rs.getString("occupation"));
        return p;
    }
}
