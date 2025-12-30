package test.java;

import config.DBConnection;
import dao.UserDAO;
import model.User;
import utils.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * AuthService with role checking and authentication
 * Combined with unit tests for authentication
 */
public class AuthServiceTest {

    // -----------------------
    // Authentication Service
    // -----------------------
    public static class AuthService {

        private final UserDAO userDAO = new UserDAO();

        // Authenticate a user by username and password
        public User authenticate(String username, String password) {
            if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
                return null;
            }
            User user = userDAO.findByUsername(username);
            if (user != null && PasswordUtil.verifyPassword(password, user.getPassword())) {
                return user;
            }
            return null;
        }

        // Check if a user has a specific role
        public boolean hasRole(User user, String role) {
            return user != null && role.equalsIgnoreCase(user.getRole());
        }

        // Shortcut for checking admin role
        public boolean isAdmin(User user) {
            return hasRole(user, "ADMIN");
        }
    }

    // -----------------------
    // Unit Test Execution
    // -----------------------
    public static void main(String[] args) {
        System.out.println("=== Authentication Service Tests ===\n");

        try {
            setupTestData();
            runAllTests();
            System.out.println("\n=== All Authentication Tests Passed! ===");

        } catch (Exception e) {
            System.err.println("Authentication tests failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cleanupTestData();
        }
    }

    // Setup test users in the database
    private static void setupTestData() throws SQLException {
        UserDAO userDAO = new UserDAO();

        try (Connection conn = DBConnection.getConnection()) {
            String deleteQuery = "DELETE FROM users WHERE username LIKE 'authtest%'";
            try (PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
                stmt.executeUpdate();
            }
        }

        User adminUser = new User(0, "authtestadmin",
                PasswordUtil.hashPassword("AdminPass123!"),
                "ADMIN", "Auth Test Admin", "admin@test.com", "ADM001", "N/A");
        userDAO.addUser(adminUser);

        User regularUser = new User(0, "authtestuser",
                PasswordUtil.hashPassword("UserPass123!"),
                "USER", "Auth Test User", "user@test.com", "USR001", "Undergraduate");
        userDAO.addUser(regularUser);
    }

    // Cleanup test users
    private static void cleanupTestData() {
        try (Connection conn = DBConnection.getConnection()) {
            String deleteQuery = "DELETE FROM users WHERE username LIKE 'authtest%'";
            try (PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error cleaning up test data: " + e.getMessage());
        }
    }

    // Run all test cases
    private static void runAllTests() {
        testValidAdminLogin();
        testValidUserLogin();
        testInvalidUsername();
        testInvalidPassword();
        testNullCredentials();
        testEmptyCredentials();
        testRoleChecking();
        testPasswordVerification();
    }

    private static void testValidAdminLogin() {
        System.out.println("Testing valid admin login...");
        AuthService authService = new AuthService();
        User user = authService.authenticate("authtestadmin", "AdminPass123!");
        assert user != null : "Admin login should succeed";
        assert "ADMIN".equals(user.getRole()) : "User should have ADMIN role";
        assert "Auth Test Admin".equals(user.getFullName()) : "Full name should match";
        System.out.println("✓ Valid admin login test passed");
    }

    private static void testValidUserLogin() {
        System.out.println("Testing valid user login...");
        AuthService authService = new AuthService();
        User user = authService.authenticate("authtestuser", "UserPass123!");
        assert user != null : "User login should succeed";
        assert "USER".equals(user.getRole()) : "User should have USER role";
        assert "Auth Test User".equals(user.getFullName()) : "Full name should match";
        System.out.println("✓ Valid user login test passed");
    }

    private static void testInvalidUsername() {
        System.out.println("Testing invalid username...");
        AuthService authService = new AuthService();
        User user = authService.authenticate("nonexistentuser", "SomePass123!");
        assert user == null : "Login with invalid username should fail";
        System.out.println("✓ Invalid username test passed");
    }

    private static void testInvalidPassword() {
        System.out.println("Testing invalid password...");
        AuthService authService = new AuthService();
        User user = authService.authenticate("authtestadmin", "WrongPassword123!");
        assert user == null : "Login with invalid password should fail";
        System.out.println("✓ Invalid password test passed");
    }

    private static void testNullCredentials() {
        System.out.println("Testing null credentials...");
        AuthService authService = new AuthService();
        assert authService.authenticate(null, "SomePass123!") == null : "Null username should fail";
        assert authService.authenticate("authtestadmin", null) == null : "Null password should fail";
        System.out.println("✓ Null credentials test passed");
    }

    private static void testEmptyCredentials() {
        System.out.println("Testing empty credentials...");
        AuthService authService = new AuthService();
        assert authService.authenticate("", "SomePass123!") == null : "Empty username should fail";
        assert authService.authenticate("authtestadmin", "") == null : "Empty password should fail";
        System.out.println("✓ Empty credentials test passed");
    }

    private static void testRoleChecking() {
        System.out.println("Testing role checking...");
        AuthService authService = new AuthService();
        User adminUser = authService.authenticate("authtestadmin", "AdminPass123!");
        User regularUser = authService.authenticate("authtestuser", "UserPass123!");
        assert authService.isAdmin(adminUser) : "Admin should be admin";
        assert !authService.isAdmin(regularUser) : "Regular user should not be admin";
        System.out.println("✓ Role checking test passed");
    }

    private static void testPasswordVerification() {
        System.out.println("Testing password verification...");
        String password = "TestVerifyPass123!";
        String hashedPassword = PasswordUtil.hashPassword(password);
        assert PasswordUtil.verifyPassword(password, hashedPassword) : "Password should verify";
        assert !PasswordUtil.verifyPassword("wrongpassword", hashedPassword) : "Wrong password should fail";
        assert !PasswordUtil.verifyPassword("", hashedPassword) : "Empty password should fail";
        assert !PasswordUtil.verifyPassword(password, "wronghash") : "Wrong hash should fail";
        System.out.println("✓ Password verification test passed");
    }
}
