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
            seedUser(c, "superadmin", "superadmin", "ADMIN", "Super Administrator", "superadmin@school.com", "SUP001", "N/A");
            seedUser(c, "user", "user", "USER", "Normal User", "user@school.com", "USR001", "Level 1");
            seedUser(c, "teacher1", "teacher", "TEACHER", "John Doe", "john.doe@school.com", "TCH001", "Senior");
            seedUser(c, "teacher2", "teacher", "TEACHER", "Jane Smith", "jane.smith@school.com", "TCH002", "Senior");

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

        // Departments
        st.execute("CREATE TABLE IF NOT EXISTS departments (id SERIAL PRIMARY KEY, department_code VARCHAR(10) UNIQUE NOT NULL, department_name VARCHAR(150) NOT NULL, description TEXT, head_of_department VARCHAR(150))");

        // Classes
        st.execute("CREATE TABLE IF NOT EXISTS classes (id SERIAL PRIMARY KEY, name VARCHAR(100) NOT NULL)");

        // Students
        st.execute("CREATE TABLE IF NOT EXISTS students (id SERIAL PRIMARY KEY, first_name VARCHAR(100) NOT NULL, last_name VARCHAR(100) NOT NULL, email VARCHAR(255) UNIQUE, class_id INTEGER REFERENCES classes(id) ON DELETE SET NULL)");

        // Teachers
        st.execute("CREATE TABLE IF NOT EXISTS teachers (id SERIAL PRIMARY KEY, first_name VARCHAR(100) NOT NULL, last_name VARCHAR(100) NOT NULL, email VARCHAR(255) UNIQUE)");

        // Courses
        st.execute("CREATE TABLE IF NOT EXISTS courses (id SERIAL PRIMARY KEY, course_code VARCHAR(20) UNIQUE NOT NULL, course_name VARCHAR(150) NOT NULL, description TEXT, credits INTEGER NOT NULL CHECK (credits > 0), department_id INTEGER REFERENCES departments(id) ON DELETE SET NULL)");

        // Subjects
        st.execute("CREATE TABLE IF NOT EXISTS subjects (id SERIAL PRIMARY KEY, name VARCHAR(150) NOT NULL, teacher_id INTEGER REFERENCES teachers(id) ON DELETE SET NULL)");

        // Enrollments
        st.execute("CREATE TABLE IF NOT EXISTS enrollments (id SERIAL PRIMARY KEY, student_id INTEGER NOT NULL REFERENCES students(id) ON DELETE CASCADE, course_id INTEGER NOT NULL REFERENCES courses(id) ON DELETE CASCADE, enrollment_date DATE DEFAULT CURRENT_DATE, status VARCHAR(20) DEFAULT 'ACTIVE', grade DECIMAL(5,2) CHECK (grade BETWEEN 0 AND 100))");

        // Timetables
        st.execute("CREATE TABLE IF NOT EXISTS timetables (id SERIAL PRIMARY KEY, course_id INTEGER REFERENCES courses(id) ON DELETE CASCADE, day_of_week VARCHAR(15) NOT NULL, start_time TIME NOT NULL, end_time TIME NOT NULL, room VARCHAR(50) NOT NULL, instructor VARCHAR(150))");

        // Attendance
        st.execute("CREATE TABLE IF NOT EXISTS attendance (id SERIAL PRIMARY KEY, student_id INTEGER REFERENCES students(id) ON DELETE CASCADE, attendance_date DATE NOT NULL, present BOOLEAN NOT NULL DEFAULT TRUE, remarks TEXT)");

        // Fees
        st.execute("CREATE TABLE IF NOT EXISTS fees (id SERIAL PRIMARY KEY, student_id INTEGER REFERENCES students(id) ON DELETE CASCADE, fee_type VARCHAR(100) NOT NULL, amount DECIMAL(10,2) NOT NULL CHECK (amount > 0), paid_amount DECIMAL(10,2) DEFAULT 0 CHECK (paid_amount >= 0), due_date DATE NOT NULL, payment_date DATE, status VARCHAR(20) DEFAULT 'PENDING')");

        // Announcements
        st.execute("CREATE TABLE IF NOT EXISTS announcements (id SERIAL PRIMARY KEY, title VARCHAR(255) NOT NULL, content TEXT NOT NULL, author VARCHAR(150) NOT NULL, publish_date DATE DEFAULT CURRENT_DATE, expiry_date DATE, target_audience VARCHAR(20) DEFAULT 'ALL', is_active BOOLEAN DEFAULT TRUE)");

        // Indexes
        st.execute("CREATE INDEX IF NOT EXISTS idx_students_class ON students(class_id)");
        st.execute("CREATE INDEX IF NOT EXISTS idx_subjects_teacher ON subjects(teacher_id)");
        st.execute("CREATE INDEX IF NOT EXISTS idx_enrollments_student ON enrollments(student_id)");
        st.execute("CREATE INDEX IF NOT EXISTS idx_enrollments_course ON enrollments(course_id)");
        st.execute("CREATE INDEX IF NOT EXISTS idx_courses_department ON courses(department_id)");
        st.execute("CREATE INDEX IF NOT EXISTS idx_fees_student ON fees(student_id)");
        st.execute("CREATE INDEX IF NOT EXISTS idx_announcements_active ON announcements(is_active)");
        st.execute("CREATE INDEX IF NOT EXISTS idx_attendance_student_date ON attendance(student_id, attendance_date)");
        st.execute("CREATE UNIQUE INDEX IF NOT EXISTS uq_enrollment ON enrollments(student_id, course_id)");
        st.execute("CREATE UNIQUE INDEX IF NOT EXISTS uq_attendance ON attendance(student_id, attendance_date)");

    }

    private static void seedSampleData(Connection c) throws SQLException {
        // Seed departments
        try (PreparedStatement ps = c.prepareStatement("INSERT INTO departments (department_code, department_name, description) VALUES (?, ?, ?) ON CONFLICT DO NOTHING")) {
            Object[][] departments = {
                {"MATH", "Mathematics", "Mathematics Department"},
                {"ENG", "English", "English Department"},
                {"SCI", "Science", "Science Department"},
                {"HIST", "History", "History Department"}
            };
            for (Object[] dept : departments) {
                ps.setString(1, (String)dept[0]);
                ps.setString(2, (String)dept[1]);
                ps.setString(3, (String)dept[2]);
                ps.executeUpdate();
            }
        }

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

        // Seed courses
        try (PreparedStatement ps = c.prepareStatement("INSERT INTO courses (course_code, course_name, description, credits, department_id) VALUES (?, ?, ?, ?, ?) ON CONFLICT DO NOTHING")) {
            Object[][] courses = {
                {"MATH101", "Mathematics", "Basic Mathematics", 3, 1},
                {"ENG101", "English", "English Literature", 2, 2},
                {"SCI101", "Science", "General Science", 3, 3},
                {"HIS101", "History", "World History", 2, 4}
            };
            for (Object[] course : courses) {
                ps.setString(1, (String)course[0]);
                ps.setString(2, (String)course[1]);
                ps.setString(3, (String)course[2]);
                ps.setInt(4, (Integer)course[3]);
                ps.setInt(5, (Integer)course[4]);
                ps.executeUpdate();
            }
        }

        // Seed enrollments
        try (PreparedStatement ps = c.prepareStatement("INSERT INTO enrollments (student_id, course_id, enrollment_date, status) VALUES (?, ?, CURRENT_DATE, 'ACTIVE') ON CONFLICT DO NOTHING")) {
            // Enroll students in courses
            int[][] enrollments = {
                {1, 1}, {1, 2}, {2, 1}, {2, 3}, {3, 2}, {3, 4}, {4, 3}, {4, 4}
            };
            for (int[] en : enrollments) {
                ps.setInt(1, en[0]);
                ps.setInt(2, en[1]);
                ps.executeUpdate();
            }
        }

        // Seed timetables
        try (PreparedStatement ps = c.prepareStatement("INSERT INTO timetables (course_id, day_of_week, start_time, end_time, room, instructor) VALUES (?, ?, ?, ?, ?, ?) ON CONFLICT DO NOTHING")) {
            Object[][] timetables = {
                {1, "Monday", "09:00", "10:30", "Room 101", "John Doe"},
                {2, "Tuesday", "10:00", "11:30", "Room 102", "Jane Smith"},
                {3, "Wednesday", "14:00", "15:30", "Room 103", "Bob Johnson"},
                {4, "Thursday", "15:00", "16:30", "Room 104", "John Doe"}
            };
            for (Object[] tt : timetables) {
                ps.setInt(1, (Integer)tt[0]);
                ps.setString(2, (String)tt[1]);
                ps.setString(3, (String)tt[2]);
                ps.setString(4, (String)tt[3]);
                ps.setString(5, (String)tt[4]);
                ps.setString(6, (String)tt[5]);
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
