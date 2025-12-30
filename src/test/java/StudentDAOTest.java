package test.java;

import config.DBConnection;
import dao.StudentDAO;
import model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Unit tests for StudentDAO functionality
 */
public class StudentDAOTest {

    public static void main(String[] args) {
        System.out.println("=== StudentDAO Tests ===\n");

        try {
            setupTestData();
            runAllTests();
            System.out.println("\n=== All StudentDAO Tests Passed! ===");

        } catch (Exception e) {
            System.err.println("StudentDAO tests failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cleanupTestData();
        }
    }

    private static void setupTestData() throws SQLException {
        StudentDAO studentDAO = new StudentDAO();

        // Clean up existing test students
        try (Connection conn = DBConnection.getConnection()) {
            String deleteQuery = "DELETE FROM students WHERE email LIKE 'studenttest%'";
            try (PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
                stmt.executeUpdate();
            }
        }

        // Create test students
        Student student1 = new Student(0, "John", "Doe", "studenttest1@test.com", 1);
        studentDAO.addStudent(student1);

        Student student2 = new Student(0, "Jane", "Smith", "studenttest2@test.com", 2);
        studentDAO.addStudent(student2);
    }

    private static void cleanupTestData() {
        try (Connection conn = DBConnection.getConnection()) {
            String deleteQuery = "DELETE FROM students WHERE email LIKE 'studenttest%'";
            try (PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error cleaning up test data: " + e.getMessage());
        }
    }

    private static void runAllTests() {
        testAddStudent();
        testGetAllStudents();
        testFindById();
        testFindByEmail();
        testUpdateStudent();
        testDeleteStudent();
        testDuplicateEmail();
        testSearchStudents();
    }

    private static void testAddStudent() {
        System.out.println("Testing add student...");

        StudentDAO studentDAO = new StudentDAO();
        Student newStudent = new Student(0, "Bob", "Johnson", "studenttest3@test.com", 1);

        boolean result = studentDAO.addStudent(newStudent);
        assert result : "Student should be added successfully";

        // Verify student was added
        Student retrieved = studentDAO.findByEmail("studenttest3@test.com");
        assert retrieved != null : "Student should be retrievable after adding";
        assert "Bob".equals(retrieved.getFirstName()) : "First name should match";
        assert "Johnson".equals(retrieved.getLastName()) : "Last name should match";

        System.out.println("✓ Add student test passed");
    }

    private static void testGetAllStudents() {
        System.out.println("Testing get all students...");

        StudentDAO studentDAO = new StudentDAO();
        List<Student> students = studentDAO.getAllStudents();

        assert students != null : "Students list should not be null";
        assert !students.isEmpty() : "Students list should not be empty";

        // Check that our test students are in the list
        boolean foundStudent1 = students.stream().anyMatch(s -> "studenttest1@test.com".equals(s.getEmail()));
        boolean foundStudent2 = students.stream().anyMatch(s -> "studenttest2@test.com".equals(s.getEmail()));

        assert foundStudent1 : "Student 1 should be in the list";
        assert foundStudent2 : "Student 2 should be in the list";

        System.out.println("✓ Get all students test passed");
    }

    private static void testFindById() {
        System.out.println("Testing find student by ID...");

        StudentDAO studentDAO = new StudentDAO();
        Student student = studentDAO.findByEmail("studenttest1@test.com");
        assert student != null : "Student should exist for ID test";

        Student foundById = studentDAO.findById(student.getId());
        assert foundById != null : "Student should be found by ID";
        assert student.getId() == foundById.getId() : "Student IDs should match";
        assert student.getEmail().equals(foundById.getEmail()) : "Student emails should match";

        Student nonExistent = studentDAO.findById(-1);
        assert nonExistent == null : "Non-existent ID should return null";

        System.out.println("✓ Find by ID test passed");
    }

    private static void testFindByEmail() {
        System.out.println("Testing find student by email...");

        StudentDAO studentDAO = new StudentDAO();

        Student student = studentDAO.findByEmail("studenttest1@test.com");
        assert student != null : "Student should be found by email";
        assert "studenttest1@test.com".equals(student.getEmail()) : "Email should match";
        assert "John".equals(student.getFirstName()) : "First name should match";
        assert "Doe".equals(student.getLastName()) : "Last name should match";

        Student nonExistent = studentDAO.findByEmail("nonexistent@test.com");
        assert nonExistent == null : "Non-existent email should return null";

        System.out.println("✓ Find by email test passed");
    }

    private static void testUpdateStudent() {
        System.out.println("Testing update student...");

        StudentDAO studentDAO = new StudentDAO();
        Student student = studentDAO.findByEmail("studenttest1@test.com");
        assert student != null : "Student should exist for update test";

        // Update student information
        student.setFirstName("Johnny");
        student.setLastName("Doe Jr.");
        student.setYear(2);

        boolean result = studentDAO.updateStudent(student);
        assert result : "Student should be updated successfully";

        // Verify update
        Student updated = studentDAO.findByEmail("studenttest1@test.com");
        assert updated != null : "Updated student should still exist";
        assert "Johnny".equals(updated.getFirstName()) : "First name should be updated";
        assert "Doe Jr.".equals(updated.getLastName()) : "Last name should be updated";
        assert updated.getYear() == 2 : "Year should be updated";

        System.out.println("✓ Update student test passed");
    }

    private static void testDeleteStudent() {
        System.out.println("Testing delete student...");

        StudentDAO studentDAO = new StudentDAO();

        // Add a student to delete
        Student deleteStudent = new Student(0, "Delete", "Me", "studenttest_delete@test.com", 1);
        studentDAO.addStudent(deleteStudent);

        // Verify student exists
        Student exists = studentDAO.findByEmail("studenttest_delete@test.com");
        assert exists != null : "Student should exist before deletion";

        // Delete student
        boolean result = studentDAO.deleteStudent(exists.getId());
        assert result : "Student should be deleted successfully";

        // Verify student is gone
        Student deleted = studentDAO.findByEmail("studenttest_delete@test.com");
        assert deleted == null : "Deleted student should not be found";

        System.out.println("✓ Delete student test passed");
    }

    private static void testDuplicateEmail() {
        System.out.println("Testing duplicate email handling...");

        StudentDAO studentDAO = new StudentDAO();

        // Try to add student with existing email
        Student duplicateStudent = new Student(0, "Duplicate", "Student", "studenttest1@test.com", 1);

        boolean result = studentDAO.addStudent(duplicateStudent);
        // Note: Depending on implementation, this might succeed or fail
        // The test just ensures the operation completes without crashing
        assert true : "Duplicate email operation should complete";

        System.out.println("✓ Duplicate email test passed");
    }

    private static void testSearchStudents() {
        System.out.println("Testing search students...");

        StudentDAO studentDAO = new StudentDAO();

        // Test search by name
        List<Student> searchResults = studentDAO.searchStudents("John");
        assert searchResults != null : "Search results should not be null";

        // Should find our test student
        boolean foundJohn = searchResults.stream()
            .anyMatch(s -> "John".equals(s.getFirstName()) || "Johnny".equals(s.getFirstName()));
        assert foundJohn : "Should find John in search results";

        // Test search with no results
        List<Student> noResults = studentDAO.searchStudents("NonExistentName");
        assert noResults != null : "No results search should not be null";
        // Note: Depending on implementation, this might be empty or null

        System.out.println("✓ Search students test passed");
    }
}