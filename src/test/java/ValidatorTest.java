package test.java;

import utils.Validator;

/**
 * Unit tests for Validator utility class
 */
public class ValidatorTest {

    public static void main(String[] args) {
        System.out.println("=== Validator Tests ===\n");

        try {
            runAllTests();
            System.out.println("\n=== All Validator Tests Passed! ===");

        } catch (Exception e) {
            System.err.println("Validator tests failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void runAllTests() {
        testIsNullOrEmpty();
        testHasMinLength();
        testIsAlphabetic();
        testValidEmail();
        testInvalidEmail();
        testIsInteger();
        testIsPositiveInteger();
        testIsDouble();
        testValidScore();
        testValidDate();
        testValidPassword();
        testInvalidPassword();
        testNullAndEmptyValues();
    }

    private static void testIsNullOrEmpty() {
        System.out.println("Testing isNullOrEmpty...");

        assert Validator.isNullOrEmpty(null) : "Null should be null or empty";
        assert Validator.isNullOrEmpty("") : "Empty string should be null or empty";
        assert Validator.isNullOrEmpty("   ") : "Whitespace string should be null or empty";
        assert !Validator.isNullOrEmpty("test") : "Non-empty string should not be null or empty";
        assert !Validator.isNullOrEmpty(" test ") : "String with content should not be null or empty";

        System.out.println("✓ isNullOrEmpty tests passed");
    }

    private static void testHasMinLength() {
        System.out.println("Testing hasMinLength...");

        assert Validator.hasMinLength("hello", 3) : "String longer than min should pass";
        assert Validator.hasMinLength("hi", 2) : "String equal to min should pass";
        assert !Validator.hasMinLength("hi", 3) : "String shorter than min should fail";
        assert !Validator.hasMinLength(null, 1) : "Null string should fail";
        assert !Validator.hasMinLength("", 1) : "Empty string should fail";

        System.out.println("✓ hasMinLength tests passed");
    }

    private static void testIsAlphabetic() {
        System.out.println("Testing isAlphabetic...");

        assert Validator.isAlphabetic("Hello") : "Alphabetic string should pass";
        assert Validator.isAlphabetic("Hello World") : "Alphabetic string with space should pass";
        assert Validator.isAlphabetic("José") : "String with accent should pass";
        assert !Validator.isAlphabetic("Hello123") : "String with numbers should fail";
        assert !Validator.isAlphabetic("Hello@") : "String with special chars should fail";
        assert !Validator.isAlphabetic(null) : "Null string should fail";

        System.out.println("✓ isAlphabetic tests passed");
    }

    private static void testValidEmail() {
        System.out.println("Testing valid email addresses...");

        assert Validator.isValidEmail("test@example.com") : "Standard email should be valid";
        assert Validator.isValidEmail("user.name@domain.co.uk") : "Email with subdomain should be valid";
        assert Validator.isValidEmail("test+tag@gmail.com") : "Email with plus sign should be valid";
        assert Validator.isValidEmail("123@test.com") : "Email starting with numbers should be valid";

        System.out.println("✓ Valid email tests passed");
    }

    private static void testInvalidEmail() {
        System.out.println("Testing invalid email addresses...");

        assert !Validator.isValidEmail("") : "Empty string should be invalid";
        assert !Validator.isValidEmail("invalid-email") : "Email without @ should be invalid";
        assert !Validator.isValidEmail("@domain.com") : "Email without local part should be invalid";
        assert !Validator.isValidEmail("user@") : "Email without domain should be invalid";
        assert !Validator.isValidEmail("user name@domain.com") : "Email with space should be invalid";
        assert !Validator.isValidEmail(null) : "Null email should be invalid";

        System.out.println("✓ Invalid email tests passed");
    }

    private static void testIsInteger() {
        System.out.println("Testing isInteger...");

        assert Validator.isInteger("123") : "Valid integer string should pass";
        assert Validator.isInteger("0") : "Zero should pass";
        assert Validator.isInteger("-123") : "Negative integer should pass";
        assert !Validator.isInteger("123.45") : "Decimal should fail";
        assert !Validator.isInteger("abc") : "Non-numeric should fail";
        assert !Validator.isInteger("") : "Empty string should fail";
        assert !Validator.isInteger(null) : "Null should fail";

        System.out.println("✓ isInteger tests passed");
    }

    private static void testIsPositiveInteger() {
        System.out.println("Testing isPositiveInteger...");

        assert Validator.isPositiveInteger("123") : "Positive integer should pass";
        assert Validator.isPositiveInteger("1") : "One should pass";
        assert !Validator.isPositiveInteger("0") : "Zero should fail";
        assert !Validator.isPositiveInteger("-123") : "Negative integer should fail";
        assert !Validator.isPositiveInteger("123.45") : "Decimal should fail";
        assert !Validator.isPositiveInteger("abc") : "Non-numeric should fail";

        System.out.println("✓ isPositiveInteger tests passed");
    }

    private static void testIsDouble() {
        System.out.println("Testing isDouble...");

        assert Validator.isDouble("123.45") : "Valid decimal should pass";
        assert Validator.isDouble("123") : "Integer should pass as double";
        assert Validator.isDouble("0.0") : "Zero decimal should pass";
        assert Validator.isDouble("-123.45") : "Negative decimal should pass";
        assert !Validator.isDouble("abc") : "Non-numeric should fail";
        assert !Validator.isDouble("") : "Empty string should fail";
        assert !Validator.isDouble(null) : "Null should fail";

        System.out.println("✓ isDouble tests passed");
    }

    private static void testValidScore() {
        System.out.println("Testing valid scores...");

        assert Validator.isValidScore("0") : "Zero score should be valid";
        assert Validator.isValidScore("50") : "Mid score should be valid";
        assert Validator.isValidScore("100") : "Perfect score should be valid";
        assert !Validator.isValidScore("-1") : "Negative score should be invalid";
        assert !Validator.isValidScore("101") : "Score over 100 should be invalid";
        assert !Validator.isValidScore("abc") : "Non-numeric score should be invalid";

        System.out.println("✓ Valid score tests passed");
    }

    private static void testValidDate() {
        System.out.println("Testing valid dates...");

        assert Validator.isValidDate("2023-12-25") : "Valid date should pass";
        assert Validator.isValidDate("1999-01-01") : "Another valid date should pass";
        assert !Validator.isValidDate("12-25-2023") : "Wrong format should fail";
        assert !Validator.isValidDate("2023/12/25") : "Wrong separators should fail";
        assert !Validator.isValidDate("2023-13-25") : "Invalid month should fail";
        assert !Validator.isValidDate("2023-12-32") : "Invalid day should fail";
        assert !Validator.isValidDate("abc") : "Non-date should fail";
        assert !Validator.isValidDate("") : "Empty string should fail";
        assert !Validator.isValidDate(null) : "Null should fail";

        System.out.println("✓ Valid date tests passed");
    }

    private static void testValidPassword() {
        System.out.println("Testing valid passwords...");

        assert Validator.isValidPassword("password") : "6+ character password should be valid";
        assert Validator.isValidPassword("longerpassword") : "Long password should be valid";
        assert !Validator.isValidPassword("") : "Empty password should be invalid";
        assert !Validator.isValidPassword("12345") : "5 character password should be invalid";
        assert !Validator.isValidPassword(null) : "Null password should be invalid";

        System.out.println("✓ Valid password tests passed");
    }

    private static void testInvalidPassword() {
        System.out.println("Testing invalid passwords...");

        assert !Validator.isValidPassword("") : "Empty password should be invalid";
        assert !Validator.isValidPassword("12345") : "Too short password should be invalid";
        assert !Validator.isValidPassword(null) : "Null password should be invalid";

        System.out.println("✓ Invalid password tests passed");
    }

    private static void testNullAndEmptyValues() {
        System.out.println("Testing null and empty value handling...");

        // Test null values
        assert !Validator.isValidEmail(null) : "Null email should be invalid";
        assert !Validator.isValidPassword(null) : "Null password should be invalid";
        assert !Validator.isInteger(null) : "Null integer should be invalid";
        assert !Validator.isDouble(null) : "Null double should be invalid";
        assert !Validator.isValidDate(null) : "Null date should be invalid";
        assert !Validator.isAlphabetic(null) : "Null alphabetic should be invalid";

        // Test empty strings
        assert !Validator.isValidEmail("") : "Empty email should be invalid";
        assert !Validator.isValidPassword("") : "Empty password should be invalid";
        assert !Validator.isInteger("") : "Empty integer should be invalid";
        assert !Validator.isDouble("") : "Empty double should be invalid";
        assert !Validator.isValidDate("") : "Empty date should be invalid";
        assert !Validator.isAlphabetic("") : "Empty alphabetic should be invalid";

        System.out.println("✓ Null and empty value tests passed");
    }
}