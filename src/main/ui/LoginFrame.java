package ui;

import javax.swing.*;
import model.User;
import service.AuthService;

public class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("School Management System - Login");
        setSize(400, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        JTextField userField = new JTextField(15);
        JPasswordField passField = new JPasswordField(15);
        JButton loginBtn = new JButton("Login");
        JButton signupBtn = new JButton("Sign Up");

        panel.add(new JLabel("Username"));
        panel.add(userField);
        panel.add(new JLabel("Password"));
        panel.add(passField);
        panel.add(loginBtn);
        panel.add(signupBtn);

        add(panel);
        setVisible(true);

        // Add action listener for login button
        loginBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());

            AuthService authService = new AuthService();
            User user = authService.login(username, password);

            if (user != null) {
                // Login successful, open dashboard
                dispose(); // Close login window
                new DashboardFrame(user);
            } else {
                // Login failed, show error
                JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add action listener for sign up button
        signupBtn.addActionListener(e -> {
            new SignUpFrame();
        });
    }
}
