package ui;

import javax.swing.*;
import service.AuthService;

public class SignUpFrame extends JFrame {

    public SignUpFrame() {
        setTitle("School Management System - Sign Up");
        setSize(400, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JTextField nameField = new JTextField(15);
        JTextField emailField = new JTextField(15);
        JTextField matriculeField = new JTextField(15);
        JTextField levelField = new JTextField(15);
        JButton signupBtn = new JButton("Sign Up");
        JButton backBtn = new JButton("Back to Login");

        panel.add(new JLabel("Username"));
        panel.add(usernameField);
        panel.add(new JLabel("Password"));
        panel.add(passwordField);
        panel.add(new JLabel("Name"));
        panel.add(nameField);
        panel.add(new JLabel("Email"));
        panel.add(emailField);
        panel.add(new JLabel("Matricule"));
        panel.add(matriculeField);
        panel.add(new JLabel("Level"));
        panel.add(levelField);
        panel.add(signupBtn);
        panel.add(backBtn);

        add(panel);
        setVisible(true);

        signupBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String matricule = matriculeField.getText().trim();
            String level = levelField.getText().trim();

            AuthService authService = new AuthService();
            boolean success = authService.register(username, password, name, email, matricule, level);

            if (success) {
                JOptionPane.showMessageDialog(this, "Registration successful! Please login.", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed. Username may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backBtn.addActionListener(e -> dispose());
    }
}