package dao;

import config.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Attendance;

public class AttendanceDAO {

   public void markAttendance(Attendance attendance) {
    String sql = "INSERT INTO attendance(student_id, attendance_date, present, remarks) VALUES (?, ?, ?, ?) ON CONFLICT (student_id, attendance_date) DO UPDATE SET present = EXCLUDED.present, remarks = EXCLUDED.remarks";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, attendance.getStudentId());
        ps.setDate(2, Date.valueOf(attendance.getDate()));
        ps.setBoolean(3, "Present".equalsIgnoreCase(attendance.getStatus()));
        ps.setString(4, attendance.getRemarks());

        ps.executeUpdate();

    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    public List<Attendance> getAttendanceByStudent(int studentId) {
        List<Attendance> list = new ArrayList<>();
        String sql = "SELECT * FROM attendance WHERE student_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Attendance a = new Attendance();
                a.setId(rs.getInt("id"));
                a.setStudentId(rs.getInt("student_id"));
                a.setDate(rs.getDate("attendance_date").toLocalDate());
                a.setStatus(rs.getBoolean("present") ? "Present" : "Absent");
                a.setRemarks(rs.getString("remarks"));
                list.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Attendance> getAttendanceByCourse(int courseId) {
        List<Attendance> list = new ArrayList<>();
        String sql = "SELECT a.*, s.first_name || ' ' || s.last_name as student_name FROM attendance a " +
                     "JOIN students s ON a.student_id = s.id " +
                     "JOIN enrollments e ON s.id = e.student_id " +
                     "WHERE e.course_id = ? ORDER BY a.attendance_date, s.first_name";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Attendance a = new Attendance();
                a.setId(rs.getInt("id"));
                a.setStudentId(rs.getInt("student_id"));
                a.setStudentName(rs.getString("student_name"));
                a.setDate(rs.getDate("attendance_date").toLocalDate());
                a.setStatus(rs.getBoolean("present") ? "Present" : "Absent");
                a.setRemarks(rs.getString("remarks"));
                list.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
