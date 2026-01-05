package dao;

import config.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Announcement;

public class AnnouncementDAO {

    public List<Announcement> getAllAnnouncements() {
        List<Announcement> announcements = new ArrayList<>();
        String sql = "SELECT * FROM announcements WHERE is_active = true ORDER BY publish_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Announcement announcement = new Announcement();
                announcement.setId(rs.getInt("id"));
                announcement.setTitle(rs.getString("title"));
                announcement.setContent(rs.getString("content"));
                announcement.setAuthor(rs.getString("author"));
                announcement.setPublishDate(rs.getDate("publish_date").toLocalDate());
                if (rs.getDate("expiry_date") != null) {
                    announcement.setExpiryDate(rs.getDate("expiry_date").toLocalDate());
                }
                announcement.setTargetAudience(rs.getString("target_audience"));
                announcement.setActive(rs.getBoolean("is_active"));
                announcements.add(announcement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return announcements;
    }

    public List<Announcement> getAnnouncementsForAudience(String audience) {
        List<Announcement> announcements = new ArrayList<>();
        String sql = "SELECT * FROM announcements WHERE is_active = true AND (target_audience = 'ALL' OR target_audience = ?) ORDER BY publish_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, audience);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Announcement announcement = new Announcement();
                announcement.setId(rs.getInt("id"));
                announcement.setTitle(rs.getString("title"));
                announcement.setContent(rs.getString("content"));
                announcement.setAuthor(rs.getString("author"));
                announcement.setPublishDate(rs.getDate("publish_date").toLocalDate());
                if (rs.getDate("expiry_date") != null) {
                    announcement.setExpiryDate(rs.getDate("expiry_date").toLocalDate());
                }
                announcement.setTargetAudience(rs.getString("target_audience"));
                announcement.setActive(rs.getBoolean("is_active"));
                announcements.add(announcement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return announcements;
    }

    public boolean addAnnouncement(Announcement announcement) {
        String sql = "INSERT INTO announcements (title, content, author, publish_date, expiry_date, target_audience, is_active) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, announcement.getTitle());
            stmt.setString(2, announcement.getContent());
            stmt.setString(3, announcement.getAuthor());
            stmt.setDate(4, Date.valueOf(announcement.getPublishDate()));
            if (announcement.getExpiryDate() != null) {
                stmt.setDate(5, Date.valueOf(announcement.getExpiryDate()));
            } else {
                stmt.setNull(5, Types.DATE);
            }
            stmt.setString(6, announcement.getTargetAudience());
            stmt.setBoolean(7, announcement.isActive());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateAnnouncement(Announcement announcement) {
        String sql = "UPDATE announcements SET title = ?, content = ?, expiry_date = ?, target_audience = ?, is_active = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, announcement.getTitle());
            stmt.setString(2, announcement.getContent());
            if (announcement.getExpiryDate() != null) {
                stmt.setDate(3, Date.valueOf(announcement.getExpiryDate()));
            } else {
                stmt.setNull(3, Types.DATE);
            }
            stmt.setString(4, announcement.getTargetAudience());
            stmt.setBoolean(5, announcement.isActive());
            stmt.setInt(6, announcement.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteAnnouncement(int id) {
        String sql = "DELETE FROM announcements WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}