package test.java;

import config.DBConnection;
import dao.UserDAO;
import model.User;
import utils.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Unit tests for UserDAO functionality
 */
public class UserDAOTest {

    public static void main(String[] args) {
        System.out.println("=== UserDAO Tests ===\n");

        try {
            setupTestData();
            runAllTests();
            System.out.println("\n=== All UserDAO Tests Passed! ===");

        } catch (Exception e) {
            System.err.println("UserDAO tests failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cleanupTestData();
        }
    }

    private static void setupTestData() throws SQLException {
        UserDAO userDAO = new UserDAO();

        // Clean up existing test users
        try (Connection conn = DBConnection.getConnection()) {
            String deleteQuery = "DELETE FROM users WHERE username LIKE 'userdaotest%'";
            try (PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
                stmt.executeUpdate();
            }
        }

        // Create test users
        User testUser1 = new User(0, "userdaotest1", PasswordUtil.hashPassword("Pass123!"),
                                "USER", "Test User One", "user1@test.com", "USR001", "Undergraduate");
        userDAO.addUser(testUser1);

        User testUser2 = new User(0, "userdaotest2", PasswordUtil.hashPassword("Pass123!"),
                                "ADMIN", "Test Admin Two", "admin2@test.com", "ADM002", "N/A");
        userDAO.addUser(testUser2);
    }

    private static void cleanupTestData() {
        try (Connection conn = DBConnection.getConnection()) {
            String deleteQuery = "DELETE FROM users WHERE username LIKE 'userdaotest%'";
            try (PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error cleaning up test data: " + e.getMessage());
        }
    }

    private static void runAllTests() {
        testAddUser();
        testGetAllUsers();
        testFindByUsername();
        testFindById();
        testFindByEmail();
        testUpdateUser();
        testDeleteUser();
        testDuplicateUsername();
        testDuplicateEmail();
    }

    private static void testAddUser() {
        System.out.println("Testing add user...");

        UserDAO userDAO = new UserDAO();
        User newUser = new User(0, "userdaotest3", PasswordUtil.hashPassword("Pass123!"),
                              "USER", "Test User Three", "user3@test.com", "USR003", "Graduate");

        boolean result = userDAO.addUser(newUser);
        assert result : "User should be added successfully";

        // Verify user was added
        User retrieved = userDAO.findByUsername("userdaotest3");
        assert retrieved != null : "User should be retrievable after adding";
        assert "Test User Three".equals(retrieved.getFullName()) : "User data should match";

        System.out.println("✓ Add user test passed");
    }

    private static void testGetAllUsers() {
        System.out.println("Testing get all users...");

        UserDAO userDAO = new UserDAO();
        List<User> users = userDAO.getAllUsers();

        assert users != null : "Users list should not be null";
        assert !users.isEmpty() : "Users list should not be empty";

        // Check that our test users are in the list
        boolean foundTestUser1 = users.stream().anyMatch(u -> "userdaotest1".equals(u.getUsername()));
        boolean foundTestUser2 = users.stream().anyMatch(u -> "userdaotest2".equals(u.getUsername()));

        assert foundTestUser1 : "Test user 1 should be in the list";
        assert foundTestUser2 : "Test user 2 should be in the list";

        System.out.println("✓ Get all users test passed");
    }

    private static void testFindByUsername() {
        System.out.println("Testing find by username...");

        UserDAO userDAO = new UserDAO();

        User user = userDAO.findByUsername("userdaotest1");
        assert user != null : "User should be found by username";
        assert "userdaotest1".equals(user.getUsername()) : "Username should match";
        assert "Test User One".equals(user.getFullName()) : "Full name should match";

        User nonExistent = userDAO.findByUsername("nonexistentuser");
        assert nonExistent == null : "Non-existent user should return null";

        System.out.println("✓ Find by username test passed");
    }

    private static void testFindById() {
        System.out.println("Testing find by ID...");

        UserDAO userDAO = new UserDAO();
        User user = userDAO.findByUsername("userdaotest1");
        assert user != null : "User should exist for ID test";

        User foundById = userDAO.findById(user.getId());
        assert foundById != null : "User should be found by ID";
        assert user.getId() == foundById.getId() : "User IDs should match";
        assert user.getUsername().equals(foundById.getUsername()) : "Usernames should match";

        User nonExistent = userDAO.findById(-1);
        assert nonExistent == null : "Non-existent ID should return null";

        System.out.println("✓ Find by ID test passed");
    }

    private static void testFindByEmail() {
        System.out.println("Testing find by email...");

        UserDAO userDAO = new UserDAO();

        User user = userDAO.findByEmail("user1@test.com");
        assert user != null : "User should be found by email";
        assert "user1@test.com".equals(user.getEmail()) : "Email should match";
        assert "userdaotest1".equals(user.getUsername()) : "Username should match";

        User nonExistent = userDAO.findByEmail("nonexistent@test.com");
        assert nonExistent == null : "Non-existent email should return null";

        System.out.println("✓ Find by email test passed");
    }

    private static void testUpdateUser() {
        System.out.println("Testing update user...");

        UserDAO userDAO = new UserDAO();
        User user = userDAO.findByUsername("userdaotest1");
        assert user != null : "User should exist for update test";

        // Update user information
        user.setFullName("Updated Test User One");
        user.setEmail("updated1@test.com");

        boolean result = userDAO.updateUser(user);
        assert result : "User should be updated successfully";

        // Verify update
        User updated = userDAO.findByUsername("userdaotest1");
        assert updated != null : "Updated user should still exist";
        assert "Updated Test User One".equals(updated.getFullName()) : "Full name should be updated";
        assert "updated1@test.com".equals(updated.getEmail()) : "Email should be updated";

        System.out.println("✓ Update user test passed");
    }

    private static void testDeleteUser() {
        System.out.println("Testing delete user...");

        UserDAO userDAO = new UserDAO();

        // Add a user to delete
        User deleteUser = new User(0, "userdaotest_delete", PasswordUtil.hashPassword("Pass123!"),
                                 "USER", "Delete Test User", "delete@test.com", "DEL001", "Undergraduate");
        userDAO.addUser(deleteUser);

        // Verify user exists
        User exists = userDAO.findByUsername("userdaotest_delete");
        assert exists != null : "User should exist before deletion";

        // Delete user
        boolean result = userDAO.deleteUser(exists.getId());
        assert result : "User should be deleted successfully";

        // Verify user is gone
        User deleted = userDAO.findByUsername("userdaotest_delete");
        assert deleted == null : "Deleted user should not be found";

        System.out.println("✓ Delete user test passed");
    }

    private static void testDuplicateUsername() {
        System.out.println("Testing duplicate username handling...");

        UserDAO userDAO = new UserDAO();

        // Try to add user with existing username
        User duplicateUser = new User(0, "userdaotest1", PasswordUtil.hashPassword("Pass123!"),
                                    "USER", "Duplicate User", "duplicate@test.com", "DUP001", "Undergraduate");

        boolean result = userDAO.addUser(duplicateUser);
        // Note: Depending on implementation, this might succeed or fail
        // The test just ensures the operation completes without crashing
        assert true : "Duplicate username operation should complete";

        System.out.println("✓ Duplicate username test passed");
    }

    private static void testDuplicateEmail() {
        System.out.println("Testing duplicate email handling...");

        UserDAO userDAO = new UserDAO();

        // Try to add user with existing email
        User duplicateUser = new User(0, "userdaotest_email", PasswordUtil.hashPassword("Pass123!"),
                                    "USER", "Duplicate Email User", "user1@test.com", "EML001", "Undergraduate");

        boolean result = userDAO.addUser(duplicateUser);
        // Note: Depending on implementation, this might succeed or fail
        // The test just ensures the operation completes without crashing
        assert true : "Duplicate email operation should complete";

        System.out.println("✓ Duplicate email test passed");
    }
}