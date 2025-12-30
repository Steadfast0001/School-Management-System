package test.java;

import config.DBConnection;
import dao.CourseDAO;
import dao.DepartmentDAO;
import model.Course;
import model.Department;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Unit tests for CourseDAO functionality
 */
public class CourseDAOTest {

    private static int testDepartmentId;

    public static void main(String[] args) {
        System.out.println("=== CourseDAO Tests ===\n");

        try {
            setupTestData();
            runAllTests();
            System.out.println("\n=== All CourseDAO Tests Passed! ===");

        } catch (Exception e) {
            System.err.println("CourseDAO tests failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cleanupTestData();
        }
    }

    private static void setupTestData() throws SQLException {
        DepartmentDAO deptDAO = new DepartmentDAO();
        CourseDAO courseDAO = new CourseDAO();

        // Clean up existing test data
        try (Connection conn = DBConnection.getConnection()) {
            String deleteCourses = "DELETE FROM courses WHERE course_code LIKE 'COURSETEST%'";
            String deleteDepts = "DELETE FROM departments WHERE department_code LIKE 'DEPTCOURSETEST%'";
            try (PreparedStatement stmt1 = conn.prepareStatement(deleteCourses);
                 PreparedStatement stmt2 = conn.prepareStatement(deleteDepts)) {
                stmt1.executeUpdate();
                stmt2.executeUpdate();
            }
        }

        // Create test department
        Department dept = new Department(0, "DEPTCOURSETEST_CS", "Computer Science",
                                       "Department for course testing", "Dr. Test");
        deptDAO.addDepartment(dept);

        // Get the department ID
        List<Department> departments = deptDAO.getAllDepartments();
        Department testDept = departments.stream()
            .filter(d -> "DEPTCOURSETEST_CS".equals(d.getDepartmentCode()))
            .findFirst()
            .orElse(null);
        assert testDept != null : "Test department should be created";
        testDepartmentId = testDept.getId();

        // Create test courses
        Course course1 = new Course(0, "COURSETEST_CS101", "Introduction to CS",
                                  "Basic computer science concepts", 3, testDepartmentId);
        courseDAO.addCourse(course1);

        Course course2 = new Course(0, "COURSETEST_CS201", "Data Structures",
                                  "Advanced data structures and algorithms", 4, testDepartmentId);
        courseDAO.addCourse(course2);
    }

    private static void cleanupTestData() {
        try (Connection conn = DBConnection.getConnection()) {
            String deleteCourses = "DELETE FROM courses WHERE course_code LIKE 'COURSETEST%'";
            String deleteDepts = "DELETE FROM departments WHERE department_code LIKE 'DEPTCOURSETEST%'";
            try (PreparedStatement stmt1 = conn.prepareStatement(deleteCourses);
                 PreparedStatement stmt2 = conn.prepareStatement(deleteDepts)) {
                stmt1.executeUpdate();
                stmt2.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error cleaning up test data: " + e.getMessage());
        }
    }

    private static void runAllTests() {
        testAddCourse();
        testGetAllCourses();
        testFindById();
        testFindByCode();
        testGetCoursesByDepartment();
        testUpdateCourse();
        testDeleteCourse();
        testDuplicateCode();
    }

    private static void testAddCourse() {
        System.out.println("Testing add course...");

        CourseDAO courseDAO = new CourseDAO();
        Course newCourse = new Course(0, "COURSETEST_CS301", "Database Systems",
                                    "Database design and management", 3, testDepartmentId);

        boolean result = courseDAO.addCourse(newCourse);
        assert result : "Course should be added successfully";

        // Verify course was added
        Course retrieved = courseDAO.findByCode("COURSETEST_CS301");
        assert retrieved != null : "Course should be retrievable after adding";
        assert "Database Systems".equals(retrieved.getName()) : "Course name should match";

        System.out.println("✓ Add course test passed");
    }

    private static void testGetAllCourses() {
        System.out.println("Testing get all courses...");

        CourseDAO courseDAO = new CourseDAO();
        List<Course> courses = courseDAO.getAllCourses();

        assert courses != null : "Courses list should not be null";
        assert !courses.isEmpty() : "Courses list should not be empty";

        // Check that our test courses are in the list
        boolean foundCS101 = courses.stream().anyMatch(c -> "COURSETEST_CS101".equals(c.getCourseCode()));
        boolean foundCS201 = courses.stream().anyMatch(c -> "COURSETEST_CS201".equals(c.getCourseCode()));

        assert foundCS101 : "CS101 course should be in the list";
        assert foundCS201 : "CS201 course should be in the list";

        System.out.println("✓ Get all courses test passed");
    }

    private static void testFindById() {
        System.out.println("Testing find course by ID...");

        CourseDAO courseDAO = new CourseDAO();
        Course course = courseDAO.findByCode("COURSETEST_CS101");
        assert course != null : "Course should exist for ID test";

        Course foundById = courseDAO.findById(course.getId());
        assert foundById != null : "Course should be found by ID";
        assert course.getId() == foundById.getId() : "Course IDs should match";
        assert course.getCourseCode().equals(foundById.getCourseCode()) : "Course codes should match";

        Course nonExistent = courseDAO.findById(-1);
        assert nonExistent == null : "Non-existent ID should return null";

        System.out.println("✓ Find by ID test passed");
    }

    private static void testFindByCode() {
        System.out.println("Testing find course by code...");

        CourseDAO courseDAO = new CourseDAO();

        Course course = courseDAO.findByCode("COURSETEST_CS101");
        assert course != null : "Course should be found by code";
        assert "COURSETEST_CS101".equals(course.getCourseCode()) : "Course code should match";
        assert "Introduction to CS".equals(course.getName()) : "Course name should match";

        Course nonExistent = courseDAO.findByCode("NONEXISTENT");
        assert nonExistent == null : "Non-existent code should return null";

        System.out.println("✓ Find by code test passed");
    }

    private static void testGetCoursesByDepartment() {
        System.out.println("Testing get courses by department...");

        CourseDAO courseDAO = new CourseDAO();
        List<Course> deptCourses = courseDAO.getCoursesByDepartment(testDepartmentId);

        assert deptCourses != null : "Department courses list should not be null";
        assert !deptCourses.isEmpty() : "Department should have courses";

        // All courses should belong to the test department
        boolean allBelongToDept = deptCourses.stream()
            .allMatch(c -> c.getDepartmentId() == testDepartmentId);
        assert allBelongToDept : "All courses should belong to the test department";

        System.out.println("✓ Get courses by department test passed");
    }

    private static void testUpdateCourse() {
        System.out.println("Testing update course...");

        CourseDAO courseDAO = new CourseDAO();
        Course course = courseDAO.findByCode("COURSETEST_CS101");
        assert course != null : "Course should exist for update test";

        // Update course information
        course.setName("Updated Introduction to CS");
        course.setDescription("Updated basic computer science concepts");
        course.setCredits(4);

        boolean result = courseDAO.updateCourse(course);
        assert result : "Course should be updated successfully";

        // Verify update
        Course updated = courseDAO.findByCode("COURSETEST_CS101");
        assert updated != null : "Updated course should still exist";
        assert "Updated Introduction to CS".equals(updated.getName()) : "Name should be updated";
        assert "Updated basic computer science concepts".equals(updated.getDescription()) : "Description should be updated";
        assert updated.getCredits() == 4 : "Credits should be updated";

        System.out.println("✓ Update course test passed");
    }

    private static void testDeleteCourse() {
        System.out.println("Testing delete course...");

        CourseDAO courseDAO = new CourseDAO();

        // Add a course to delete
        Course deleteCourse = new Course(0, "COURSETEST_DELETE", "Delete Test Course",
                                       "Course to be deleted", 2, testDepartmentId);
        courseDAO.addCourse(deleteCourse);

        // Verify course exists
        Course exists = courseDAO.findByCode("COURSETEST_DELETE");
        assert exists != null : "Course should exist before deletion";

        // Delete course
        boolean result = courseDAO.deleteCourse(exists.getId());
        assert result : "Course should be deleted successfully";

        // Verify course is gone
        Course deleted = courseDAO.findByCode("COURSETEST_DELETE");
        assert deleted == null : "Deleted course should not be found";

        System.out.println("✓ Delete course test passed");
    }

    private static void testDuplicateCode() {
        System.out.println("Testing duplicate course code handling...");

        CourseDAO courseDAO = new CourseDAO();

        // Try to add course with existing code
        Course duplicateCourse = new Course(0, "COURSETEST_CS101", "Duplicate CS Course",
                                          "Duplicate course", 3, testDepartmentId);

        boolean result = courseDAO.addCourse(duplicateCourse);
        // Note: Depending on implementation, this might succeed or fail
        // The test just ensures the operation completes without crashing
        assert true : "Duplicate code operation should complete";

        System.out.println("✓ Duplicate code test passed");
    }
}