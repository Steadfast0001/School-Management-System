package dao;

import config.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Fee;

public class FeeDAO {

    public List<Fee> getAllFees() {
        List<Fee> fees = new ArrayList<>();
        String sql = "SELECT f.*, s.name as student_name FROM fees f JOIN students s ON f.student_id = s.id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Fee fee = new Fee();
                fee.setId(rs.getInt("id"));
                fee.setStudentId(rs.getInt("student_id"));
                fee.setStudentName(rs.getString("student_name"));
                fee.setFeeType(rs.getString("fee_type"));
                fee.setAmount(rs.getDouble("amount"));
                fee.setPaidAmount(rs.getDouble("paid_amount"));
                fee.setDueDate(rs.getDate("due_date").toLocalDate());
                if (rs.getDate("payment_date") != null) {
                    fee.setPaymentDate(rs.getDate("payment_date").toLocalDate());
                }
                fee.setStatus(rs.getString("status"));
                fees.add(fee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fees;
    }

    public List<Fee> getFeesByStudent(int studentId) {
        List<Fee> fees = new ArrayList<>();
        String sql = "SELECT * FROM fees WHERE student_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Fee fee = new Fee();
                fee.setId(rs.getInt("id"));
                fee.setStudentId(rs.getInt("student_id"));
                fee.setFeeType(rs.getString("fee_type"));
                fee.setAmount(rs.getDouble("amount"));
                fee.setPaidAmount(rs.getDouble("paid_amount"));
                fee.setDueDate(rs.getDate("due_date").toLocalDate());
                if (rs.getDate("payment_date") != null) {
                    fee.setPaymentDate(rs.getDate("payment_date").toLocalDate());
                }
                fee.setStatus(rs.getString("status"));
                fees.add(fee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fees;
    }

    public boolean addFee(Fee fee) {
        String sql = "INSERT INTO fees (student_id, fee_type, amount, paid_amount, due_date, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, fee.getStudentId());
            stmt.setString(2, fee.getFeeType());
            stmt.setDouble(3, fee.getAmount());
            stmt.setDouble(4, fee.getPaidAmount());
            stmt.setDate(5, Date.valueOf(fee.getDueDate()));
            stmt.setString(6, fee.getStatus());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateFee(Fee fee) {
        String sql = "UPDATE fees SET paid_amount = ?, payment_date = ?, status = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, fee.getPaidAmount());
            if (fee.getPaymentDate() != null) {
                stmt.setDate(2, Date.valueOf(fee.getPaymentDate()));
            } else {
                stmt.setNull(2, Types.DATE);
            }
            stmt.setString(3, fee.getStatus());
            stmt.setInt(4, fee.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteFee(int id) {
        String sql = "DELETE FROM fees WHERE id = ?";

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