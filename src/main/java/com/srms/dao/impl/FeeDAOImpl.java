package com.srms.dao.impl;

import com.srms.dao.FeeDAO;
import com.srms.model.FeeRecord;
import com.srms.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeeDAOImpl implements FeeDAO {

    @Override
    public List<FeeRecord> getFeeRecordsByStudent(int studentId) {
        List<FeeRecord> list = new ArrayList<>();
        String sql = "SELECT f.*, st.name AS student_name, st.roll_no FROM fee_record f " +
                     "JOIN student st ON f.student_id = st.student_id " +
                     "WHERE f.student_id = ? ORDER BY f.semester DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapFee(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public FeeRecord getFeeRecordById(int feeId) {
        String sql = "SELECT f.*, st.name AS student_name, st.roll_no FROM fee_record f " +
                     "JOIN student st ON f.student_id = st.student_id " +
                     "WHERE f.fee_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, feeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapFee(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<FeeRecord> getAllFeeRecords() {
        List<FeeRecord> list = new ArrayList<>();
        String sql = "SELECT f.*, st.name AS student_name, st.roll_no FROM fee_record f " +
                     "JOIN student st ON f.student_id = st.student_id " +
                     "ORDER BY f.fee_id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapFee(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean addFeeRecord(FeeRecord fee) {
        String sql = "INSERT INTO fee_record (student_id, semester, amount, status, due_date, payment_date, receipt_no) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, fee.getStudentId());
            ps.setInt(2, fee.getSemester());
            ps.setDouble(3, fee.getAmount());
            ps.setString(4, fee.getStatus() != null ? fee.getStatus() : "PENDING");
            ps.setDate(5, fee.getDueDate());
            ps.setDate(6, fee.getPaymentDate());
            ps.setString(7, fee.getReceiptNo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean markAsPaid(int feeId, String receiptNo) {
        String sql = "UPDATE fee_record SET status = 'PAID', payment_date = ?, receipt_no = ? WHERE fee_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, new Date(System.currentTimeMillis()));
            ps.setString(2, receiptNo != null ? receiptNo : "REC-" + System.currentTimeMillis());
            ps.setInt(3, feeId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int getPendingFeeCount() {
        String sql = "SELECT COUNT(*) FROM fee_record WHERE status = 'PENDING'";
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

    @Override
    public double getTotalPendingAmount() {
        String sql = "SELECT SUM(amount) FROM fee_record WHERE status = 'PENDING'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    private FeeRecord mapFee(ResultSet rs) throws SQLException {
        FeeRecord f = new FeeRecord();
        f.setFeeId(rs.getInt("fee_id"));
        f.setStudentId(rs.getInt("student_id"));
        f.setStudentName(rs.getString("student_name"));
        f.setRollNo(rs.getString("roll_no"));
        f.setSemester(rs.getInt("semester"));
        f.setAmount(rs.getDouble("amount"));
        f.setStatus(rs.getString("status"));
        f.setDueDate(rs.getDate("due_date"));
        f.setPaymentDate(rs.getDate("payment_date"));
        f.setReceiptNo(rs.getString("receipt_no"));
        return f;
    }
}
