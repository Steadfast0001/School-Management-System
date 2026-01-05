package dao;

import config.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Report;

public class ReportDAO {

    public List<Report> getAllReports() {
        List<Report> reports = new ArrayList<>();
        String sql = "SELECT * FROM reports ORDER BY generated_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Report report = new Report();
                report.setId(rs.getInt("id"));
                report.setReportType(rs.getString("report_type"));
                report.setTitle(rs.getString("title"));
                report.setDescription(rs.getString("description"));
                report.setGeneratedDate(rs.getDate("generated_date").toLocalDate());
                report.setGeneratedBy(rs.getString("generated_by"));
                report.setData(rs.getString("data"));
                reports.add(report);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }

    public boolean addReport(Report report) {
        String sql = "INSERT INTO reports (report_type, title, description, generated_date, generated_by, data) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, report.getReportType());
            stmt.setString(2, report.getTitle());
            stmt.setString(3, report.getDescription());
            stmt.setDate(4, Date.valueOf(report.getGeneratedDate()));
            stmt.setString(5, report.getGeneratedBy());
            stmt.setString(6, report.getData());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteReport(int id) {
        String sql = "DELETE FROM reports WHERE id = ?";

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