package dao;

import config.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Department;

public class DepartmentDAO {

    public List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT * FROM departments";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Department dept = new Department();
                dept.setId(rs.getInt("id"));
                dept.setDepartmentCode(rs.getString("department_code"));
                dept.setDepartmentName(rs.getString("department_name"));
                dept.setDescription(rs.getString("description"));
                dept.setHeadOfDepartment(rs.getString("head_of_department"));
                departments.add(dept);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departments;
    }

    public Department getDepartmentById(int id) {
        String sql = "SELECT * FROM departments WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Department dept = new Department();
                dept.setId(rs.getInt("id"));
                dept.setDepartmentCode(rs.getString("department_code"));
                dept.setDepartmentName(rs.getString("department_name"));
                dept.setDescription(rs.getString("description"));
                dept.setHeadOfDepartment(rs.getString("head_of_department"));
                return dept;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addDepartment(Department dept) {
        String sql = "INSERT INTO departments (department_code, department_name, description, head_of_department) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dept.getDepartmentCode());
            stmt.setString(2, dept.getDepartmentName());
            stmt.setString(3, dept.getDescription());
            stmt.setString(4, dept.getHeadOfDepartment());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateDepartment(Department dept) {
        String sql = "UPDATE departments SET department_code = ?, department_name = ?, description = ?, head_of_department = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dept.getDepartmentCode());
            stmt.setString(2, dept.getDepartmentName());
            stmt.setString(3, dept.getDescription());
            stmt.setString(4, dept.getHeadOfDepartment());
            stmt.setInt(5, dept.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteDepartment(int id) {
        String sql = "DELETE FROM departments WHERE id = ?";

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