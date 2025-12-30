package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TeacherFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public TeacherFrame() {
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
        model = new DefaultTableModel(new String[]{"ID", "Name", "Email"}, 0);
        table = new JTable(model);
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
}
