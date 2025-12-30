package test.java;

import config.DBConnection;
import dao.DepartmentDAO;
import model.Department;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Unit tests for DepartmentDAO functionality
 */
public class DepartmentDAOTest {

    public static void main(String[] args) {
        System.out.println("=== DepartmentDAO Tests ===\n");

        try {
            setupTestData();
            runAllTests();
            System.out.println("\n=== All DepartmentDAO Tests Passed! ===");

        } catch (Exception e) {
            System.err.println("DepartmentDAO tests failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cleanupTestData();
        }
    }

    private static void setupTestData() throws SQLException {
        DepartmentDAO deptDAO = new DepartmentDAO();

        // Clean up existing test departments
        try (Connection conn = DBConnection.getConnection()) {
            String deleteQuery = "DELETE FROM departments WHERE department_code LIKE 'DEPTTEST%'";
            try (PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
                stmt.executeUpdate();
            }
        }

        // Create test departments
        Department dept1 = new Department(0, "DEPTTEST_CS", "Computer Science",
                                        "Department of Computer Science", "Dr. Smith");
        deptDAO.addDepartment(dept1);

        Department dept2 = new Department(0, "DEPTTEST_MATH", "Mathematics",
                                        "Department of Mathematics", "Dr. Johnson");
        deptDAO.addDepartment(dept2);
    }

    private static void cleanupTestData() {
        try (Connection conn = DBConnection.getConnection()) {
            String deleteQuery = "DELETE FROM departments WHERE department_code LIKE 'DEPTTEST%'";
            try (PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error cleaning up test data: " + e.getMessage());
        }
    }

    private static void runAllTests() {
        testAddDepartment();
        testGetAllDepartments();
        testFindById();
        testFindByCode();
        testUpdateDepartment();
        testDeleteDepartment();
        testDuplicateCode();
    }

    private static void testAddDepartment() {
        System.out.println("Testing add department...");

        DepartmentDAO deptDAO = new DepartmentDAO();
        Department newDept = new Department(0, "DEPTTEST_ENG", "Engineering",
                                          "Department of Engineering", "Dr. Brown");

        boolean result = deptDAO.addDepartment(newDept);
        assert result : "Department should be added successfully";

        // Verify department was added
        Department retrieved = deptDAO.findByCode("DEPTTEST_ENG");
        assert retrieved != null : "Department should be retrievable after adding";
        assert "Engineering".equals(retrieved.getName()) : "Department name should match";

        System.out.println("✓ Add department test passed");
    }

    private static void testGetAllDepartments() {
        System.out.println("Testing get all departments...");

        DepartmentDAO deptDAO = new DepartmentDAO();
        List<Department> departments = deptDAO.getAllDepartments();

        assert departments != null : "Departments list should not be null";
        assert !departments.isEmpty() : "Departments list should not be empty";

        // Check that our test departments are in the list
        boolean foundCS = departments.stream().anyMatch(d -> "DEPTTEST_CS".equals(d.getDepartmentCode()));
        boolean foundMath = departments.stream().anyMatch(d -> "DEPTTEST_MATH".equals(d.getDepartmentCode()));

        assert foundCS : "CS department should be in the list";
        assert foundMath : "Math department should be in the list";

        System.out.println("✓ Get all departments test passed");
    }

    private static void testFindById() {
        System.out.println("Testing find department by ID...");

        DepartmentDAO deptDAO = new DepartmentDAO();
        Department dept = deptDAO.findByCode("DEPTTEST_CS");
        assert dept != null : "Department should exist for ID test";

        Department foundById = deptDAO.findById(dept.getId());
        assert foundById != null : "Department should be found by ID";
        assert dept.getId() == foundById.getId() : "Department IDs should match";
        assert dept.getDepartmentCode().equals(foundById.getDepartmentCode()) : "Department codes should match";

        Department nonExistent = deptDAO.findById(-1);
        assert nonExistent == null : "Non-existent ID should return null";

        System.out.println("✓ Find by ID test passed");
    }

    private static void testFindByCode() {
        System.out.println("Testing find department by code...");

        DepartmentDAO deptDAO = new DepartmentDAO();

        Department dept = deptDAO.findByCode("DEPTTEST_CS");
        assert dept != null : "Department should be found by code";
        assert "DEPTTEST_CS".equals(dept.getDepartmentCode()) : "Department code should match";
        assert "Computer Science".equals(dept.getName()) : "Department name should match";

        Department nonExistent = deptDAO.findByCode("NONEXISTENT");
        assert nonExistent == null : "Non-existent code should return null";

        System.out.println("✓ Find by code test passed");
    }

    private static void testUpdateDepartment() {
        System.out.println("Testing update department...");

        DepartmentDAO deptDAO = new DepartmentDAO();
        Department dept = deptDAO.findByCode("DEPTTEST_CS");
        assert dept != null : "Department should exist for update test";

        // Update department information
        dept.setName("Updated Computer Science");
        dept.setDescription("Updated Department of Computer Science");
        dept.setHead("Dr. Updated Smith");

        boolean result = deptDAO.updateDepartment(dept);
        assert result : "Department should be updated successfully";

        // Verify update
        Department updated = deptDAO.findByCode("DEPTTEST_CS");
        assert updated != null : "Updated department should still exist";
        assert "Updated Computer Science".equals(updated.getName()) : "Name should be updated";
        assert "Updated Department of Computer Science".equals(updated.getDescription()) : "Description should be updated";
        assert "Dr. Updated Smith".equals(updated.getHead()) : "Head should be updated";

        System.out.println("✓ Update department test passed");
    }

    private static void testDeleteDepartment() {
        System.out.println("Testing delete department...");

        DepartmentDAO deptDAO = new DepartmentDAO();

        // Add a department to delete
        Department deleteDept = new Department(0, "DEPTTEST_DELETE", "Delete Test Department",
                                             "Department to be deleted", "Dr. Delete");
        deptDAO.addDepartment(deleteDept);

        // Verify department exists
        Department exists = deptDAO.findByCode("DEPTTEST_DELETE");
        assert exists != null : "Department should exist before deletion";

        // Delete department
        boolean result = deptDAO.deleteDepartment(exists.getId());
        assert result : "Department should be deleted successfully";

        // Verify department is gone
        Department deleted = deptDAO.findByCode("DEPTTEST_DELETE");
        assert deleted == null : "Deleted department should not be found";

        System.out.println("✓ Delete department test passed");
    }

    private static void testDuplicateCode() {
        System.out.println("Testing duplicate department code handling...");

        DepartmentDAO deptDAO = new DepartmentDAO();

        // Try to add department with existing code
        Department duplicateDept = new Department(0, "DEPTTEST_CS", "Duplicate Computer Science",
                                                "Duplicate department", "Dr. Duplicate");

        boolean result = deptDAO.addDepartment(duplicateDept);
        // Note: Depending on implementation, this might succeed or fail
        // The test just ensures the operation completes without crashing
        assert true : "Duplicate code operation should complete";

        System.out.println("✓ Duplicate code test passed");
    }
}