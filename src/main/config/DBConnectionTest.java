package config;

import java.sql.Connection;

/**
 * Simple test class to verify database connection configuration
 */
public class DBConnectionTest {
    public static void main(String[] args) {
        try {
            System.out.println("Testing database connection...");
            System.out.println("Database URL: " + DBConnection.getDatabaseUrl());
            System.out.println("Database User: " + DBConnection.getDatabaseUser());

            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                System.out.println("✅ Database connection successful!");
                conn.close();
            }
        } catch (Exception e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}