package ui;

import java.awt.*;
import javax.swing.*;
import service.AuthService;

public class SignUpFrame extends JFrame {

    public SignUpFrame() {

        setTitle("School Management System - Sign Up");
        setSize(400, 520);
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
        JLabel title = new JLabel("User Registration");
        title.setFont(titleFont);
        title.setForeground(accent);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));

        // ===== INPUT FIELDS =====
        JTextField usernameField = createField(fieldBg, textColor);
        JPasswordField passwordField = createPasswordField(fieldBg, textColor);
        JTextField nameField = createField(fieldBg, textColor);
        JTextField emailField = createField(fieldBg, textColor);
        JTextField matriculeField = createField(fieldBg, textColor);
        JTextField levelField = createField(fieldBg, textColor);

        JComboBox<String> roleCombo =
                new JComboBox<>(new String[]{"Student", "Teacher"});
        roleCombo.setBackground(fieldBg);
        roleCombo.setForeground(textColor);
        roleCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // ===== BUTTONS =====
        JButton signupBtn = createButton("Sign Up", accent);
        JButton backBtn = createButton("Back to Login", new Color(80, 80, 80));

        // ===== ADD COMPONENTS =====
        addField(panel, "Username", usernameField, labelFont, textColor);
        addField(panel, "Password", passwordField, labelFont, textColor);
        addField(panel, "Full Name", nameField, labelFont, textColor);
        addField(panel, "Email", emailField, labelFont, textColor);
        addField(panel, "Matricule", matriculeField, labelFont, textColor);
        addField(panel, "Level / Class", levelField, labelFont, textColor);

        JLabel roleLabel = new JLabel("Register As");
        roleLabel.setFont(labelFont);
        roleLabel.setForeground(textColor);
        panel.add(roleLabel);
        panel.add(roleCombo);

        panel.add(Box.createVerticalStrut(20));
        panel.add(signupBtn);
        panel.add(Box.createVerticalStrut(8));
        panel.add(backBtn);

        add(panel);
        setVisible(true);

        // ===== ROLE-BASED FIELD CONTROL (✅ FIXED) =====
        roleCombo.addActionListener(e -> {
            boolean isStudent =
                    roleCombo.getSelectedItem().toString().equalsIgnoreCase("Student");

            matriculeField.setEnabled(isStudent);
            levelField.setEnabled(isStudent);

            if (!isStudent) {
                matriculeField.setText("");
                levelField.setText("");
            }
        });

        // ===== DEFAULT STATE =====
        roleCombo.setSelectedIndex(0);
        matriculeField.setEnabled(true);
        levelField.setEnabled(true);

        // ===== SIGNUP LOGIC =====
        signupBtn.addActionListener(e -> {

            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String matricule = matriculeField.getText().trim();
            String level = levelField.getText().trim();
            String role = roleCombo.getSelectedItem().toString();

            if (username.isEmpty() || password.isEmpty()
                    || name.isEmpty() || email.isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "All required fields must be filled.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            AuthService authService = new AuthService();
            boolean success = authService.register(
                    username, password, name, email,
                    matricule, level, role
            );

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Account created successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Registration failed. Username or email already exists.",
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
