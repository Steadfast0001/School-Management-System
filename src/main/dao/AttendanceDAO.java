package dao;

import config.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Attendance;

public class AttendanceDAO {

   public void markAttendance(Attendance attendance) {
    String sql = "INSERT INTO attendance(student_id, attendance_date, status) VALUES (?, ?, ?)";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, attendance.getStudentId());
        ps.setDate(2, Date.valueOf(attendance.getDate()));
        ps.setString(3, attendance.getStatus());

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
                a.setDate(rs.getDate("date").toLocalDate());
                a.setStatus(rs.getString("status"));
                list.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
