package ui;

import dao.*;
import model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CourseAssignmentFrame extends JFrame {
    private User currentUser;
    private CourseDAO courseDAO = new CourseDAO();
    private TeacherDAO teacherDAO = new TeacherDAO();
    private TimetableDAO timetableDAO = new TimetableDAO();
    private JTable assignmentTable;
    private DefaultTableModel tableModel;

    public CourseAssignmentFrame(User user) {
        this.currentUser = user;

        setTitle("Course Assignment Management");
        setSize(900, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        loadAssignments();

        setVisible(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Table for current assignments
        tableModel = new DefaultTableModel(new String[]{"Course Code", "Course Name", "Teacher", "Day", "Time", "Room"}, 0);
        assignmentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(assignmentTable);

        // Control panel
        JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBorder(BorderFactory.createTitledBorder("Assignment Controls"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        controlPanel.add(new JLabel("Course:"), gbc);
        gbc.gridx = 1;
        JComboBox<Course> courseCombo = new JComboBox<>();
        controlPanel.add(courseCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        controlPanel.add(new JLabel("Teacher:"), gbc);
        gbc.gridx = 1;
        JComboBox<Teacher> teacherCombo = new JComboBox<>();
        controlPanel.add(teacherCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        controlPanel.add(new JLabel("Day:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> dayCombo = new JComboBox<>(new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"});
        controlPanel.add(dayCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        controlPanel.add(new JLabel("Start Time:"), gbc);
        gbc.gridx = 1;
        JTextField startTimeField = new JTextField("09:00", 10);
        controlPanel.add(startTimeField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        controlPanel.add(new JLabel("End Time:"), gbc);
        gbc.gridx = 1;
        JTextField endTimeField = new JTextField("10:30", 10);
        controlPanel.add(endTimeField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        controlPanel.add(new JLabel("Room:"), gbc);
        gbc.gridx = 1;
        JTextField roomField = new JTextField("Room 101", 10);
        controlPanel.add(roomField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton assignBtn = new JButton("Assign Course");
        JButton updateBtn = new JButton("Update Assignment");
        JButton removeBtn = new JButton("Remove Assignment");
        JButton refreshBtn = new JButton("Refresh");

        buttonPanel.add(assignBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(removeBtn);
        buttonPanel.add(refreshBtn);

        // Load data
        loadCourses(courseCombo);
        loadTeachers(teacherCombo);

        // Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(controlPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Button actions
        assignBtn.addActionListener(e -> assignCourse(courseCombo, teacherCombo, dayCombo, startTimeField, endTimeField, roomField));
        updateBtn.addActionListener(e -> updateAssignment());
        removeBtn.addActionListener(e -> removeAssignment());
        refreshBtn.addActionListener(e -> loadAssignments());
    }

    private void loadCourses(JComboBox<Course> combo) {
        List<Course> courses = courseDAO.getAllCourses();
        combo.removeAllItems();
        for (Course course : courses) {
            combo.addItem(course);
        }
    }

    private void loadTeachers(JComboBox<Teacher> combo) {
        List<Teacher> teachers = teacherDAO.getAllTeachers();
        combo.removeAllItems();
        for (Teacher teacher : teachers) {
            combo.addItem(teacher);
        }
    }

    private void loadAssignments() {
        List<Timetable> timetables = timetableDAO.getAllTimetables();
        tableModel.setRowCount(0);

        for (Timetable t : timetables) {
            tableModel.addRow(new Object[]{
                t.getCourseId(), // This should be course code, but we have ID
                t.getCourseName(),
                t.getInstructor(),
                t.getDayOfWeek(),
                t.getStartTime() + " - " + t.getEndTime(),
                t.getRoom()
            });
        }
    }

    private void assignCourse(JComboBox<Course> courseCombo, JComboBox<Teacher> teacherCombo,
                            JComboBox<String> dayCombo, JTextField startTime, JTextField endTime, JTextField room) {
        Course selectedCourse = (Course) courseCombo.getSelectedItem();
        Teacher selectedTeacher = (Teacher) teacherCombo.getSelectedItem();
        String day = (String) dayCombo.getSelectedItem();

        if (selectedCourse == null || selectedTeacher == null) {
            JOptionPane.showMessageDialog(this, "Please select both course and teacher");
            return;
        }

        Timetable timetable = new Timetable();
        timetable.setCourseId(selectedCourse.getId());
        timetable.setCourseName(selectedCourse.getCourseName());
        timetable.setDayOfWeek(day);
        timetable.setStartTime(startTime.getText());
        timetable.setEndTime(endTime.getText());
        timetable.setRoom(room.getText());
        timetable.setInstructor(selectedTeacher.getFirstName() + " " + selectedTeacher.getLastName());

        if (timetableDAO.addTimetable(timetable)) {
            JOptionPane.showMessageDialog(this, "Course assigned successfully!");
            loadAssignments();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to assign course");
        }
    }

    private void updateAssignment() {
        int selectedRow = assignmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an assignment to update");
            return;
        }

        // Implementation for updating assignment
        JOptionPane.showMessageDialog(this, "Update functionality coming soon");
    }

    private void removeAssignment() {
        int selectedRow = assignmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an assignment to remove");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this assignment?",
                                                   "Confirm Removal", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // Get timetable ID and remove
            // This would need to be implemented properly
            JOptionPane.showMessageDialog(this, "Remove functionality coming soon");
        }
    }
}