package ui;

import javax.swing.*;
import service.AuthService;
import java.awt.*;

public class SignUpFrame extends JFrame {

    public SignUpFrame() {

        setTitle("School Management System - Sign Up");
        setSize(420, 480);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JTextField nameField = new JTextField(15);
        JTextField emailField = new JTextField(15);
        JTextField matriculeField = new JTextField(15);
        JTextField levelField = new JTextField(15);

        // ✅ ROLE SELECTION (ONLY STUDENT / TEACHER)
        JComboBox<String> roleComboBox =
                new JComboBox<>(new String[]{"Student", "Teacher"});

        JButton signupBtn = new JButton("Sign Up");
        JButton backBtn = new JButton("Back to Login");

        // Add components
        panel.add(new JLabel("Username"));
        panel.add(usernameField);

        panel.add(Box.createVerticalStrut(8));
        panel.add(new JLabel("Password"));
        panel.add(passwordField);

        panel.add(Box.createVerticalStrut(8));
        panel.add(new JLabel("Full Name"));
        panel.add(nameField);

        panel.add(Box.createVerticalStrut(8));
        panel.add(new JLabel("Email"));
        panel.add(emailField);

        panel.add(Box.createVerticalStrut(8));
        panel.add(new JLabel("Matricule"));
        panel.add(matriculeField);

        panel.add(Box.createVerticalStrut(8));
        panel.add(new JLabel("Level / Class"));
        panel.add(levelField);

        panel.add(Box.createVerticalStrut(8));
        panel.add(new JLabel("Register As"));
        panel.add(roleComboBox);

        panel.add(Box.createVerticalStrut(15));
        panel.add(signupBtn);
        panel.add(Box.createVerticalStrut(5));
        panel.add(backBtn);

        add(panel);
        setVisible(true);

        // 🔐 SIGNUP LOGIC
        signupBtn.addActionListener(e -> {

            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String matricule = matriculeField.getText().trim();
            String level = levelField.getText().trim();
            String role = roleComboBox.getSelectedItem().toString();

            // Basic validation
            if (username.isEmpty() || password.isEmpty() ||
                name.isEmpty() || email.isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Please fill in all required fields.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            AuthService authService = new AuthService();

            boolean success = authService.register(
                    username,
                    password,
                    name,
                    email,
                    matricule,
                    level,
                    role // ✅ ONLY STUDENT OR TEACHER
            );

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Registration successful! Please login.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Registration failed. Username or email may already exist.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        backBtn.addActionListener(e -> dispose());
    }
}
