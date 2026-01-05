package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Database connection manager with configurable credentials
 * Supports environment variables and properties file
 */
public final class DBConnection {

    // Default values for development
    private static final String DEFAULT_URL = "jdbc:postgresql://localhost:5432/school_db";
    private static final String DEFAULT_USER = "postgres";
    private static final String DEFAULT_PASSWORD = "SLIM.V.";

    // Environment variable names
    private static final String ENV_DB_URL = "DB_URL";
    private static final String ENV_DB_USER = "DB_USER";
    private static final String ENV_DB_PASSWORD = "DB_PASSWORD";
    private static final String ENV_DB_CONFIG_FILE = "DB_CONFIG_FILE";

    // Properties file path (can be overridden by environment variable)
    private static final String DEFAULT_CONFIG_FILE = "config/database.properties";

    // Cached configuration
    private static String dbUrl;
    private static String dbUser;
    private static String dbPassword;

    // Prevent object creation
    private DBConnection() {}

    /**
     * Get database connection using configured credentials
     */
    public static Connection getConnection() {
        try {
            // Load configuration if not already loaded
            if (dbUrl == null) {
                loadConfiguration();
            }

            // Optional for newer JDBC, but safe
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL JDBC Driver not found.", e);
        } catch (SQLException e) {
            throw new RuntimeException("Database connection failed. Please check your database configuration.", e);
        }
    }

    /**
     * Load database configuration from environment variables or properties file
     */
    private static void loadConfiguration() {
        // First, try to load from properties file
        String configFile = System.getenv(ENV_DB_CONFIG_FILE);
        if (configFile == null) {
            configFile = DEFAULT_CONFIG_FILE;
        }

        Properties props = new Properties();
        boolean configLoaded = false;

        try (FileInputStream fis = new FileInputStream(configFile)) {
            props.load(fis);
            configLoaded = true;
        } catch (IOException e) {
            // Properties file not found or not readable, will use environment variables
            System.out.println("Configuration file not found: " + configFile + ". Using environment variables.");
        }

        // Load configuration values (environment variables take precedence over properties file)
        dbUrl = getConfigValue(ENV_DB_URL, "db.url", props, DEFAULT_URL);
        dbUser = getConfigValue(ENV_DB_USER, "db.user", props, DEFAULT_USER);
        dbPassword = getConfigValue(ENV_DB_PASSWORD, "db.password", props, DEFAULT_PASSWORD);

        if (configLoaded) {
            System.out.println("Database configuration loaded from: " + configFile);
        } else {
            System.out.println("Database configuration loaded from environment variables");
        }
    }

    /**
     * Get configuration value with priority: Environment Variable > Properties File > Default
     */
    private static String getConfigValue(String envVar, String propKey, Properties props, String defaultValue) {
        // Check environment variable first
        String value = System.getenv(envVar);
        if (value != null && !value.trim().isEmpty()) {
            return value.trim();
        }

        // Check properties file
        value = props.getProperty(propKey);
        if (value != null && !value.trim().isEmpty()) {
            return value.trim();
        }

        // Return default
        return defaultValue;
    }

    /**
     * Get current database URL (for debugging/logging purposes)
     */
    public static String getDatabaseUrl() {
        if (dbUrl == null) {
            loadConfiguration();
        }
        return dbUrl;
    }

    /**
     * Get current database user (for debugging/logging purposes)
     */
    public static String getDatabaseUser() {
        if (dbUser == null) {
            loadConfiguration();
        }
        return dbUser;
    }
}
