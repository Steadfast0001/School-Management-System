package dao;

import config.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Timetable;

public class TimetableDAO {

    public List<Timetable> getAllTimetables() {
        List<Timetable> timetables = new ArrayList<>();
        String sql = "SELECT t.*, c.course_name FROM timetables t JOIN courses c ON t.course_id = c.id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Timetable timetable = new Timetable();
                timetable.setId(rs.getInt("id"));
                timetable.setCourseId(rs.getInt("course_id"));
                timetable.setCourseName(rs.getString("course_name"));
                timetable.setDayOfWeek(rs.getString("day_of_week"));
                timetable.setStartTime(rs.getString("start_time"));
                timetable.setEndTime(rs.getString("end_time"));
                timetable.setRoom(rs.getString("room"));
                timetable.setInstructor(rs.getString("instructor"));
                timetables.add(timetable);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return timetables;
    }

    public boolean addTimetable(Timetable timetable) {
        String sql = "INSERT INTO timetables (course_id, day_of_week, start_time, end_time, room, instructor) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, timetable.getCourseId());
            stmt.setString(2, timetable.getDayOfWeek());
            stmt.setString(3, timetable.getStartTime());
            stmt.setString(4, timetable.getEndTime());
            stmt.setString(5, timetable.getRoom());
            stmt.setString(6, timetable.getInstructor());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateTimetable(Timetable timetable) {
        String sql = "UPDATE timetables SET day_of_week = ?, start_time = ?, end_time = ?, room = ?, instructor = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, timetable.getDayOfWeek());
            stmt.setString(2, timetable.getStartTime());
            stmt.setString(3, timetable.getEndTime());
            stmt.setString(4, timetable.getRoom());
            stmt.setString(5, timetable.getInstructor());
            stmt.setInt(6, timetable.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteTimetable(int id) {
        String sql = "DELETE FROM timetables WHERE id = ?";

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