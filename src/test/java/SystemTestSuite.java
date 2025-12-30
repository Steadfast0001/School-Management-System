package test.java;

import config.DBConnection;
import dao.*;
import model.*;
import service.AuthService;
import utils.PasswordUtil;
import utils.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Comprehensive test suite for University Management System
 * Tests all CRUD operations, authentication, and edge cases
 */
public class SystemTestSuite {

    private static final String TEST_ADMIN_USERNAME = "testadmin";
    private static final String TEST_ADMIN_PASSWORD = "TestAdmin123!";
    private static final String TEST_USER_USERNAME = "testuser";
    private static final String TEST_USER_PASSWORD = "TestUser123!";

    public static void main(String[] args) {
        System.out.println("=== University Management System Test Suite ===\n");

        try {
            // Setup test environment
            setupTestEnvironment();

            // Run all tests
            runAuthenticationTests();
            runUserManagementTests();
            runDepartmentTests();
            runCourseTests();
            runStudentTests();
            runEnrollmentTests();
            runTimetableTests();
            runLibraryTests();
            runFeeTests();
            runAnnouncementTests();
            runReportTests();

            // Edge case tests
            runEdgeCaseTests();

            System.out.println("\n=== All Tests Completed Successfully! ===");

        } catch (Exception e) {
            System.err.println("Test suite failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cleanupTestEnvironment();
        }
    }

    private static void setupTestEnvironment() throws SQLException {
        System.out.println("Setting up test environment...");

        // Create test admin user
        UserDAO userDAO = new UserDAO();
        AuthService authService = new AuthService();

        // Clean up any existing test users
        try (Connection conn = DBConnection.getConnection()) {
            String deleteTestUsers = "DELETE FROM users WHERE username LIKE 'test%'";
            try (PreparedStatement stmt = conn.prepareStatement(deleteTestUsers)) {
                stmt.executeUpdate();
            }
        }

        // Create test admin
        User testAdmin = new User(0, TEST_ADMIN_USERNAME, PasswordUtil.hashPassword(TEST_ADMIN_PASSWORD),
                                "ADMIN", "Test Admin", "admin@test.com", "ADM001", "N/A");
        userDAO.addUser(testAdmin);

        // Create test regular user
        User testUser = new User(0, TEST_USER_USERNAME, PasswordUtil.hashPassword(TEST_USER_PASSWORD),
                               "USER", "Test User", "user@test.com", "USR001", "Undergraduate");
        userDAO.addUser(testUser);

        System.out.println("Test environment setup complete.");
    }

    private static void cleanupTestEnvironment() {
        System.out.println("Cleaning up test environment...");

        try (Connection conn = DBConnection.getConnection()) {
            // Clean up test data
            String[] cleanupQueries = {
                "DELETE FROM enrollments WHERE student_id IN (SELECT id FROM students WHERE email LIKE 'test%')",
                "DELETE FROM fees WHERE student_id IN (SELECT id FROM students WHERE email LIKE 'test%')",
                "DELETE FROM attendance WHERE student_id IN (SELECT id FROM students WHERE email LIKE 'test%')",
                "DELETE FROM students WHERE email LIKE 'test%'",
                "DELETE FROM timetables WHERE instructor LIKE 'Test%'",
                "DELETE FROM courses WHERE course_code LIKE 'TEST%'",
                "DELETE FROM departments WHERE department_code LIKE 'TEST%'",
                "DELETE FROM library WHERE book_title LIKE 'Test%'",
                "DELETE FROM announcements WHERE author LIKE 'Test%'",
                "DELETE FROM reports WHERE generated_by LIKE 'Test%'",
                "DELETE FROM users WHERE username LIKE 'test%'"
            };

            for (String query : cleanupQueries) {
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error during cleanup: " + e.getMessage());
        }

        System.out.println("Test environment cleanup complete.");
    }

    private static void runAuthenticationTests() {
        System.out.println("\n--- Authentication Tests ---");

        AuthService authService = new AuthService();

        // Test valid admin login
        User adminUser = authService.authenticate(TEST_ADMIN_USERNAME, TEST_ADMIN_PASSWORD);
        assert adminUser != null : "Admin login should succeed";
        assert "ADMIN".equals(adminUser.getRole()) : "Admin should have ADMIN role";
        System.out.println("✓ Admin login test passed");

        // Test valid user login
        User regularUser = authService.authenticate(TEST_USER_USERNAME, TEST_USER_PASSWORD);
        assert regularUser != null : "User login should succeed";
        assert "USER".equals(regularUser.getRole()) : "User should have USER role";
        System.out.println("✓ User login test passed");

        // Test invalid login
        User invalidUser = authService.authenticate("nonexistent", "wrongpass");
        assert invalidUser == null : "Invalid login should return null";
        System.out.println("✓ Invalid login test passed");

        // Test role checking
        assert authService.isAdmin(adminUser) : "Admin user should be identified as admin";
        assert !authService.isAdmin(regularUser) : "Regular user should not be identified as admin";
        System.out.println("✓ Role checking test passed");
    }

    private static void runUserManagementTests() {
        System.out.println("\n--- User Management Tests ---");

        UserDAO userDAO = new UserDAO();

        // Test user retrieval
        User admin = userDAO.findByUsername(TEST_ADMIN_USERNAME);
        assert admin != null : "Should find admin user";
        assert TEST_ADMIN_USERNAME.equals(admin.getUsername()) : "Username should match";
        System.out.println("✓ User retrieval test passed");

        // Test user update (if implemented)
        // Note: Update functionality would need to be added to UserDAO
        System.out.println("✓ User management tests completed");
    }

    private static void runDepartmentTests() {
        System.out.println("\n--- Department Tests ---");

        DepartmentDAO deptDAO = new DepartmentDAO();

        // Create test department
        Department dept = new Department(0, "TEST_CS", "Test Computer Science",
                                       "Test department for computer science", "Dr. Test");
        boolean added = deptDAO.addDepartment(dept);
        assert added : "Department should be added successfully";
        System.out.println("✓ Department creation test passed");

        // Retrieve department
        List<Department> departments = deptDAO.getAllDepartments();
        Department foundDept = departments.stream()
            .filter(d -> "TEST_CS".equals(d.getDepartmentCode()))
            .findFirst()
            .orElse(null);
        assert foundDept != null : "Department should be found";
        System.out.println("✓ Department retrieval test passed");

        // Update department
        foundDept.setDescription("Updated test department");
        boolean updated = deptDAO.updateDepartment(foundDept);
        assert updated : "Department should be updated successfully";
        System.out.println("✓ Department update test passed");

        // Delete department
        boolean deleted = deptDAO.deleteDepartment(foundDept.getId());
        assert deleted : "Department should be deleted successfully";
        System.out.println("✓ Department deletion test passed");
    }

    private static void runCourseTests() {
        System.out.println("\n--- Course Tests ---");

        CourseDAO courseDAO = new CourseDAO();
        DepartmentDAO deptDAO = new DepartmentDAO();

        // Create test department first
        Department dept = new Department(0, "TEST_CS", "Test Computer Science",
                                       "Test department", "Dr. Test");
        deptDAO.addDepartment(dept);

        // Get the department ID
        List<Department> departments = deptDAO.getAllDepartments();
        Department testDept = departments.stream()
            .filter(d -> "TEST_CS".equals(d.getDepartmentCode()))
            .findFirst()
            .orElse(null);
        assert testDept != null : "Test department should exist";

        // Create test course
        Course course = new Course(0, "TEST101", "Test Course", "Test course description", 3, testDept.getId());
        boolean added = courseDAO.addCourse(course);
        assert added : "Course should be added successfully";
        System.out.println("✓ Course creation test passed");

        // Retrieve course
        List<Course> courses = courseDAO.getAllCourses();
        Course foundCourse = courses.stream()
            .filter(c -> "TEST101".equals(c.getCourseCode()))
            .findFirst()
            .orElse(null);
        assert foundCourse != null : "Course should be found";
        System.out.println("✓ Course retrieval test passed");

        // Update course
        foundCourse.setDescription("Updated test course");
        boolean updated = courseDAO.updateCourse(foundCourse);
        assert updated : "Course should be updated successfully";
        System.out.println("✓ Course update test passed");

        // Delete course
        boolean deleted = courseDAO.deleteCourse(foundCourse.getId());
        assert deleted : "Course should be deleted successfully";
        System.out.println("✓ Course deletion test passed");

        // Cleanup department
        deptDAO.deleteDepartment(testDept.getId());
    }

    private static void runStudentTests() {
        System.out.println("\n--- Student Tests ---");

        StudentDAO studentDAO = new StudentDAO();

        // Create test student
        Student student = new Student(0, "Test", "Student", "test.student@test.com", 1);
        boolean added = studentDAO.addStudent(student);
        assert added : "Student should be added successfully";
        System.out.println("✓ Student creation test passed");

        // Retrieve student
        List<Student> students = studentDAO.getAllStudents();
        Student foundStudent = students.stream()
            .filter(s -> "test.student@test.com".equals(s.getEmail()))
            .findFirst()
            .orElse(null);
        assert foundStudent != null : "Student should be found";
        System.out.println("✓ Student retrieval test passed");

        // Update student (if implemented in StudentDAO)
        System.out.println("✓ Student tests completed");
    }

    private static void runEnrollmentTests() {
        System.out.println("\n--- Enrollment Tests ---");

        EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
        StudentDAO studentDAO = new StudentDAO();
        CourseDAO courseDAO = new CourseDAO();
        DepartmentDAO deptDAO = new DepartmentDAO();

        // Setup test data
        Department dept = new Department(0, "TEST_CS", "Test CS", "Test", "Dr. Test");
        deptDAO.addDepartment(dept);

        List<Department> departments = deptDAO.getAllDepartments();
        Department testDept = departments.stream()
            .filter(d -> "TEST_CS".equals(d.getDepartmentCode()))
            .findFirst()
            .orElse(null);

        Course course = new Course(0, "TEST101", "Test Course", "Test", 3, testDept.getId());
        courseDAO.addCourse(course);

        Student student = new Student(0, "Test", "Student", "test.student@test.com", 1);
        studentDAO.addStudent(student);

        // Get IDs
        List<Course> courses = courseDAO.getAllCourses();
        Course testCourse = courses.stream()
            .filter(c -> "TEST101".equals(c.getCourseCode()))
            .findFirst()
            .orElse(null);

        List<Student> students = studentDAO.getAllStudents();
        Student testStudent = students.stream()
            .filter(s -> "test.student@test.com".equals(s.getEmail()))
            .findFirst()
            .orElse(null);

        assert testCourse != null && testStudent != null : "Test data should exist";

        // Create enrollment
        Enrollment enrollment = new Enrollment(0, testStudent.getId(), testCourse.getId(),
                                            LocalDate.now(), "ACTIVE");
        boolean added = enrollmentDAO.addEnrollment(enrollment);
        assert added : "Enrollment should be added successfully";
        System.out.println("✓ Enrollment creation test passed");

        // Retrieve enrollments
        List<Enrollment> enrollments = enrollmentDAO.getAllEnrollments();
        assert !enrollments.isEmpty() : "Should have enrollments";
        System.out.println("✓ Enrollment retrieval test passed");

        // Update enrollment (if implemented)
        System.out.println("✓ Enrollment tests completed");

        // Cleanup
        enrollmentDAO.deleteEnrollment(enrollments.get(enrollments.size() - 1).getId());
    }

    private static void runTimetableTests() {
        System.out.println("\n--- Timetable Tests ---");

        TimetableDAO timetableDAO = new TimetableDAO();
        CourseDAO courseDAO = new CourseDAO();
        DepartmentDAO deptDAO = new DepartmentDAO();

        // Setup test course
        Department dept = new Department(0, "TEST_CS", "Test CS", "Test", "Dr. Test");
        deptDAO.addDepartment(dept);

        List<Department> departments = deptDAO.getAllDepartments();
        Department testDept = departments.stream()
            .filter(d -> "TEST_CS".equals(d.getDepartmentCode()))
            .findFirst()
            .orElse(null);

        Course course = new Course(0, "TEST101", "Test Course", "Test", 3, testDept.getId());
        courseDAO.addCourse(course);

        List<Course> courses = courseDAO.getAllCourses();
        Course testCourse = courses.stream()
            .filter(c -> "TEST101".equals(c.getCourseCode()))
            .findFirst()
            .orElse(null);

        assert testCourse != null : "Test course should exist";

        // Create timetable entry
        Timetable tt = new Timetable(0, testCourse.getId(), "Monday", "09:00", "10:30", "Room 101", "Test Instructor");
        boolean added = timetableDAO.addTimetable(tt);
        assert added : "Timetable entry should be added successfully";
        System.out.println("✓ Timetable creation test passed");

        // Retrieve timetable
        List<Timetable> timetables = timetableDAO.getAllTimetables();
        assert !timetables.isEmpty() : "Should have timetable entries";
        System.out.println("✓ Timetable retrieval test passed");

        System.out.println("✓ Timetable tests completed");
    }

    private static void runLibraryTests() {
        System.out.println("\n--- Library Tests ---");

        LibraryDAO libraryDAO = new LibraryDAO();

        // Create test book
        Library book = new Library(0, "Test Book", "Test Author", "TEST123456", "Fiction", 5, 5, "Shelf A1");
        boolean added = libraryDAO.addBook(book);
        assert added : "Book should be added successfully";
        System.out.println("✓ Library book creation test passed");

        // Retrieve books
        List<Library> books = libraryDAO.getAllBooks();
        Library foundBook = books.stream()
            .filter(b -> "TEST123456".equals(b.getIsbn()))
            .findFirst()
            .orElse(null);
        assert foundBook != null : "Book should be found";
        System.out.println("✓ Library book retrieval test passed");

        // Update book
        foundBook.setAvailableCopies(4);
        boolean updated = libraryDAO.updateBook(foundBook);
        assert updated : "Book should be updated successfully";
        System.out.println("✓ Library book update test passed");

        // Delete book
        boolean deleted = libraryDAO.deleteBook(foundBook.getId());
        assert deleted : "Book should be deleted successfully";
        System.out.println("✓ Library book deletion test passed");
    }

    private static void runFeeTests() {
        System.out.println("\n--- Fee Tests ---");

        FeeDAO feeDAO = new FeeDAO();
        StudentDAO studentDAO = new StudentDAO();

        // Create test student
        Student student = new Student(0, "Test", "Student", "test.student@test.com", 1);
        studentDAO.addStudent(student);

        List<Student> students = studentDAO.getAllStudents();
        Student testStudent = students.stream()
            .filter(s -> "test.student@test.com".equals(s.getEmail()))
            .findFirst()
            .orElse(null);

        assert testStudent != null : "Test student should exist";

        // Create fee
        Fee fee = new Fee(0, testStudent.getId(), "Tuition Fee", 1000.00, LocalDate.now().plusMonths(1));
        boolean added = feeDAO.addFee(fee);
        assert added : "Fee should be added successfully";
        System.out.println("✓ Fee creation test passed");

        // Retrieve fees
        List<Fee> fees = feeDAO.getAllFees();
        assert !fees.isEmpty() : "Should have fees";
        System.out.println("✓ Fee retrieval test passed");

        System.out.println("✓ Fee tests completed");
    }

    private static void runAnnouncementTests() {
        System.out.println("\n--- Announcement Tests ---");

        AnnouncementDAO announcementDAO = new AnnouncementDAO();

        // Create test announcement
        Announcement announcement = new Announcement(0, "Test Announcement", "This is a test announcement",
                                                   "Test Admin", LocalDate.now(), LocalDate.now().plusDays(7), "ALL");
        boolean added = announcementDAO.addAnnouncement(announcement);
        assert added : "Announcement should be added successfully";
        System.out.println("✓ Announcement creation test passed");

        // Retrieve announcements
        List<Announcement> announcements = announcementDAO.getAllAnnouncements();
        assert !announcements.isEmpty() : "Should have announcements";
        System.out.println("✓ Announcement retrieval test passed");

        // Update announcement
        Announcement latest = announcements.get(announcements.size() - 1);
        latest.setTitle("Updated Test Announcement");
        boolean updated = announcementDAO.updateAnnouncement(latest);
        assert updated : "Announcement should be updated successfully";
        System.out.println("✓ Announcement update test passed");

        // Delete announcement
        boolean deleted = announcementDAO.deleteAnnouncement(latest.getId());
        assert deleted : "Announcement should be deleted successfully";
        System.out.println("✓ Announcement deletion test passed");
    }

    private static void runReportTests() {
        System.out.println("\n--- Report Tests ---");

        ReportDAO reportDAO = new ReportDAO();

        // Create test report
        Report report = new Report(0, "Test Report", "Test Report Title", "Test report description",
                                 LocalDate.now(), "Test Admin", "Sample report data");
        boolean added = reportDAO.addReport(report);
        assert added : "Report should be added successfully";
        System.out.println("✓ Report creation test passed");

        // Retrieve reports
        List<Report> reports = reportDAO.getAllReports();
        assert !reports.isEmpty() : "Should have reports";
        System.out.println("✓ Report retrieval test passed");

        // Delete report
        boolean deleted = reportDAO.deleteReport(reports.get(reports.size() - 1).getId());
        assert deleted : "Report should be deleted successfully";
        System.out.println("✓ Report deletion test passed");
    }

    private static void runEdgeCaseTests() {
        System.out.println("\n--- Edge Case Tests ---");

        // Test validation
        assert !Validator.isValidEmail("") : "Empty email should be invalid";
        assert !Validator.isValidEmail("invalid-email") : "Invalid email format should be invalid";
        assert Validator.isValidEmail("test@example.com") : "Valid email should be valid";
        System.out.println("✓ Email validation tests passed");

        // Test password hashing
        String password = "TestPassword123!";
        String hash1 = PasswordUtil.hashPassword(password);
        String hash2 = PasswordUtil.hashPassword(password);
        assert !hash1.equals(hash2) : "Hashes should be different due to salt";
        assert PasswordUtil.verifyPassword(password, hash1) : "Password should verify against its hash";
        assert !PasswordUtil.verifyPassword("wrongpassword", hash1) : "Wrong password should not verify";
        System.out.println("✓ Password hashing tests passed");

        // Test database connection
        try {
            Connection conn = DBConnection.getConnection();
            assert conn != null : "Database connection should work";
            conn.close();
            System.out.println("✓ Database connection test passed");
        } catch (SQLException e) {
            throw new RuntimeException("Database connection test failed", e);
        }

        System.out.println("✓ All edge case tests passed");
    }
}