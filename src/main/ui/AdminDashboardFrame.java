package ui;

import java.awt.*;
import javax.swing.*;
import model.User;
import service.AuthService;
import dao.UserDAO;
import dao.StudentDAO;
import dao.TeacherDAO;

public class AdminDashboardFrame extends JFrame {

    private User currentUser;
    private AuthService authService = new AuthService();
    private UserDAO userDAO = new UserDAO();
    private StudentDAO studentDAO = new StudentDAO();
    private TeacherDAO teacherDAO = new TeacherDAO();

    public AdminDashboardFrame(User user) {
        this.currentUser = user;

        if (!authService.isAdmin(currentUser)) {
            JOptionPane.showMessageDialog(this, "Access denied", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        setTitle("Super Admin Control Panel");
        setSize(1000, 700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create tabbed pane for comprehensive admin control
        JTabbedPane tabbedPane = new JTabbedPane();

        // User Management Tab
        tabbedPane.addTab("User Management", createUserManagementTab());

        // Academic Management Tab
        tabbedPane.addTab("Academic Management", createAcademicManagementTab());

        // System Management Tab
        tabbedPane.addTab("System Management", createSystemManagementTab());

        // Reports & Analytics Tab
        tabbedPane.addTab("Reports & Analytics", createReportsTab());

        add(tabbedPane, BorderLayout.CENTER);

        // Footer with quick actions
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutBtn = new JButton("Logout");
        JButton backBtn = new JButton("Back to Dashboard");

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        backBtn.addActionListener(e -> dispose());

        footerPanel.add(backBtn);
        footerPanel.add(logoutBtn);
        add(footerPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createUserManagementTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Quick action buttons
        JPanel actionPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        actionPanel.setBorder(BorderFactory.createTitledBorder("Quick User Actions"));

        JButton manageUsersBtn = createStyledButton("Manage All Users", "Complete user CRUD operations");
        JButton manageStudentsBtn = createStyledButton("Student Records", "Manage student information");
        JButton manageTeachersBtn = createStyledButton("Teacher Records", "Manage faculty information");
        JButton assignCoursesBtn = createStyledButton("Assign Courses", "Assign courses to teachers/students");
        JButton bulkImportBtn = createStyledButton("Bulk Import", "Import users from CSV");
        JButton userPermissionsBtn = createStyledButton("User Permissions", "Manage roles and access");

        actionPanel.add(manageUsersBtn);
        actionPanel.add(manageStudentsBtn);
        actionPanel.add(manageTeachersBtn);
        actionPanel.add(assignCoursesBtn);
        actionPanel.add(bulkImportBtn);
        actionPanel.add(userPermissionsBtn);

        // Statistics panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        statsPanel.setBorder(BorderFactory.createTitledBorder("System Statistics"));

        // Real statistics from database
        int totalUsers = userDAO.getAllUsers().size();
        int activeStudents = studentDAO.getAllStudents().size();
        int facultyMembers = teacherDAO.getAllTeachers().size();
        int adminUsers = userDAO.getUserCountByRole("ADMIN");

        statsPanel.add(createStatPanel("Total Users", String.valueOf(totalUsers)));
        statsPanel.add(createStatPanel("Active Students", String.valueOf(activeStudents)));
        statsPanel.add(createStatPanel("Faculty Members", String.valueOf(facultyMembers)));
        statsPanel.add(createStatPanel("Admin Users", String.valueOf(adminUsers)));

        panel.add(actionPanel, BorderLayout.CENTER);
        panel.add(statsPanel, BorderLayout.SOUTH);

        // Button actions
        manageUsersBtn.addActionListener(e -> new UserManagementFrame(currentUser));
        manageStudentsBtn.addActionListener(e -> new StudentFrame(currentUser));
        manageTeachersBtn.addActionListener(e -> new TeacherFrame(currentUser));
        assignCoursesBtn.addActionListener(e -> new CourseAssignmentFrame(currentUser));
        bulkImportBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Bulk import feature coming soon"));
        userPermissionsBtn.addActionListener(e -> new UserPermissionsFrame(currentUser));

        return panel;
    }

    private JPanel createAcademicManagementTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel actionPanel = new JPanel(new GridLayout(3, 3, 10, 10));
        actionPanel.setBorder(BorderFactory.createTitledBorder("Academic Administration"));

        JButton coursesBtn = createStyledButton("Course Management", "Create and manage courses");
        JButton enrollmentsBtn = createStyledButton("Enrollments", "Manage student enrollments");
        JButton departmentsBtn = createStyledButton("Departments", "Manage academic departments");
        JButton timetableBtn = createStyledButton("Timetable", "Schedule management");
        JButton calendarBtn = createStyledButton("Academic Calendar", "Manage academic year");
        JButton curriculumBtn = createStyledButton("Curriculum", "Course curriculum planning");
        JButton assessmentsBtn = createStyledButton("Assessments", "Exam and grading setup");
        JButton attendanceBtn = createStyledButton("Attendance System", "Configure attendance policies");
        JButton transcriptsBtn = createStyledButton("Transcripts", "Generate academic records");

        actionPanel.add(coursesBtn);
        actionPanel.add(enrollmentsBtn);
        actionPanel.add(departmentsBtn);
        actionPanel.add(timetableBtn);
        actionPanel.add(calendarBtn);
        actionPanel.add(curriculumBtn);
        actionPanel.add(assessmentsBtn);
        actionPanel.add(attendanceBtn);
        actionPanel.add(transcriptsBtn);

        panel.add(actionPanel, BorderLayout.CENTER);

        // Button actions
        coursesBtn.addActionListener(e -> new CourseFrame(currentUser));
        enrollmentsBtn.addActionListener(e -> new EnrollmentFrame(currentUser));
        departmentsBtn.addActionListener(e -> new DepartmentFrame(currentUser));
        timetableBtn.addActionListener(e -> new TimetableFrame(currentUser));
        calendarBtn.addActionListener(e -> new AcademicCalendarFrame(currentUser));
        curriculumBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Curriculum planning feature coming soon"));
        assessmentsBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Assessment setup feature coming soon"));
        attendanceBtn.addActionListener(e -> new AttendanceFrame());
        transcriptsBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Transcript generation feature coming soon"));

        return panel;
    }

    private JPanel createSystemManagementTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel actionPanel = new JPanel(new GridLayout(3, 3, 10, 10));
        actionPanel.setBorder(BorderFactory.createTitledBorder("System Administration"));

        JButton feesBtn = createStyledButton("Fee Management", "Configure fee structures");
        JButton libraryBtn = createStyledButton("Library System", "Manage library resources");
        JButton announcementsBtn = createStyledButton("Announcements", "System-wide communications");
        JButton backupBtn = createStyledButton("Backup & Restore", "Database maintenance");
        JButton securityBtn = createStyledButton("Security Settings", "Access control and security");
        JButton integrationsBtn = createStyledButton("Integrations", "External system connections");
        JButton auditBtn = createStyledButton("Audit Logs", "System activity monitoring");
        JButton maintenanceBtn = createStyledButton("System Maintenance", "Performance optimization");
        JButton settingsBtn = createStyledButton("Global Settings", "System configuration");

        actionPanel.add(feesBtn);
        actionPanel.add(libraryBtn);
        actionPanel.add(announcementsBtn);
        actionPanel.add(backupBtn);
        actionPanel.add(securityBtn);
        actionPanel.add(integrationsBtn);
        actionPanel.add(auditBtn);
        actionPanel.add(maintenanceBtn);
        actionPanel.add(settingsBtn);

        panel.add(actionPanel, BorderLayout.CENTER);

        // Button actions
        feesBtn.addActionListener(e -> new FeeFrame(currentUser));
        libraryBtn.addActionListener(e -> new LibraryFrame(currentUser));
        announcementsBtn.addActionListener(e -> new AnnouncementFrame(currentUser));
        backupBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Backup & restore feature coming soon"));
        securityBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Security settings feature coming soon"));
        integrationsBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Integrations feature coming soon"));
        auditBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Audit logs feature coming soon"));
        maintenanceBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Maintenance tools feature coming soon"));
        settingsBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Global settings feature coming soon"));

        return panel;
    }

    private JPanel createReportsTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel actionPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        actionPanel.setBorder(BorderFactory.createTitledBorder("Reports & Analytics"));

        JButton studentReportsBtn = createStyledButton("Student Reports", "Academic performance reports");
        JButton teacherReportsBtn = createStyledButton("Teacher Reports", "Faculty workload reports");
        JButton enrollmentReportsBtn = createStyledButton("Enrollment Reports", "Course enrollment statistics");
        JButton financialReportsBtn = createStyledButton("Financial Reports", "Fee collection reports");
        JButton attendanceReportsBtn = createStyledButton("Attendance Reports", "Attendance analytics");
        JButton customReportsBtn = createStyledButton("Custom Reports", "Build custom queries");
        JButton exportBtn = createStyledButton("Export Data", "Export to various formats");
        JButton dashboardBtn = createStyledButton("Analytics Dashboard", "Visual analytics");

        actionPanel.add(studentReportsBtn);
        actionPanel.add(teacherReportsBtn);
        actionPanel.add(enrollmentReportsBtn);
        actionPanel.add(financialReportsBtn);
        actionPanel.add(attendanceReportsBtn);
        actionPanel.add(customReportsBtn);
        actionPanel.add(exportBtn);
        actionPanel.add(dashboardBtn);

        panel.add(actionPanel, BorderLayout.CENTER);

        // Button actions
        studentReportsBtn.addActionListener(e -> new ReportFrame(currentUser, "STUDENT"));
        teacherReportsBtn.addActionListener(e -> new ReportFrame(currentUser, "TEACHER"));
        enrollmentReportsBtn.addActionListener(e -> new ReportFrame(currentUser, "ENROLLMENT"));
        financialReportsBtn.addActionListener(e -> new ReportFrame(currentUser, "FINANCIAL"));
        attendanceReportsBtn.addActionListener(e -> new ReportFrame(currentUser, "ATTENDANCE"));
        customReportsBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Custom reports feature coming soon"));
        exportBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Data export feature coming soon"));
        dashboardBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Analytics dashboard feature coming soon"));

        return panel;
    }

    private JButton createStyledButton(String text, String tooltip) {
        JButton button = new JButton(text);
        button.setToolTipText(tooltip);
        button.setPreferredSize(new Dimension(120, 40));
        button.setFont(new Font("Arial", Font.PLAIN, 11));
        return button;
    }

    private JPanel createStatPanel(String title, String value) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));

        JLabel valueLabel = new JLabel(value, JLabel.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(Color.BLUE);

        panel.add(valueLabel, BorderLayout.CENTER);
        return panel;
    }
}