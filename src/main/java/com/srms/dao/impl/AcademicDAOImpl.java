package com.srms.dao.impl;

import com.srms.dao.AcademicDAO;
import com.srms.model.Department;
import com.srms.model.Subject;
import com.srms.model.Timetable;
import com.srms.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AcademicDAOImpl implements AcademicDAO {

    @Override
    public List<Department> getAllDepartments() {
        List<Department> list = new ArrayList<>();
        String sql = "SELECT * FROM department ORDER BY dept_name ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Department(rs.getInt("dept_id"), rs.getString("dept_code"), rs.getString("dept_name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Department getDepartmentById(int deptId) {
        String sql = "SELECT * FROM department WHERE dept_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, deptId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Department(rs.getInt("dept_id"), rs.getString("dept_code"), rs.getString("dept_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addDepartment(Department dept) {
        String sql = "INSERT INTO department (dept_code, dept_name) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dept.getDeptCode());
            ps.setString(2, dept.getDeptName());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Subject> getAllSubjects() {
        List<Subject> list = new ArrayList<>();
        String sql = "SELECT s.*, d.dept_name, f.name AS faculty_name FROM subject s " +
                     "LEFT JOIN department d ON s.dept_id = d.dept_id " +
                     "LEFT JOIN faculty f ON s.faculty_id = f.faculty_id " +
                     "ORDER BY s.semester ASC, s.subject_code ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapSubject(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Subject> getSubjectsByFaculty(int facultyId) {
        List<Subject> list = new ArrayList<>();
        String sql = "SELECT s.*, d.dept_name, f.name AS faculty_name FROM subject s " +
                     "LEFT JOIN department d ON s.dept_id = d.dept_id " +
                     "LEFT JOIN faculty f ON s.faculty_id = f.faculty_id " +
                     "WHERE s.faculty_id = ? ORDER BY s.subject_code ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, facultyId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapSubject(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Subject> getSubjectsByDeptAndSemester(int deptId, int semester) {
        List<Subject> list = new ArrayList<>();
        String sql = "SELECT s.*, d.dept_name, f.name AS faculty_name FROM subject s " +
                     "LEFT JOIN department d ON s.dept_id = d.dept_id " +
                     "LEFT JOIN faculty f ON s.faculty_id = f.faculty_id " +
                     "WHERE s.dept_id = ? AND s.semester = ? ORDER BY s.subject_code ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, deptId);
            ps.setInt(2, semester);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapSubject(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Subject getSubjectById(int subjectId) {
        String sql = "SELECT s.*, d.dept_name, f.name AS faculty_name FROM subject s " +
                     "LEFT JOIN department d ON s.dept_id = d.dept_id " +
                     "LEFT JOIN faculty f ON s.faculty_id = f.faculty_id " +
                     "WHERE s.subject_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, subjectId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapSubject(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addSubject(Subject subject) {
        String sql = "INSERT INTO subject (subject_code, subject_name, semester, dept_id, faculty_id, credits) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, subject.getSubjectCode());
            ps.setString(2, subject.getSubjectName());
            ps.setInt(3, subject.getSemester());
            ps.setInt(4, subject.getDeptId());
            if (subject.getFacultyId() > 0) {
                ps.setInt(5, subject.getFacultyId());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            ps.setInt(6, subject.getCredits());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateSubject(Subject subject) {
        String sql = "UPDATE subject SET subject_code = ?, subject_name = ?, semester = ?, dept_id = ?, faculty_id = ?, credits = ? WHERE subject_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, subject.getSubjectCode());
            ps.setString(2, subject.getSubjectName());
            ps.setInt(3, subject.getSemester());
            ps.setInt(4, subject.getDeptId());
            if (subject.getFacultyId() > 0) {
                ps.setInt(5, subject.getFacultyId());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            ps.setInt(6, subject.getCredits());
            ps.setInt(7, subject.getSubjectId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteSubject(int subjectId) {
        String sql = "DELETE FROM subject WHERE subject_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, subjectId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Timetable> getTimetableByDeptAndSemester(int deptId, int semester) {
        List<Timetable> list = new ArrayList<>();
        String sql = "SELECT t.*, d.dept_name, s.subject_code, s.subject_name, f.name AS faculty_name FROM timetable t " +
                     "JOIN department d ON t.dept_id = d.dept_id " +
                     "JOIN subject s ON t.subject_id = s.subject_id " +
                     "LEFT JOIN faculty f ON t.faculty_id = f.faculty_id " +
                     "WHERE t.dept_id = ? AND t.semester = ? " +
                     "ORDER BY CASE t.day_of_week WHEN 'MONDAY' THEN 1 WHEN 'TUESDAY' THEN 2 WHEN 'WEDNESDAY' THEN 3 WHEN 'THURSDAY' THEN 4 WHEN 'FRIDAY' THEN 5 END, t.period_no ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, deptId);
            ps.setInt(2, semester);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapTimetable(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Timetable> getTimetableByFaculty(int facultyId) {
        List<Timetable> list = new ArrayList<>();
        String sql = "SELECT t.*, d.dept_name, s.subject_code, s.subject_name, f.name AS faculty_name FROM timetable t " +
                     "JOIN department d ON t.dept_id = d.dept_id " +
                     "JOIN subject s ON t.subject_id = s.subject_id " +
                     "LEFT JOIN faculty f ON t.faculty_id = f.faculty_id " +
                     "WHERE t.faculty_id = ? " +
                     "ORDER BY CASE t.day_of_week WHEN 'MONDAY' THEN 1 WHEN 'TUESDAY' THEN 2 WHEN 'WEDNESDAY' THEN 3 WHEN 'THURSDAY' THEN 4 WHEN 'FRIDAY' THEN 5 END, t.period_no ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, facultyId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapTimetable(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean addTimetableEntry(Timetable entry) {
        String sql = "INSERT INTO timetable (dept_id, semester, day_of_week, period_no, subject_id, faculty_id, room_no) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entry.getDeptId());
            ps.setInt(2, entry.getSemester());
            ps.setString(3, entry.getDayOfWeek());
            ps.setInt(4, entry.getPeriodNo());
            ps.setInt(5, entry.getSubjectId());
            if (entry.getFacultyId() > 0) {
                ps.setInt(6, entry.getFacultyId());
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            ps.setString(7, entry.getRoomNo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Subject mapSubject(ResultSet rs) throws SQLException {
        Subject s = new Subject();
        s.setSubjectId(rs.getInt("subject_id"));
        s.setSubjectCode(rs.getString("subject_code"));
        s.setSubjectName(rs.getString("subject_name"));
        s.setSemester(rs.getInt("semester"));
        s.setDeptId(rs.getInt("dept_id"));
        s.setDeptName(rs.getString("dept_name"));
        s.setFacultyId(rs.getInt("faculty_id"));
        s.setFacultyName(rs.getString("faculty_name"));
        s.setCredits(rs.getInt("credits"));
        return s;
    }

    private Timetable mapTimetable(ResultSet rs) throws SQLException {
        Timetable t = new Timetable();
        t.setTimetableId(rs.getInt("timetable_id"));
        t.setDeptId(rs.getInt("dept_id"));
        t.setDeptName(rs.getString("dept_name"));
        t.setSemester(rs.getInt("semester"));
        t.setDayOfWeek(rs.getString("day_of_week"));
        t.setPeriodNo(rs.getInt("period_no"));
        t.setSubjectId(rs.getInt("subject_id"));
        t.setSubjectCode(rs.getString("subject_code"));
        t.setSubjectName(rs.getString("subject_name"));
        t.setFacultyId(rs.getInt("faculty_id"));
        t.setFacultyName(rs.getString("faculty_name"));
        t.setRoomNo(rs.getString("room_no"));
        return t;
    }
}
