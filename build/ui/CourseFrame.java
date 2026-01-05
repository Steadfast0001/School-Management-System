package ui;

import dao.CourseDAO;
import dao.DepartmentDAO;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Course;
import model.Department;
import model.User;

public class CourseFrame extends JFrame {
    private User currentUser;
    private CourseDAO courseDAO = new CourseDAO();
    private DepartmentDAO departmentDAO = new DepartmentDAO();
    private JTable courseTable;
    private DefaultTableModel tableModel;
    private JTextField codeField, nameField, creditsField;
    private JTextArea descriptionArea;
    private JComboBox<Department> departmentCombo;

    public CourseFrame(User user) {
        this.currentUser = user;

        setTitle("Course Management");
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        loadCourses();
        loadDepartments();

        setVisible(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Course Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Course Code:"), gbc);
        gbc.gridx = 1;
        codeField = new JTextField(20);
        formPanel.add(codeField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Course Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        formPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Credits:"), gbc);
        gbc.gridx = 1;
        creditsField = new JTextField(20);
        formPanel.add(creditsField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1;
        departmentCombo = new JComboBox<>();
        formPanel.add(departmentCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        formPanel.add(scrollPane, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addBtn = new JButton("Add Course");
        JButton updateBtn = new JButton("Update Course");
        JButton deleteBtn = new JButton("Delete Course");
        JButton clearBtn = new JButton("Clear");

        addBtn.addActionListener(e -> addCourse());
        updateBtn.addActionListener(e -> updateCourse());
        deleteBtn.addActionListener(e -> deleteCourse());
        clearBtn.addActionListener(e -> clearForm());

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);

        // Table
        String[] columns = {"ID", "Code", "Name", "Credits", "Department", "Description"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        courseTable = new JTable(tableModel);
        courseTable.getSelectionModel().addListSelectionListener(e -> loadSelectedCourse());
        JScrollPane tableScroll = new JScrollPane(courseTable);

        // Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
    }

    private void loadCourses() {
        tableModel.setRowCount(0);
        List<Course> courses = courseDAO.getAllCourses();
        for (Course course : courses) {
            tableModel.addRow(new Object[]{
                course.getId(),
                course.getCourseCode(),
                course.getCourseName(),
                course.getCredits(),
                course.getDepartmentName(),
                course.getDescription()
            });
        }
    }

    private void loadDepartments() {
        List<Department> departments = departmentDAO.getAllDepartments();
        for (Department dept : departments) {
            departmentCombo.addItem(dept);
        }
    }

    private void loadSelectedCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow >= 0) {
            int courseId = (Integer) tableModel.getValueAt(selectedRow, 0);
            Course course = courseDAO.getCourseById(courseId);
            if (course != null) {
                codeField.setText(course.getCourseCode());
                nameField.setText(course.getCourseName());
                creditsField.setText(String.valueOf(course.getCredits()));
                descriptionArea.setText(course.getDescription());

                // Set department combo
                for (int i = 0; i < departmentCombo.getItemCount(); i++) {
                    Department dept = departmentCombo.getItemAt(i);
                    if (dept.getId() == course.getDepartmentId()) {
                        departmentCombo.setSelectedItem(dept);
                        break;
                    }
                }
            }
        }
    }

    private void addCourse() {
        if (validateForm()) {
            Course course = new Course();
            course.setCourseCode(codeField.getText().trim());
            course.setCourseName(nameField.getText().trim());
            course.setCredits(Integer.parseInt(creditsField.getText().trim()));
            course.setDescription(descriptionArea.getText().trim());
            Department selectedDept = (Department) departmentCombo.getSelectedItem();
            course.setDepartmentId(selectedDept.getId());

            if (courseDAO.addCourse(course)) {
                JOptionPane.showMessageDialog(this, "Course added successfully!");
                loadCourses();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add course.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow >= 0 && validateForm()) {
            int courseId = (Integer) tableModel.getValueAt(selectedRow, 0);
            Course course = courseDAO.getCourseById(courseId);
            if (course != null) {
                course.setCourseCode(codeField.getText().trim());
                course.setCourseName(nameField.getText().trim());
                course.setCredits(Integer.parseInt(creditsField.getText().trim()));
                course.setDescription(descriptionArea.getText().trim());
                Department selectedDept = (Department) departmentCombo.getSelectedItem();
                course.setDepartmentId(selectedDept.getId());

                if (courseDAO.updateCourse(course)) {
                    JOptionPane.showMessageDialog(this, "Course updated successfully!");
                    loadCourses();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update course.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a course to update.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow >= 0) {
            int courseId = (Integer) tableModel.getValueAt(selectedRow, 0);
            int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this course?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                if (courseDAO.deleteCourse(courseId)) {
                    JOptionPane.showMessageDialog(this, "Course deleted successfully!");
                    loadCourses();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete course.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a course to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        codeField.setText("");
        nameField.setText("");
        creditsField.setText("");
        descriptionArea.setText("");
        departmentCombo.setSelectedIndex(0);
        courseTable.clearSelection();
    }

    private boolean validateForm() {
        if (codeField.getText().trim().isEmpty() || nameField.getText().trim().isEmpty() ||
            creditsField.getText().trim().isEmpty() || departmentCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            Integer.parseInt(creditsField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Credits must be a valid number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}