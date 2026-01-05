package ui;

import dao.DepartmentDAO;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Department;
import model.User;

public class DepartmentFrame extends JFrame {
    private User currentUser;
    private DepartmentDAO departmentDAO = new DepartmentDAO();
    private JTable departmentTable;
    private DefaultTableModel tableModel;
    private JTextField codeField, nameField, headField;
    private JTextArea descriptionArea;

    public DepartmentFrame(User user) {
        this.currentUser = user;

        setTitle("Department Management");
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        loadDepartments();

        setVisible(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Department Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Department Code:"), gbc);
        gbc.gridx = 1;
        codeField = new JTextField(20);
        formPanel.add(codeField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Department Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        formPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Head of Department:"), gbc);
        gbc.gridx = 1;
        headField = new JTextField(20);
        formPanel.add(headField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        formPanel.add(scrollPane, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addBtn = new JButton("Add Department");
        JButton updateBtn = new JButton("Update Department");
        JButton deleteBtn = new JButton("Delete Department");
        JButton clearBtn = new JButton("Clear");

        addBtn.addActionListener(e -> addDepartment());
        updateBtn.addActionListener(e -> updateDepartment());
        deleteBtn.addActionListener(e -> deleteDepartment());
        clearBtn.addActionListener(e -> clearForm());

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);

        // Table
        String[] columns = {"ID", "Code", "Name", "Head", "Description"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        departmentTable = new JTable(tableModel);
        departmentTable.getSelectionModel().addListSelectionListener(e -> loadSelectedDepartment());
        JScrollPane tableScroll = new JScrollPane(departmentTable);

        // Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
    }

    private void loadDepartments() {
        tableModel.setRowCount(0);
        List<Department> departments = departmentDAO.getAllDepartments();
        for (Department dept : departments) {
            tableModel.addRow(new Object[]{
                dept.getId(),
                dept.getDepartmentCode(),
                dept.getDepartmentName(),
                dept.getHeadOfDepartment(),
                dept.getDescription()
            });
        }
    }

    private void loadSelectedDepartment() {
        int selectedRow = departmentTable.getSelectedRow();
        if (selectedRow >= 0) {
            int deptId = (Integer) tableModel.getValueAt(selectedRow, 0);
            Department dept = departmentDAO.getDepartmentById(deptId);
            if (dept != null) {
                codeField.setText(dept.getDepartmentCode());
                nameField.setText(dept.getDepartmentName());
                headField.setText(dept.getHeadOfDepartment());
                descriptionArea.setText(dept.getDescription());
            }
        }
    }

    private void addDepartment() {
        if (validateForm()) {
            Department dept = new Department();
            dept.setDepartmentCode(codeField.getText().trim());
            dept.setDepartmentName(nameField.getText().trim());
            dept.setHeadOfDepartment(headField.getText().trim());
            dept.setDescription(descriptionArea.getText().trim());

            if (departmentDAO.addDepartment(dept)) {
                JOptionPane.showMessageDialog(this, "Department added successfully!");
                loadDepartments();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add department.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateDepartment() {
        int selectedRow = departmentTable.getSelectedRow();
        if (selectedRow >= 0 && validateForm()) {
            int deptId = (Integer) tableModel.getValueAt(selectedRow, 0);
            Department dept = departmentDAO.getDepartmentById(deptId);
            if (dept != null) {
                dept.setDepartmentCode(codeField.getText().trim());
                dept.setDepartmentName(nameField.getText().trim());
                dept.setHeadOfDepartment(headField.getText().trim());
                dept.setDescription(descriptionArea.getText().trim());

                if (departmentDAO.updateDepartment(dept)) {
                    JOptionPane.showMessageDialog(this, "Department updated successfully!");
                    loadDepartments();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update department.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a department to update.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteDepartment() {
        int selectedRow = departmentTable.getSelectedRow();
        if (selectedRow >= 0) {
            int deptId = (Integer) tableModel.getValueAt(selectedRow, 0);
            int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this department?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                if (departmentDAO.deleteDepartment(deptId)) {
                    JOptionPane.showMessageDialog(this, "Department deleted successfully!");
                    loadDepartments();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete department.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a department to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        codeField.setText("");
        nameField.setText("");
        headField.setText("");
        descriptionArea.setText("");
        departmentTable.clearSelection();
    }

    private boolean validateForm() {
        if (codeField.getText().trim().isEmpty() || nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}