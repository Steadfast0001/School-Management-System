package dao;

import config.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Student;

public class StudentDAO {

    public boolean addStudent(Student student) {

        String sql = "INSERT INTO students(name, matricule, class_name, dob) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, student.getName());
            ps.setString(2, student.getMatricule());
            ps.setString(3, student.getClassName());
            ps.setDate(4, Date.valueOf(student.getDateOfBirth()));

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Student> getAllStudents() {

        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Student s = new Student();
                s.setId(rs.getInt("id"));
                s.setName(rs.getString("name"));
                s.setMatricule(rs.getString("matricule"));
                s.setClassName(rs.getString("class_name"));
                s.setDateOfBirth(rs.getDate("dob").toLocalDate());
                list.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateStudent(Student student) {

        String sql = "UPDATE students SET name=?, matricule=?, class_name=?, dob=? WHERE id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, student.getName());
            ps.setString(2, student.getMatricule());
            ps.setString(3, student.getClassName());
            ps.setDate(4, Date.valueOf(student.getDateOfBirth()));
            ps.setInt(5, student.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteStudent(int id) {

        String sql = "DELETE FROM students WHERE id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ BUSINESS LOGIC SUPPORT METHOD
    public boolean existsByMatriculeAndNotId(String matricule, int id) {

    String sql = "SELECT 1 FROM students WHERE matricule = ? AND id <> ?";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, matricule);
        ps.setInt(2, id);

        ResultSet rs = ps.executeQuery();
        return rs.next();

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

}
