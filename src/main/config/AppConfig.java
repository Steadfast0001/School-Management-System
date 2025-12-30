package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Application configuration manager
 * Handles all application settings including database, UI, and system properties
 */
public class AppConfig {

    // Configuration file paths
    private static final String DEFAULT_CONFIG_FILE = "config/app.properties";
    private static final String ENV_CONFIG_FILE = "APP_CONFIG_FILE";

    // Application settings
    private static String appName = "University Management System";
    private static String appVersion = "1.0.0";
    private static String appDescription = "Comprehensive university management solution";
    private static boolean debugMode = false;
    private static int sessionTimeout = 30; // minutes
    private static int maxLoginAttempts = 3;
    private static boolean enableNotifications = true;

    // UI settings
    private static String theme = "system";
    private static int defaultWindowWidth = 900;
    private static int defaultWindowHeight = 700;

    // Database settings (additional to DBConnection)
    private static int connectionPoolSize = 10;
    private static int connectionTimeout = 30000; // milliseconds
    private static boolean enableConnectionPooling = false;

    // Security settings
    private static int passwordMinLength = 8;
    private static boolean requireSpecialChars = true;
    private static boolean requireNumbers = true;
    private static int passwordHistorySize = 5;

    // Email settings (for notifications)
    private static String smtpHost = "localhost";
    private static int smtpPort = 587;
    private static String smtpUsername = "";
    private static String smtpPassword = "";
    private static boolean smtpUseTLS = true;

    // Logging settings
    private static String logLevel = "INFO";
    private static String logFile = "logs/app.log";
    private static int maxLogSize = 10; // MB
    private static int maxLogFiles = 5;

    static {
        loadConfiguration();
    }

    /**
     * Load application configuration
     */
    public static void loadConfiguration() {
        String configFile = System.getenv(ENV_CONFIG_FILE);
        if (configFile == null) {
            configFile = DEFAULT_CONFIG_FILE;
        }

        Properties props = new Properties();

        try (FileInputStream fis = new FileInputStream(configFile)) {
            props.load(fis);
            System.out.println("Application configuration loaded from: " + configFile);
        } catch (IOException e) {
            System.out.println("Application configuration file not found: " + configFile + ". Using defaults.");
        }

        // Load application settings
        appName = getProperty(props, "app.name", appName);
        appVersion = getProperty(props, "app.version", appVersion);
        appDescription = getProperty(props, "app.description", appDescription);
        debugMode = Boolean.parseBoolean(getProperty(props, "app.debug", String.valueOf(debugMode)));
        sessionTimeout = Integer.parseInt(getProperty(props, "app.session.timeout", String.valueOf(sessionTimeout)));
        maxLoginAttempts = Integer.parseInt(getProperty(props, "app.login.max.attempts", String.valueOf(maxLoginAttempts)));
        enableNotifications = Boolean.parseBoolean(getProperty(props, "app.notifications.enabled", String.valueOf(enableNotifications)));

        // Load UI settings
        theme = getProperty(props, "ui.theme", theme);
        defaultWindowWidth = Integer.parseInt(getProperty(props, "ui.window.width", String.valueOf(defaultWindowWidth)));
        defaultWindowHeight = Integer.parseInt(getProperty(props, "ui.window.height", String.valueOf(defaultWindowHeight)));

        // Load database settings
        connectionPoolSize = Integer.parseInt(getProperty(props, "db.pool.size", String.valueOf(connectionPoolSize)));
        connectionTimeout = Integer.parseInt(getProperty(props, "db.connection.timeout", String.valueOf(connectionTimeout)));
        enableConnectionPooling = Boolean.parseBoolean(getProperty(props, "db.pooling.enabled", String.valueOf(enableConnectionPooling)));

        // Load security settings
        passwordMinLength = Integer.parseInt(getProperty(props, "security.password.min.length", String.valueOf(passwordMinLength)));
        requireSpecialChars = Boolean.parseBoolean(getProperty(props, "security.password.require.special", String.valueOf(requireSpecialChars)));
        requireNumbers = Boolean.parseBoolean(getProperty(props, "security.password.require.numbers", String.valueOf(requireNumbers)));
        passwordHistorySize = Integer.parseInt(getProperty(props, "security.password.history.size", String.valueOf(passwordHistorySize)));

        // Load email settings
        smtpHost = getProperty(props, "email.smtp.host", smtpHost);
        smtpPort = Integer.parseInt(getProperty(props, "email.smtp.port", String.valueOf(smtpPort)));
        smtpUsername = getProperty(props, "email.smtp.username", smtpUsername);
        smtpPassword = getProperty(props, "email.smtp.password", smtpPassword);
        smtpUseTLS = Boolean.parseBoolean(getProperty(props, "email.smtp.tls", String.valueOf(smtpUseTLS)));

        // Load logging settings
        logLevel = getProperty(props, "logging.level", logLevel);
        logFile = getProperty(props, "logging.file", logFile);
        maxLogSize = Integer.parseInt(getProperty(props, "logging.max.size.mb", String.valueOf(maxLogSize)));
        maxLogFiles = Integer.parseInt(getProperty(props, "logging.max.files", String.valueOf(maxLogFiles)));
    }

    /**
     * Get property value with environment variable override
     */
    private static String getProperty(Properties props, String key, String defaultValue) {
        // Check environment variable first (APP_[KEY] format)
        String envKey = "APP_" + key.toUpperCase().replace(".", "_");
        String value = System.getenv(envKey);
        if (value != null && !value.trim().isEmpty()) {
            return value.trim();
        }

        // Check properties file
        value = props.getProperty(key);
        if (value != null && !value.trim().isEmpty()) {
            return value.trim();
        }

        return defaultValue;
    }

    // Getters for all configuration values

    // Application settings
    public static String getAppName() { return appName; }
    public static String getAppVersion() { return appVersion; }
    public static String getAppDescription() { return appDescription; }
    public static boolean isDebugMode() { return debugMode; }
    public static int getSessionTimeout() { return sessionTimeout; }
    public static int getMaxLoginAttempts() { return maxLoginAttempts; }
    public static boolean isNotificationsEnabled() { return enableNotifications; }

    // UI settings
    public static String getTheme() { return theme; }
    public static int getDefaultWindowWidth() { return defaultWindowWidth; }
    public static int getDefaultWindowHeight() { return defaultWindowHeight; }

    // Database settings
    public static int getConnectionPoolSize() { return connectionPoolSize; }
    public static int getConnectionTimeout() { return connectionTimeout; }
    public static boolean isConnectionPoolingEnabled() { return enableConnectionPooling; }

    // Security settings
    public static int getPasswordMinLength() { return passwordMinLength; }
    public static boolean isRequireSpecialChars() { return requireSpecialChars; }
    public static boolean isRequireNumbers() { return requireNumbers; }
    public static int getPasswordHistorySize() { return passwordHistorySize; }

    // Email settings
    public static String getSmtpHost() { return smtpHost; }
    public static int getSmtpPort() { return smtpPort; }
    public static String getSmtpUsername() { return smtpUsername; }
    public static String getSmtpPassword() { return smtpPassword; }
    public static boolean isSmtpUseTLS() { return smtpUseTLS; }

    // Logging settings
    public static String getLogLevel() { return logLevel; }
    public static String getLogFile() { return logFile; }
    public static int getMaxLogSize() { return maxLogSize; }
    public static int getMaxLogFiles() { return maxLogFiles; }

    /**
     * Print current configuration (for debugging)
     */
    public static void printConfiguration() {
        System.out.println("=== Application Configuration ===");
        System.out.println("App Name: " + appName);
        System.out.println("Version: " + appVersion);
        System.out.println("Debug Mode: " + debugMode);
        System.out.println("Session Timeout: " + sessionTimeout + " minutes");
        System.out.println("Max Login Attempts: " + maxLoginAttempts);
        System.out.println("Notifications: " + enableNotifications);
        System.out.println("Theme: " + theme);
        System.out.println("Window Size: " + defaultWindowWidth + "x" + defaultWindowHeight);
        System.out.println("Connection Pool Size: " + connectionPoolSize);
        System.out.println("Connection Timeout: " + connectionTimeout + "ms");
        System.out.println("Password Min Length: " + passwordMinLength);
        System.out.println("Require Special Chars: " + requireSpecialChars);
        System.out.println("Require Numbers: " + requireNumbers);
        System.out.println("SMTP Host: " + smtpHost + ":" + smtpPort);
        System.out.println("Log Level: " + logLevel);
        System.out.println("Log File: " + logFile);
    }
}