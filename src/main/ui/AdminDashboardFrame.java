package ui;

import java.awt.*;
import javax.swing.*;
import model.User;
import service.AuthService;

public class AdminDashboardFrame extends JFrame {

    private User currentUser;
    private AuthService authService = new AuthService();

    public AdminDashboardFrame(User user) {
        this.currentUser = user;

        if (!authService.isAdmin(currentUser)) {
            JOptionPane.showMessageDialog(this, "Access denied", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        setTitle("Admin Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton manageUsersBtn = new JButton("Manage Users");
        JButton manageStudentsBtn = new JButton("Manage Students");
        JButton manageTeachersBtn = new JButton("Manage Teachers");
        JButton viewReportsBtn = new JButton("View Reports");
        JButton systemSettingsBtn = new JButton("System Settings");
        JButton backBtn = new JButton("Back");

        panel.add(manageUsersBtn);
        panel.add(manageStudentsBtn);
        panel.add(manageTeachersBtn);
        panel.add(viewReportsBtn);
        panel.add(systemSettingsBtn);
        panel.add(backBtn);

        add(panel, BorderLayout.CENTER);

        manageUsersBtn.addActionListener(e -> new UserManagementFrame(currentUser));
        manageStudentsBtn.addActionListener(e -> new StudentFrame());
        manageTeachersBtn.addActionListener(e -> new TeacherFrame());
        viewReportsBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Reports feature coming soon"));
        systemSettingsBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Settings feature coming soon"));
        backBtn.addActionListener(e -> dispose());

        setVisible(true);
    }
}