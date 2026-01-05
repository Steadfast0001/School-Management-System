package ui;

import dao.EnrollmentDAO;
import dao.StudentDAO;
import dao.CourseDAO;
import model.Enrollment;
import model.Student;
import model.Course;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class EnrollmentFrame extends JFrame {
    private User currentUser;
    private EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    private StudentDAO studentDAO = new StudentDAO();
    private CourseDAO courseDAO = new CourseDAO();
    private JTable enrollmentTable;
    private DefaultTableModel tableModel;
    private JComboBox<Student> studentCombo;
    private JComboBox<Course> courseCombo;
    private JComboBox<String> statusCombo;

    public EnrollmentFrame(User user) {
        this.currentUser = user;

        setTitle("Enrollment Management");
        setSize(900, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        loadEnrollments();
        loadStudents();
        loadCourses();

        setVisible(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Enrollment Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Student:"), gbc);
        gbc.gridx = 1;
        studentCombo = new JComboBox<>();
        formPanel.add(studentCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Course:"), gbc);
        gbc.gridx = 1;
        courseCombo = new JComboBox<>();
        formPanel.add(courseCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        statusCombo = new JComboBox<>(new String[]{"ACTIVE", "COMPLETED", "DROPPED"});
        statusCombo.setSelectedItem("ACTIVE");
        formPanel.add(statusCombo, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton enrollBtn = new JButton("Enroll Student");
        JButton updateBtn = new JButton("Update Enrollment");
        JButton deleteBtn = new JButton("Delete Enrollment");
        JButton clearBtn = new JButton("Clear");

        enrollBtn.addActionListener(e -> enrollStudent());
        updateBtn.addActionListener(e -> updateEnrollment());
        deleteBtn.addActionListener(e -> deleteEnrollment());
        clearBtn.addActionListener(e -> clearForm());

        buttonPanel.add(enrollBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);

        // Table
        String[] columns = {"ID", "Student", "Course", "Enrollment Date", "Status", "Grade"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        enrollmentTable = new JTable(tableModel);
        enrollmentTable.getSelectionModel().addListSelectionListener(e -> loadSelectedEnrollment());
        JScrollPane tableScroll = new JScrollPane(enrollmentTable);

        // Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
    }

    private void loadEnrollments() {
        tableModel.setRowCount(0);
        List<Enrollment> enrollments = enrollmentDAO.getAllEnrollments();
        for (Enrollment enrollment : enrollments) {
            tableModel.addRow(new Object[]{
                enrollment.getId(),
                enrollment.getStudentName(),
                enrollment.getCourseName(),
                enrollment.getEnrollmentDate(),
                enrollment.getStatus(),
                enrollment.getGrade() > 0 ? enrollment.getGrade() : ""
            });
        }
    }

    private void loadStudents() {
        List<Student> students = studentDAO.getAllStudents();
        for (Student student : students) {
            studentCombo.addItem(student);
        }
    }

    private void loadCourses() {
        List<Course> courses = courseDAO.getAllCourses();
        for (Course course : courses) {
            courseCombo.addItem(course);
        }
    }

    private void loadSelectedEnrollment() {
        int selectedRow = enrollmentTable.getSelectedRow();
        if (selectedRow >= 0) {
            // Load selected enrollment details if needed
            // For now, just clear selection as form is for new enrollments
        }
    }

    private void enrollStudent() {
        Student selectedStudent = (Student) studentCombo.getSelectedItem();
        Course selectedCourse = (Course) courseCombo.getSelectedItem();
        String status = (String) statusCombo.getSelectedItem();

        if (selectedStudent == null || selectedCourse == null) {
            JOptionPane.showMessageDialog(this, "Please select both student and course.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(selectedStudent.getId());
        enrollment.setCourseId(selectedCourse.getId());
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setStatus(status);

        if (enrollmentDAO.addEnrollment(enrollment)) {
            JOptionPane.showMessageDialog(this, "Student enrolled successfully!");
            loadEnrollments();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to enroll student.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateEnrollment() {
        int selectedRow = enrollmentTable.getSelectedRow();
        if (selectedRow >= 0) {
            int enrollmentId = (Integer) tableModel.getValueAt(selectedRow, 0);
            // Implementation for updating enrollment status/grade would go here
            JOptionPane.showMessageDialog(this, "Update functionality to be implemented.", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Please select an enrollment to update.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteEnrollment() {
        int selectedRow = enrollmentTable.getSelectedRow();
        if (selectedRow >= 0) {
            int enrollmentId = (Integer) tableModel.getValueAt(selectedRow, 0);
            int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this enrollment?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                if (enrollmentDAO.deleteEnrollment(enrollmentId)) {
                    JOptionPane.showMessageDialog(this, "Enrollment deleted successfully!");
                    loadEnrollments();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete enrollment.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an enrollment to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        studentCombo.setSelectedIndex(0);
        courseCombo.setSelectedIndex(0);
        statusCombo.setSelectedItem("ACTIVE");
        enrollmentTable.clearSelection();
    }
}