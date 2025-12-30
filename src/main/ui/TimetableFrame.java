package ui;

import dao.CourseDAO;
import dao.TimetableDAO;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Course;
import model.Timetable;
import model.User;

public class TimetableFrame extends JFrame {
    private User currentUser;
    private TimetableDAO timetableDAO = new TimetableDAO();
    private CourseDAO courseDAO = new CourseDAO();
    private JTable timetableTable;
    private DefaultTableModel tableModel;
    private JComboBox<Course> courseCombo;
    private JComboBox<String> dayCombo;
    private JTextField startTimeField, endTimeField, roomField, instructorField;

    public TimetableFrame(User user) {
        this.currentUser = user;

        setTitle("Timetable Management");
        setSize(900, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        loadTimetables();
        loadCourses();

        setVisible(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Timetable Entry"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Course:"), gbc);
        gbc.gridx = 1;
        courseCombo = new JComboBox<>();
        formPanel.add(courseCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Day:"), gbc);
        gbc.gridx = 1;
        dayCombo = new JComboBox<>(new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"});
        formPanel.add(dayCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Start Time:"), gbc);
        gbc.gridx = 1;
        startTimeField = new JTextField(20);
        formPanel.add(startTimeField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("End Time:"), gbc);
        gbc.gridx = 1;
        endTimeField = new JTextField(20);
        formPanel.add(endTimeField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Room:"), gbc);
        gbc.gridx = 1;
        roomField = new JTextField(20);
        formPanel.add(roomField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Instructor:"), gbc);
        gbc.gridx = 1;
        instructorField = new JTextField(20);
        formPanel.add(instructorField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addBtn = new JButton("Add Schedule");
        JButton updateBtn = new JButton("Update Schedule");
        JButton deleteBtn = new JButton("Delete Schedule");
        JButton clearBtn = new JButton("Clear");

        addBtn.addActionListener(e -> addTimetable());
        updateBtn.addActionListener(e -> updateTimetable());
        deleteBtn.addActionListener(e -> deleteTimetable());
        clearBtn.addActionListener(e -> clearForm());

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);

        // Table
        String[] columns = {"ID", "Course", "Day", "Start Time", "End Time", "Room", "Instructor"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        timetableTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(timetableTable);

        // Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
    }

    private void loadTimetables() {
        tableModel.setRowCount(0);
        List<Timetable> timetables = timetableDAO.getAllTimetables();
        for (Timetable tt : timetables) {
            tableModel.addRow(new Object[]{
                tt.getId(),
                tt.getCourseName(),
                tt.getDayOfWeek(),
                tt.getStartTime(),
                tt.getEndTime(),
                tt.getRoom(),
                tt.getInstructor()
            });
        }
    }

    private void loadCourses() {
        List<Course> courses = courseDAO.getAllCourses();
        for (Course course : courses) {
            courseCombo.addItem(course);
        }
    }

    private void addTimetable() {
        if (validateForm()) {
            Timetable tt = new Timetable();
            Course selectedCourse = (Course) courseCombo.getSelectedItem();
            tt.setCourseId(selectedCourse.getId());
            tt.setDayOfWeek((String) dayCombo.getSelectedItem());
            tt.setStartTime(startTimeField.getText().trim());
            tt.setEndTime(endTimeField.getText().trim());
            tt.setRoom(roomField.getText().trim());
            tt.setInstructor(instructorField.getText().trim());

            if (timetableDAO.addTimetable(tt)) {
                JOptionPane.showMessageDialog(this, "Schedule added successfully!");
                loadTimetables();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add schedule.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateTimetable() {
        JOptionPane.showMessageDialog(this, "Update functionality to be implemented.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteTimetable() {
        JOptionPane.showMessageDialog(this, "Delete functionality to be implemented.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearForm() {
        courseCombo.setSelectedIndex(0);
        dayCombo.setSelectedIndex(0);
        startTimeField.setText("");
        endTimeField.setText("");
        roomField.setText("");
        instructorField.setText("");
    }

    private boolean validateForm() {
        if (courseCombo.getSelectedItem() == null || startTimeField.getText().trim().isEmpty() ||
            endTimeField.getText().trim().isEmpty() || roomField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}