package ui;

import java.awt.*;
import javax.swing.*;
import service.AuthService;

public class ForgotPasswordFrame extends JFrame {

    public ForgotPasswordFrame() {

        setTitle("School Management System - Reset Password");
        setSize(400, 450);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // ===== DARK THEME =====
        Color bgColor = new Color(30, 30, 30);
        Color fieldBg = new Color(45, 45, 45);
        Color textColor = new Color(220, 220, 220);
        Color accent = new Color(33, 150, 243);

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 13);
        Font titleFont = new Font("Segoe UI", Font.BOLD, 18);

        JPanel panel = new JPanel();
        panel.setBackground(bgColor);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // ===== TITLE =====
        JLabel title = new JLabel("Reset Password");
        title.setFont(titleFont);
        title.setForeground(accent);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));

        // ===== INPUT FIELDS =====
        JTextField usernameField = createField(fieldBg, textColor);
        JTextField emailField = createField(fieldBg, textColor);
        JPasswordField newPassField = createPasswordField(fieldBg, textColor);
        JPasswordField confirmPassField = createPasswordField(fieldBg, textColor);

        // ===== BUTTONS =====
        JButton resetBtn = createButton("Reset Password", accent);
        JButton backBtn = createButton("Back to Login", new Color(80, 80, 80));

        // ===== ADD COMPONENTS =====
        addField(panel, "Username", usernameField, labelFont, textColor);
        addField(panel, "Email", emailField, labelFont, textColor);
        addField(panel, "New Password", newPassField, labelFont, textColor);
        addField(panel, "Confirm Password", confirmPassField, labelFont, textColor);

        panel.add(Box.createVerticalStrut(20));
        panel.add(resetBtn);
        panel.add(Box.createVerticalStrut(8));
        panel.add(backBtn);

        add(panel);
        setVisible(true);

        // ===== RESET LOGIC =====
        resetBtn.addActionListener(e -> {

            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String newPass = new String(newPassField.getPassword());
            String confirmPass = new String(confirmPassField.getPassword());

            if (username.isEmpty() || email.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "All fields must be filled.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!newPass.equals(confirmPass)) {
                JOptionPane.showMessageDialog(this,
                        "Passwords do not match.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            AuthService authService = new AuthService();
            boolean success = authService.resetPassword(username, email, newPass);

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Password reset successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid username or email.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        backBtn.addActionListener(e -> dispose());
    }

    // ===== HELPER METHODS =====
    private JTextField createField(Color bg, Color fg) {
        JTextField field = new JTextField();
        field.setBackground(bg);
        field.setForeground(fg);
        field.setCaretColor(fg);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        field.setDisabledTextColor(new Color(130, 130, 130));
        return field;
    }

    private JPasswordField createPasswordField(Color bg, Color fg) {
        JPasswordField field = new JPasswordField();
        field.setBackground(bg);
        field.setForeground(fg);
        field.setCaretColor(fg);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        field.setDisabledTextColor(new Color(130, 130, 130));
        return field;
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }

    private void addField(JPanel panel, String labelText,
                          JComponent field, Font font, Color color) {
        JLabel label = new JLabel(labelText);
        label.setFont(font);
        label.setForeground(color);
        panel.add(label);
        panel.add(field);
        panel.add(Box.createVerticalStrut(10));
    }
}