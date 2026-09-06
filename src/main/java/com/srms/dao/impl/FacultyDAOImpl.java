package com.srms.dao.impl;

import com.srms.dao.FacultyDAO;
import com.srms.model.Faculty;
import com.srms.util.DBConnection;
import com.srms.util.PasswordUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacultyDAOImpl implements FacultyDAO {

    @Override
    public Faculty findById(int facultyId) {
        String sql = "SELECT f.*, d.dept_name, u.username, u.password_hash FROM faculty f " +
                     "LEFT JOIN department d ON f.dept_id = d.dept_id " +
                     "LEFT JOIN users u ON f.user_id = u.user_id " +
                     "WHERE f.faculty_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, facultyId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapFaculty(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Faculty findByUserId(int userId) {
        String sql = "SELECT f.*, d.dept_name, u.username, u.password_hash FROM faculty f " +
                     "LEFT JOIN department d ON f.dept_id = d.dept_id " +
                     "LEFT JOIN users u ON f.user_id = u.user_id " +
                     "WHERE f.user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapFaculty(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Faculty> getAllFaculty() {
        List<Faculty> list = new ArrayList<>();
        String sql = "SELECT f.*, d.dept_name, u.username, u.password_hash FROM faculty f " +
                     "LEFT JOIN department d ON f.dept_id = d.dept_id " +
                     "LEFT JOIN users u ON f.user_id = u.user_id " +
                     "ORDER BY f.faculty_id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapFaculty(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean addFacultyWithUser(Faculty faculty, String initialPassword) {
        String userSql = "INSERT INTO users (username, password_hash, role) VALUES (?, ?, 'FACULTY')";
        String facultySql = "INSERT INTO faculty (user_id, employee_id, name, dept_id, designation, email, phone) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            int userId = -1;
            try (PreparedStatement psUser = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {
                psUser.setString(1, faculty.getUsername() != null ? faculty.getUsername() : faculty.getEmployeeId().toLowerCase());
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

            try (PreparedStatement psFaculty = conn.prepareStatement(facultySql, Statement.RETURN_GENERATED_KEYS)) {
                psFaculty.setInt(1, userId);
                psFaculty.setString(2, faculty.getEmployeeId());
                psFaculty.setString(3, faculty.getName());
                psFaculty.setInt(4, faculty.getDeptId());
                psFaculty.setString(5, faculty.getDesignation());
                psFaculty.setString(6, faculty.getEmail());
                psFaculty.setString(7, faculty.getPhone());
                psFaculty.executeUpdate();
                try (ResultSet rs = psFaculty.getGeneratedKeys()) {
                    if (rs.next()) {
                        faculty.setFacultyId(rs.getInt(1));
                    }
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
    public boolean updateFaculty(Faculty faculty) {
        String sql = "UPDATE faculty SET name = ?, dept_id = ?, designation = ?, email = ?, phone = ? WHERE faculty_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, faculty.getName());
            ps.setInt(2, faculty.getDeptId());
            ps.setString(3, faculty.getDesignation());
            ps.setString(4, faculty.getEmail());
            ps.setString(5, faculty.getPhone());
            ps.setInt(6, faculty.getFacultyId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteFaculty(int facultyId) {
        Faculty faculty = findById(facultyId);
        if (faculty == null) return false;

        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, faculty.getUserId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int getTotalFacultyCount() {
        String sql = "SELECT COUNT(*) FROM faculty";
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

    private Faculty mapFaculty(ResultSet rs) throws SQLException {
        Faculty f = new Faculty();
        f.setFacultyId(rs.getInt("faculty_id"));
        f.setUserId(rs.getInt("user_id"));
        f.setEmployeeId(rs.getString("employee_id"));
        f.setName(rs.getString("name"));
        f.setDeptId(rs.getInt("dept_id"));
        f.setDeptName(rs.getString("dept_name"));
        f.setDesignation(rs.getString("designation"));
        f.setEmail(rs.getString("email"));
        f.setPhone(rs.getString("phone"));
        f.setUsername(rs.getString("username"));
        f.setPasswordHash(rs.getString("password_hash"));
        return f;
    }
}
