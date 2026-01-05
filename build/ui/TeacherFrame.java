package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import model.User;
import model.*;
import dao.*;
import service.*;

public class TeacherFrame extends JFrame {

    private User currentUser;
    private int teacherId;

    public TeacherFrame(User user) {
        this.currentUser = user;

        // Get teacher ID
        TeacherDAO teacherDAO = new TeacherDAO();
        Teacher teacher = teacherDAO.getTeacherByEmail(currentUser.getEmail());
        if (teacher != null) {
            this.teacherId = teacher.getId();
        } else {
            JOptionPane.showMessageDialog(this, "Teacher record not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if ("TEACHER".equalsIgnoreCase(user.getRole())) {
            buildTeacherDashboard();
        } else {
            buildManagementPanel();
        }
    }

    private void buildTeacherDashboard() {
        setTitle("Teacher Dashboard - " + currentUser.getName());
        setSize(900, 700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Tab 1: Assigned Courses
        tabbedPane.addTab("My Courses", createCoursesPanel());

        // Tab 2: Manage Attendance
        tabbedPane.addTab("Attendance", createAttendancePanel());

        // Tab 3: Manage Grades
        tabbedPane.addTab("Grades", createGradesPanel());

        // Tab 4: Timetable
        tabbedPane.addTab("Timetable", createTimetablePanel());

        // Tab 5: Announcements
        tabbedPane.addTab("Announcements", createAnnouncementsPanel());

        add(tabbedPane);
        setVisible(true);
    }

    private void buildManagementPanel() {
        setTitle("Teacher Management");
        setSize(700, 400);
        setLocationRelativeTo(null);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();

        formPanel.add(new JLabel("Teacher Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);

        // Buttons
        JButton addBtn = new JButton("Add Teacher");
        JButton deleteBtn = new JButton("Delete Teacher");

        JPanel btnPanel = new JPanel();
        btnPanel.add(addBtn);
        btnPanel.add(deleteBtn);

        // Table
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Name", "Email"}, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        // Dummy action
        addBtn.addActionListener(e -> {
            model.addRow(new Object[]{
                    model.getRowCount() + 1,
                    nameField.getText(),
                    emailField.getText()
            });
            nameField.setText("");
            emailField.setText("");
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) model.removeRow(row);
        });

        setVisible(true);
    }

    private JPanel createCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("My Assigned Courses"));

        TimetableDAO timetableDAO = new TimetableDAO();
        List<Timetable> timetables = timetableDAO.getTimetablesByInstructor(currentUser.getName());

        String[] columns = {"Course Name", "Day", "Time", "Room"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Timetable t : timetables) {
            model.addRow(new Object[]{
                t.getCourseName(),
                t.getDayOfWeek(),
                t.getStartTime() + " - " + t.getEndTime(),
                t.getRoom()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAttendancePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Manage Attendance"));

        // Top panel for course selection and date
        JPanel topPanel = new JPanel(new FlowLayout());

        TimetableDAO timetableDAO = new TimetableDAO();
        List<Timetable> timetables = timetableDAO.getTimetablesByInstructor(currentUser.getName());

        JComboBox<String> courseCombo = new JComboBox<>();
        for (Timetable t : timetables) {
            courseCombo.addItem(t.getCourseId() + " - " + t.getCourseName());
        }

        JTextField dateField = new JTextField(10);
        dateField.setText(java.time.LocalDate.now().toString());

        JButton loadBtn = new JButton("Load Students");
        topPanel.add(new JLabel("Course:"));
        topPanel.add(courseCombo);
        topPanel.add(new JLabel("Date:"));
        topPanel.add(dateField);
        topPanel.add(loadBtn);

        // Table for attendance
        String[] columns = {"Student ID", "Student Name", "Present", "Remarks"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 2 ? Boolean.class : String.class;
            }
        };
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Bottom panel for save
        JPanel bottomPanel = new JPanel();
        JButton saveBtn = new JButton("Save Attendance");
        bottomPanel.add(saveBtn);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        loadBtn.addActionListener(e -> {
            String selected = (String) courseCombo.getSelectedItem();
            if (selected != null) {
                int courseId = Integer.parseInt(selected.split(" - ")[0]);
                EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
                List<Enrollment> enrollments = enrollmentDAO.getEnrollmentsByCourse(courseId);

                model.setRowCount(0);
                for (Enrollment en : enrollments) {
                    model.addRow(new Object[]{en.getStudentId(), en.getStudentName(), true, ""});
                }
            }
        });

        saveBtn.addActionListener(e -> {
            AttendanceDAO attendanceDAO = new AttendanceDAO();
            for (int i = 0; i < model.getRowCount(); i++) {
                int studentId = (Integer) model.getValueAt(i, 0);
                boolean present = (Boolean) model.getValueAt(i, 2);
                String remarks = (String) model.getValueAt(i, 3);

                Attendance attendance = new Attendance();
                attendance.setStudentId(studentId);
                attendance.setDate(java.time.LocalDate.parse(dateField.getText()));
                attendance.setStatus(present ? "Present" : "Absent");
                attendance.setRemarks(remarks);

                attendanceDAO.markAttendance(attendance);
            }
            JOptionPane.showMessageDialog(panel, "Attendance saved successfully!");
        });

        return panel;
    }

    private JPanel createGradesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Manage Grades"));

        // Top panel for course selection
        JPanel topPanel = new JPanel(new FlowLayout());

        TimetableDAO timetableDAO = new TimetableDAO();
        List<Timetable> timetables = timetableDAO.getTimetablesByInstructor(currentUser.getName());

        JComboBox<String> courseCombo = new JComboBox<>();
        for (Timetable t : timetables) {
            courseCombo.addItem(t.getCourseId() + " - " + t.getCourseName());
        }

        JButton loadBtn = new JButton("Load Students");
        topPanel.add(new JLabel("Course:"));
        topPanel.add(courseCombo);
        topPanel.add(loadBtn);

        // Table for grades
        String[] columns = {"Student ID", "Student Name", "Grade"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Bottom panel for save
        JPanel bottomPanel = new JPanel();
        JButton saveBtn = new JButton("Save Grades");
        bottomPanel.add(saveBtn);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        loadBtn.addActionListener(e -> {
            String selected = (String) courseCombo.getSelectedItem();
            if (selected != null) {
                int courseId = Integer.parseInt(selected.split(" - ")[0]);
                EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
                List<Enrollment> enrollments = enrollmentDAO.getEnrollmentsByCourse(courseId);

                model.setRowCount(0);
                for (Enrollment en : enrollments) {
                    model.addRow(new Object[]{en.getStudentId(), en.getStudentName(), en.getGrade()});
                }
            }
        });

        saveBtn.addActionListener(e -> {
            EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
            String selected = (String) courseCombo.getSelectedItem();
            if (selected != null) {
                int courseId = Integer.parseInt(selected.split(" - ")[0]);

                for (int i = 0; i < model.getRowCount(); i++) {
                    int studentId = (Integer) model.getValueAt(i, 0);
                    Double grade = null;
                    Object gradeObj = model.getValueAt(i, 2);
                    if (gradeObj instanceof Double) {
                        grade = (Double) gradeObj;
                    } else if (gradeObj instanceof String && !((String) gradeObj).isEmpty()) {
                        try {
                            grade = Double.parseDouble((String) gradeObj);
                        } catch (NumberFormatException ex) {
                            // ignore
                        }
                    }

                    // Find enrollment and update
                    List<Enrollment> enrollments = enrollmentDAO.getEnrollmentsByCourse(courseId);
                    for (Enrollment en : enrollments) {
                        if (en.getStudentId() == studentId) {
                            en.setGrade(grade);
                            enrollmentDAO.updateEnrollment(en);
                            break;
                        }
                    }
                }
                JOptionPane.showMessageDialog(panel, "Grades saved successfully!");
            }
        });

        return panel;
    }

    private JPanel createTimetablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("My Timetable"));

        TimetableDAO timetableDAO = new TimetableDAO();
        List<Timetable> timetables = timetableDAO.getTimetablesByInstructor(currentUser.getName());

        String[] columns = {"Day", "Time", "Course", "Room"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Timetable t : timetables) {
            model.addRow(new Object[]{
                t.getDayOfWeek(),
                t.getStartTime() + " - " + t.getEndTime(),
                t.getCourseName(),
                t.getRoom()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAnnouncementsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Send Announcements"));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        JTextField titleField = new JTextField(20);
        formPanel.add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Content:"), gbc);
        gbc.gridx = 1;
        JTextArea contentArea = new JTextArea(5, 20);
        JScrollPane contentScroll = new JScrollPane(contentArea);
        formPanel.add(contentScroll, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Audience:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> audienceCombo = new JComboBox<>(new String[]{"ALL", "STUDENT", "TEACHER", "ADMIN"});
        formPanel.add(audienceCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JButton sendBtn = new JButton("Send Announcement");
        formPanel.add(sendBtn, gbc);

        panel.add(formPanel, BorderLayout.CENTER);

        sendBtn.addActionListener(e -> {
            String title = titleField.getText().trim();
            String content = contentArea.getText().trim();
            String audience = (String) audienceCombo.getSelectedItem();

            if (title.isEmpty() || content.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            AnnouncementDAO announcementDAO = new AnnouncementDAO();
            Announcement announcement = new Announcement();
            announcement.setTitle(title);
            announcement.setContent(content);
            announcement.setAuthor(currentUser.getName());
            announcement.setTargetAudience(audience);
            announcement.setPublishDate(java.time.LocalDate.now());
            announcement.setActive(true);

            if (announcementDAO.addAnnouncement(announcement)) {
                JOptionPane.showMessageDialog(panel, "Announcement sent successfully!");
                titleField.setText("");
                contentArea.setText("");
            } else {
                JOptionPane.showMessageDialog(panel, "Failed to send announcement.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }
}
