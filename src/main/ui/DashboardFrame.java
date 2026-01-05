package ui;

import java.awt.*;
import javax.swing.*;
import model.User;
import service.AuthService;

public class DashboardFrame extends JFrame {

    private User currentUser;
    private AuthService authService = new AuthService();

    public DashboardFrame(User user) {
        this.currentUser = user;

        setTitle("University Management System - Dashboard");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Welcome to University Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JLabel userLabel = new JLabel("Logged in as: " + user.getName() + " (" + user.getRole() + ")", JLabel.RIGHT);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userLabel.setForeground(Color.WHITE);

        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(userLabel, BorderLayout.SOUTH);

        // Menu panel
        JPanel menuPanel = new JPanel(new GridLayout(4, 4, 15, 15));
        menuPanel.setBorder(BorderFactory.createTitledBorder("Main Menu"));
        menuPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Main Menu"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Common buttons
        JButton studentBtn = createStyledButton("Students", "Manage student records");
        JButton teacherBtn = createStyledButton("Teachers", "Manage faculty members");
        JButton attendanceBtn = createStyledButton("Attendance", "Track attendance");
        JButton resultBtn = createStyledButton("Results", "View academic results");
        JButton courseBtn = createStyledButton("Courses", "Manage courses");
        JButton enrollmentBtn = createStyledButton("Enrollments", "Student enrollments");
        JButton departmentBtn = createStyledButton("Departments", "University departments");
        JButton timetableBtn = createStyledButton("Timetable", "Class schedules");
        JButton libraryBtn = createStyledButton("Library", "Library management");
        JButton feeBtn = createStyledButton("Fees", "Fee management");
        JButton reportBtn = createStyledButton("Reports", "Generate reports");
        JButton announcementBtn = createStyledButton("Announcements", "System announcements");

        // Add buttons based on role
        if (authService.isAdmin(currentUser)) {
            // Admin gets all buttons
            menuPanel.add(studentBtn);
            menuPanel.add(teacherBtn);
            menuPanel.add(attendanceBtn);
            menuPanel.add(resultBtn);
            menuPanel.add(courseBtn);
            menuPanel.add(enrollmentBtn);
            menuPanel.add(departmentBtn);
            menuPanel.add(timetableBtn);
            menuPanel.add(libraryBtn);
            menuPanel.add(feeBtn);
            menuPanel.add(reportBtn);
            menuPanel.add(announcementBtn);

            JButton userMgmtBtn = createStyledButton("User Management", "Manage system users");
            JButton adminDashboardBtn = createStyledButton("Admin Panel", "Administrative controls");
            menuPanel.add(userMgmtBtn);
            menuPanel.add(adminDashboardBtn);

            userMgmtBtn.addActionListener(e -> new UserManagementFrame(currentUser));
            adminDashboardBtn.addActionListener(e -> new AdminDashboardFrame(currentUser));
        } else if (currentUser.getRole() != null && currentUser.getRole().equalsIgnoreCase("Student")) {
            // Students get only student panel
            menuPanel.add(studentBtn);
        } else if (currentUser.getRole() != null && currentUser.getRole().equalsIgnoreCase("Teacher")) {
            // Teachers get only teacher panel
            menuPanel.add(teacherBtn);
        }

        // Footer
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutBtn = createStyledButton("Logout", "Exit the system");
        footerPanel.add(logoutBtn);

        // Add panels to main
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(menuPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Button actions
        studentBtn.addActionListener(e -> openStudentManagement());
        teacherBtn.addActionListener(e -> openTeacherManagement());
        attendanceBtn.addActionListener(e -> new AttendanceFrame());
        resultBtn.addActionListener(e -> new ResultFrame());
        courseBtn.addActionListener(e -> new CourseFrame(currentUser));
        enrollmentBtn.addActionListener(e -> new EnrollmentFrame(currentUser));
        departmentBtn.addActionListener(e -> new DepartmentFrame(currentUser));
        timetableBtn.addActionListener(e -> new TimetableFrame(currentUser));
        libraryBtn.addActionListener(e -> new LibraryFrame(currentUser));
        feeBtn.addActionListener(e -> new FeeFrame(currentUser));
        reportBtn.addActionListener(e -> new ReportFrame(currentUser));
        announcementBtn.addActionListener(e -> new AnnouncementFrame(currentUser));
        logoutBtn.addActionListener(e -> logout());

        setVisible(true);
    }

    private JButton createStyledButton(String text, String tooltip) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(new Color(100, 149, 237));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setToolTipText(tooltip);
        button.setPreferredSize(new Dimension(120, 60));
        return button;
    }

    private void openStudentManagement() {
        if (authService.isAdmin(currentUser) || (currentUser.getRole() != null && currentUser.getRole().equalsIgnoreCase("Student"))) {
            new StudentFrame(currentUser);
        } else {
            JOptionPane.showMessageDialog(this, "Access denied", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openTeacherManagement() {
        if (authService.isAdmin(currentUser) || (currentUser.getRole() != null && currentUser.getRole().equalsIgnoreCase("Teacher"))) {
            new TeacherFrame(currentUser);
        } else {
            JOptionPane.showMessageDialog(this, "Access denied", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logout() {
        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame();
        }
    }
}
