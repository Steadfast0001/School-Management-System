package app;

import config.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import utils.PasswordUtil;

public class DBSeeder {
    public static void seedAdmin() {
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement()) {

            // Create all tables
            createTables(st);

            // Alter users table to add new columns if not exist
            st.execute("ALTER TABLE users ADD COLUMN IF NOT EXISTS name VARCHAR(255)");
            st.execute("ALTER TABLE users ADD COLUMN IF NOT EXISTS email VARCHAR(255)");
            st.execute("ALTER TABLE users ADD COLUMN IF NOT EXISTS matricule VARCHAR(100)");
            st.execute("ALTER TABLE users ADD COLUMN IF NOT EXISTS level VARCHAR(50)");

            // Seed users
            seedUser(c, "admin", "admin", "ADMIN", "Administrator", "admin@school.com", "ADM001", "N/A");
            seedUser(c, "user", "user", "USER", "Normal User", "user@school.com", "USR001", "Level 1");

            // Seed sample data
            seedSampleData(c);

        } catch (SQLException e) {
            System.err.println("Failed to seed DB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createTables(Statement st) throws SQLException {
        // Users table (already handled)
        st.execute("CREATE TABLE IF NOT EXISTS users (id SERIAL PRIMARY KEY, username VARCHAR(100) UNIQUE NOT NULL, password VARCHAR(255) NOT NULL, role VARCHAR(50) DEFAULT 'USER')");

        // Classes
        st.execute("CREATE TABLE IF NOT EXISTS classes (id SERIAL PRIMARY KEY, name VARCHAR(100) NOT NULL)");

        // Students
        st.execute("CREATE TABLE IF NOT EXISTS students (id SERIAL PRIMARY KEY, first_name VARCHAR(100) NOT NULL, last_name VARCHAR(100) NOT NULL, email VARCHAR(255) UNIQUE, class_id INTEGER REFERENCES classes(id) ON DELETE SET NULL)");

        // Teachers
        st.execute("CREATE TABLE IF NOT EXISTS teachers (id SERIAL PRIMARY KEY, first_name VARCHAR(100) NOT NULL, last_name VARCHAR(100) NOT NULL, email VARCHAR(255) UNIQUE)");

        // Subjects
        st.execute("CREATE TABLE IF NOT EXISTS subjects (id SERIAL PRIMARY KEY, name VARCHAR(150) NOT NULL, teacher_id INTEGER REFERENCES teachers(id) ON DELETE SET NULL)");

        // Attendance
        // Attendance
st.execute(
    "CREATE TABLE IF NOT EXISTS attendance (" +
    "id SERIAL PRIMARY KEY, " +
    "student_id INTEGER REFERENCES students(id) ON DELETE CASCADE, " +
    "attendance_date DATE NOT NULL, " +
    "present BOOLEAN NOT NULL DEFAULT TRUE, " +
    "remarks TEXT)"
);


        // Indexes
        st.execute("CREATE INDEX IF NOT EXISTS idx_students_class ON students(class_id)");
        st.execute("CREATE INDEX IF NOT EXISTS idx_subjects_teacher ON subjects(teacher_id)");
        st.execute(
    "CREATE INDEX IF NOT EXISTS idx_attendance_student_date " +
    "ON attendance(student_id, attendance_date)"
);

    }

    private static void seedSampleData(Connection c) throws SQLException {
        // Seed classes
        try (PreparedStatement ps = c.prepareStatement("INSERT INTO classes (name) VALUES (?) ON CONFLICT DO NOTHING")) {
            String[] classes = {"Class 1A", "Class 1B", "Class 2A", "Class 2B"};
            for (String cls : classes) {
                ps.setString(1, cls);
                ps.executeUpdate();
            }
        }

        // Seed teachers
        try (PreparedStatement ps = c.prepareStatement("INSERT INTO teachers (first_name, last_name, email) VALUES (?, ?, ?) ON CONFLICT DO NOTHING")) {
            Object[][] teachers = {
                {"John", "Doe", "john.doe@school.com"},
                {"Jane", "Smith", "jane.smith@school.com"},
                {"Bob", "Johnson", "bob.johnson@school.com"}
            };
            for (Object[] t : teachers) {
                ps.setString(1, (String)t[0]);
                ps.setString(2, (String)t[1]);
                ps.setString(3, (String)t[2]);
                ps.executeUpdate();
            }
        }

        // Seed subjects
        try (PreparedStatement ps = c.prepareStatement("INSERT INTO subjects (name, teacher_id) VALUES (?, ?) ON CONFLICT DO NOTHING")) {
            // Assuming teacher ids 1,2,3
            Object[][] subjects = {
                {"Mathematics", 1},
                {"English", 2},
                {"Science", 3},
                {"History", 1}
            };
            for (Object[] s : subjects) {
                ps.setString(1, (String)s[0]);
                ps.setInt(2, (Integer)s[1]);
                ps.executeUpdate();
            }
        }

        // Seed students
        try (PreparedStatement ps = c.prepareStatement("INSERT INTO students (first_name, last_name, email, class_id) VALUES (?, ?, ?, ?) ON CONFLICT DO NOTHING")) {
            Object[][] students = {
                {"Alice", "Brown", "alice.brown@school.com", 1},
                {"Charlie", "Davis", "charlie.davis@school.com", 1},
                {"Eva", "Wilson", "eva.wilson@school.com", 2},
                {"Frank", "Miller", "frank.miller@school.com", 2}
            };
            for (Object[] s : students) {
                ps.setString(1, (String)s[0]);
                ps.setString(2, (String)s[1]);
                ps.setString(3, (String)s[2]);
                ps.setInt(4, (Integer)s[3]);
                ps.executeUpdate();
            }
        }

        // Seed attendance (sample)
        try (PreparedStatement ps = c.prepareStatement("INSERT INTO attendance (student_id, attendance_date, present) VALUES (?, CURRENT_DATE, ?) ON CONFLICT DO NOTHING")) {
            // Assuming student ids 1-4
            for (int i = 1; i <= 4; i++) {
                ps.setInt(1, i);
                ps.setBoolean(2, i % 2 == 0); // Alternate present/absent
                ps.executeUpdate();
            }
        }
    }

    private static void seedUser(Connection c, String username, String password, String role, String name, String email, String matricule, String level) throws SQLException {
        String check = "SELECT id FROM users WHERE username = ?";
        String insert = "INSERT INTO users (username, password, role, name, email, matricule, level) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String update = "UPDATE users SET password=?, name=?, email=?, matricule=?, level=? WHERE username=?";

        try (PreparedStatement ps = c.prepareStatement(check)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                String hashed = PasswordUtil.hash(password);
                System.out.println("Hashed password for " + username + ": " + hashed);
                if (!rs.next()) {
                    // Insert
                    try (PreparedStatement ins = c.prepareStatement(insert)) {
                        ins.setString(1, username);
                        ins.setString(2, hashed);
                        ins.setString(3, role);
                        ins.setString(4, name);
                        ins.setString(5, email);
                        ins.setString(6, matricule);
                        ins.setString(7, level);
                        ins.executeUpdate();
                        System.out.println("Seeded " + username + " (" + role + ")");
                    }
                } else {
                    // Update
                    try (PreparedStatement upd = c.prepareStatement(update)) {
                        upd.setString(1, hashed);
                        upd.setString(2, name);
                        upd.setString(3, email);
                        upd.setString(4, matricule);
                        upd.setString(5, level);
                        upd.setString(6, username);
                        upd.executeUpdate();
                        System.out.println("Updated " + username + " password");
                    }
                }
            }
        }
    }
}
