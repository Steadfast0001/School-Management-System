package ui;

import java.awt.*;
import javax.swing.*;
import model.User;
import service.AuthService;

public class LoginFrame extends JFrame {

    public LoginFrame() {

        setTitle("School Management System | Login");
        setSize(420, 360);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Colors
        Color bgDark = new Color(18, 18, 18);
        Color panelDark = new Color(30, 30, 30);
        Color fieldDark = new Color(43, 43, 43);
        Color textLight = new Color(234, 234, 234);
        Color textMuted = new Color(158, 158, 158);
        Color primaryBlue = new Color(13, 110, 253);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(bgDark);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Header
        JLabel titleLabel = new JLabel("School Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(textLight);

        JLabel subtitleLabel = new JLabel("Login to your account", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(textMuted);

        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setBackground(bgDark);
        headerPanel.add(titleLabel);
        headerPanel.add(subtitleLabel);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(panelDark);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username");
        userLabel.setForeground(textLight);

        JTextField userField = new JTextField(15);
        userField.setBackground(fieldDark);
        userField.setForeground(textLight);
        userField.setCaretColor(textLight);
        userField.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JLabel passLabel = new JLabel("Password");
        passLabel.setForeground(textLight);

        JPasswordField passField = new JPasswordField(15);
        passField.setBackground(fieldDark);
        passField.setForeground(textLight);
        passField.setCaretColor(textLight);
        passField.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // Layout fields
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(userLabel, gbc);

        gbc.gridy = 1;
        formPanel.add(userField, gbc);

        gbc.gridy = 2;
        formPanel.add(passLabel, gbc);

        gbc.gridy = 3;
        formPanel.add(passField, gbc);

        // Buttons
        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(primaryBlue);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);

        JButton signupBtn = new JButton("Create Account");
        signupBtn.setBackground(panelDark);
        signupBtn.setForeground(textMuted);
        signupBtn.setBorderPainted(false);
        signupBtn.setFocusPainted(false);

        JButton forgotBtn = new JButton("Forgot Password?");
        forgotBtn.setBackground(panelDark);
        forgotBtn.setForeground(textMuted);
        forgotBtn.setBorderPainted(false);
        forgotBtn.setFocusPainted(false);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 0, 10));
        buttonPanel.setBackground(panelDark);
        buttonPanel.add(loginBtn);
        buttonPanel.add(signupBtn);
        buttonPanel.add(forgotBtn);

        gbc.gridy = 4;
        gbc.insets = new Insets(15, 5, 5, 5);
        formPanel.add(buttonPanel, gbc);

        // Assemble
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);

        setVisible(true);

        // Login action
        loginBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please fill in all fields",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            AuthService authService = new AuthService();
            User user = authService.login(username, password);

            if (user != null) {
                dispose();
                new DashboardFrame(user);
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Invalid username or password",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        signupBtn.addActionListener(e -> new SignUpFrame());

        forgotBtn.addActionListener(e -> new ForgotPasswordFrame());
    }
}
