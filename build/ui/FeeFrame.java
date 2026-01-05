package ui;

import dao.FeeDAO;
import dao.StudentDAO;
import model.Fee;
import model.Student;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class FeeFrame extends JFrame {
    private User currentUser;
    private FeeDAO feeDAO = new FeeDAO();
    private StudentDAO studentDAO = new StudentDAO();
    private JTable feeTable;
    private DefaultTableModel tableModel;
    private JComboBox<Student> studentCombo;
    private JTextField feeTypeField, amountField, paidAmountField;
    private JComboBox<String> statusCombo;

    public FeeFrame(User user) {
        this.currentUser = user;

        setTitle("Fee Management");
        setSize(900, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        loadFees();
        loadStudents();

        setVisible(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Fee Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Student:"), gbc);
        gbc.gridx = 1;
        studentCombo = new JComboBox<>();
        formPanel.add(studentCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Fee Type:"), gbc);
        gbc.gridx = 1;
        feeTypeField = new JTextField(20);
        formPanel.add(feeTypeField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Amount:"), gbc);
        gbc.gridx = 1;
        amountField = new JTextField(20);
        formPanel.add(amountField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Paid Amount:"), gbc);
        gbc.gridx = 1;
        paidAmountField = new JTextField(20);
        formPanel.add(paidAmountField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        statusCombo = new JComboBox<>(new String[]{"PENDING", "PAID", "OVERDUE"});
        statusCombo.setSelectedItem("PENDING");
        formPanel.add(statusCombo, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addBtn = new JButton("Add Fee");
        JButton updateBtn = new JButton("Update Fee");
        JButton deleteBtn = new JButton("Delete Fee");
        JButton clearBtn = new JButton("Clear");

        addBtn.addActionListener(e -> addFee());
        updateBtn.addActionListener(e -> updateFee());
        deleteBtn.addActionListener(e -> deleteFee());
        clearBtn.addActionListener(e -> clearForm());

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);

        // Table
        String[] columns = {"ID", "Student", "Fee Type", "Amount", "Paid", "Balance", "Due Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        feeTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(feeTable);

        // Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
    }

    private void loadFees() {
        tableModel.setRowCount(0);
        List<Fee> fees = feeDAO.getAllFees();
        for (Fee fee : fees) {
            tableModel.addRow(new Object[]{
                fee.getId(),
                fee.getStudentName(),
                fee.getFeeType(),
                fee.getAmount(),
                fee.getPaidAmount(),
                fee.getBalance(),
                fee.getDueDate(),
                fee.getStatus()
            });
        }
    }

    private void loadStudents() {
        List<Student> students = studentDAO.getAllStudents();
        for (Student student : students) {
            studentCombo.addItem(student);
        }
    }

    private void addFee() {
        if (validateForm()) {
            Fee fee = new Fee();
            Student selectedStudent = (Student) studentCombo.getSelectedItem();
            fee.setStudentId(selectedStudent.getId());
            fee.setFeeType(feeTypeField.getText().trim());
            fee.setAmount(Double.parseDouble(amountField.getText().trim()));
            fee.setPaidAmount(Double.parseDouble(paidAmountField.getText().trim()));
            fee.setDueDate(LocalDate.now().plusMonths(1)); // Default due date
            fee.setStatus((String) statusCombo.getSelectedItem());

            if (feeDAO.addFee(fee)) {
                JOptionPane.showMessageDialog(this, "Fee added successfully!");
                loadFees();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add fee.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateFee() {
        JOptionPane.showMessageDialog(this, "Update functionality to be implemented.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteFee() {
        JOptionPane.showMessageDialog(this, "Delete functionality to be implemented.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearForm() {
        studentCombo.setSelectedIndex(0);
        feeTypeField.setText("");
        amountField.setText("");
        paidAmountField.setText("");
        statusCombo.setSelectedItem("PENDING");
    }

    private boolean validateForm() {
        if (studentCombo.getSelectedItem() == null || feeTypeField.getText().trim().isEmpty() ||
            amountField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            Double.parseDouble(amountField.getText().trim());
            if (!paidAmountField.getText().trim().isEmpty()) {
                Double.parseDouble(paidAmountField.getText().trim());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Amount must be valid numbers.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}