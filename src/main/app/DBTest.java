package app;

import config.DBConnection;
import java.sql.Connection;

public class DBTest {
    public static void main(String[] args) {
        try {
            Connection c = DBConnection.getConnection();
            System.out.println("Database connection successful!");
            c.close();
        } catch (Exception e) {
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}