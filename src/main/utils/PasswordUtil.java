package utils;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordUtil {
    private static final int SALT_LEN = 16;
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256; // bits

    public static String hash(String password) {
        try {
            byte[] salt = new byte[SALT_LEN];
            SecureRandom sr = new SecureRandom();
            sr.nextBytes(salt);

            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = f.generateSecret(spec).getEncoded();

            String sSalt = Base64.getEncoder().encodeToString(salt);
            String sHash = Base64.getEncoder().encodeToString(hash);
            return ITERATIONS + ":" + sSalt + ":" + sHash;
        } catch (Exception e) {
            throw new RuntimeException("Failed to hash password", e);
        }
    }

    public static boolean verify(String password, String stored) {
        try {
            String[] parts = stored.split(":");
            int iterations = Integer.parseInt(parts[0]);
            byte[] salt = Base64.getDecoder().decode(parts[1]);
            byte[] hash = Base64.getDecoder().decode(parts[2]);

            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, hash.length * 8);
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] testHash = f.generateSecret(spec).getEncoded();

            return java.security.MessageDigest.isEqual(hash, testHash);
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify password", e);
        }
    }
}
