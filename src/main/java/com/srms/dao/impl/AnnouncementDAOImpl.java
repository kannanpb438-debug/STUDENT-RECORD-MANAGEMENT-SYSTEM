package com.srms.dao.impl;

import com.srms.dao.AnnouncementDAO;
import com.srms.model.Announcement;
import com.srms.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementDAOImpl implements AnnouncementDAO {

    @Override
    public List<Announcement> getAnnouncementsForUser(String role, int deptId, String batchYear) {
        List<Announcement> list = new ArrayList<>();
        String sql = "SELECT a.*, u.username AS poster_name FROM announcement a " +
                     "LEFT JOIN users u ON a.posted_by = u.user_id " +
                     "WHERE (a.target_role = 'ALL' OR a.target_role = ?) " +
                     "AND (a.target_dept = 0 OR a.target_dept = ?) " +
                     "AND (a.target_batch = 'ALL' OR a.target_batch = ?) " +
                     "ORDER BY a.posted_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, role != null ? role : "ALL");
            ps.setInt(2, deptId);
            ps.setString(3, batchYear != null ? batchYear : "ALL");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapAnnouncement(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Announcement> getAllAnnouncements() {
        List<Announcement> list = new ArrayList<>();
        String sql = "SELECT a.*, u.username AS poster_name FROM announcement a " +
                     "LEFT JOIN users u ON a.posted_by = u.user_id " +
                     "ORDER BY a.posted_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapAnnouncement(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean createAnnouncement(Announcement a) {
        String sql = "INSERT INTO announcement (title, message, posted_by, target_role, target_dept, target_batch) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, a.getTitle());
            ps.setString(2, a.getMessage());
            if (a.getPostedBy() > 0) {
                ps.setInt(3, a.getPostedBy());
            } else {
                ps.setInt(3, 1);
            }
            ps.setString(4, a.getTargetRole() != null ? a.getTargetRole() : "ALL");
            ps.setInt(5, a.getTargetDept());
            ps.setString(6, a.getTargetBatch() != null ? a.getTargetBatch() : "ALL");
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateAnnouncement(Announcement a) {
        String sql = "UPDATE announcement SET title = ?, message = ?, target_role = ?, target_dept = ?, target_batch = ? WHERE announcement_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, a.getTitle());
            ps.setString(2, a.getMessage());
            ps.setString(3, a.getTargetRole() != null ? a.getTargetRole() : "ALL");
            ps.setInt(4, a.getTargetDept());
            ps.setString(5, a.getTargetBatch() != null ? a.getTargetBatch() : "ALL");
            ps.setInt(6, a.getAnnouncementId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteAnnouncement(int announcementId) {
        String sql = "DELETE FROM announcement WHERE announcement_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, announcementId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Announcement getAnnouncementById(int announcementId) {
        String sql = "SELECT a.*, u.username AS poster_name FROM announcement a " +
                     "LEFT JOIN users u ON a.posted_by = u.user_id " +
                     "WHERE a.announcement_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, announcementId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapAnnouncement(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Announcement mapAnnouncement(ResultSet rs) throws SQLException {
        Announcement a = new Announcement();
        a.setAnnouncementId(rs.getInt("announcement_id"));
        a.setTitle(rs.getString("title"));
        a.setMessage(rs.getString("message"));
        a.setPostedBy(rs.getInt("posted_by"));
        a.setPosterName(rs.getString("poster_name"));
        a.setTargetRole(rs.getString("target_role"));
        a.setTargetDept(rs.getInt("target_dept"));
        a.setTargetBatch(rs.getString("target_batch"));
        a.setPostedAt(rs.getTimestamp("posted_at"));
        return a;
    }
}
