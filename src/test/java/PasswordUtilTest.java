package test.java;

import utils.PasswordUtil;

/**
 * Unit tests for PasswordUtil functionality
 */
public class PasswordUtilTest {

    public static void main(String[] args) {
        System.out.println("=== PasswordUtil Tests ===\n");

        try {
            runAllTests();
            System.out.println("\n=== All PasswordUtil Tests Passed! ===");

        } catch (Exception e) {
            System.err.println("PasswordUtil tests failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void runAllTests() {
        testPasswordHashing();
        testPasswordVerification();
        testHashUniqueness();
        testInvalidInputs();
        testHashConsistency();
    }

    private static void testPasswordHashing() {
        System.out.println("Testing password hashing...");

        String password = "TestPassword123!";
        String hash = PasswordUtil.hash(password);

        assert hash != null : "Hash should not be null";
        assert !hash.isEmpty() : "Hash should not be empty";
        assert !hash.equals(password) : "Hash should not equal original password";

        // PBKDF2 hash should be in format: iterations:salt:hash
        String[] parts = hash.split(":");
        assert parts.length == 3 : "Hash should have 3 parts separated by colons";

        System.out.println("✓ Password hashing test passed");
    }

    private static void testPasswordVerification() {
        System.out.println("Testing password verification...");

        String password = "MySecurePassword123!";

        // Hash the password
        String hash = PasswordUtil.hash(password);

        // Verify correct password
        assert PasswordUtil.verify(password, hash) : "Correct password should verify";

        // Verify incorrect password
        assert !PasswordUtil.verify("WrongPassword123!", hash) : "Incorrect password should not verify";
        assert !PasswordUtil.verify("", hash) : "Empty password should not verify";
        assert !PasswordUtil.verify("mypassword", hash) : "Different password should not verify";

        System.out.println("✓ Password verification test passed");
    }

    private static void testHashUniqueness() {
        System.out.println("Testing hash uniqueness...");

        String password = "SamePassword123!";

        // Hash the same password multiple times
        String hash1 = PasswordUtil.hash(password);
        String hash2 = PasswordUtil.hash(password);
        String hash3 = PasswordUtil.hash(password);

        // All hashes should be different due to random salts
        assert !hash1.equals(hash2) : "Different hashes should be generated for same password";
        assert !hash1.equals(hash3) : "Different hashes should be generated for same password";
        assert !hash2.equals(hash3) : "Different hashes should be generated for same password";

        // But all should verify correctly
        assert PasswordUtil.verify(password, hash1) : "Hash1 should verify password";
        assert PasswordUtil.verify(password, hash2) : "Hash2 should verify password";
        assert PasswordUtil.verify(password, hash3) : "Hash3 should verify password";

        System.out.println("✓ Hash uniqueness test passed");
    }

    private static void testInvalidInputs() {
        System.out.println("Testing invalid input handling...");

        // Test null password
        try {
            String hash = PasswordUtil.hash(null);
            assert false : "Null password should throw exception";
        } catch (Exception e) {
            // Expected exception
        }

        // Test empty password
        try {
            String hash = PasswordUtil.hash("");
            // Empty password might be allowed, depending on implementation
            // Just ensure it doesn't crash
            assert hash != null : "Empty password should not crash";
        } catch (Exception e) {
            // If exception is thrown, that's also acceptable
        }

        // Test null hash verification
        assert !PasswordUtil.verify("password", null) : "Null hash should not verify";

        // Test null password verification
        assert !PasswordUtil.verify(null, "hash") : "Null password should not verify";

        // Test invalid hash format
        assert !PasswordUtil.verify("password", "invalid") : "Invalid hash format should not verify";
        assert !PasswordUtil.verify("password", "") : "Empty hash should not verify";
        assert !PasswordUtil.verify("password", "part1:part2") : "Incomplete hash should not verify";

        System.out.println("✓ Invalid input handling test passed");
    }

    private static void testHashConsistency() {
        System.out.println("Testing hash consistency...");

        String password = "ConsistentPassword123!";
        String hash = PasswordUtil.hash(password);

        // Verify multiple times with same hash
        for (int i = 0; i < 10; i++) {
            assert PasswordUtil.verify(password, hash) : "Password should consistently verify against its hash";
        }

        // Verify that wrong password consistently fails
        for (int i = 0; i < 10; i++) {
            assert !PasswordUtil.verify("wrongpassword", hash) : "Wrong password should consistently fail verification";
        }

        System.out.println("✓ Hash consistency test passed");
    }
}