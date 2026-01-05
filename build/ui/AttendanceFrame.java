package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AttendanceFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public AttendanceFrame() {
        setTitle("Attendance Management");
        setSize(700, 400);
        setLocationRelativeTo(null);

        // Top panel
        JPanel topPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        JTextField studentField = new JTextField();
        JComboBox<String> statusBox = new JComboBox<>(new String[]{"Present", "Absent"});

        topPanel.add(new JLabel("Student Name:"));
        topPanel.add(studentField);
        topPanel.add(new JLabel("Status:"));
        topPanel.add(statusBox);

        JButton markBtn = new JButton("Mark Attendance");

        // Table
        model = new DefaultTableModel(
                new String[]{"ID", "Student", "Date", "Status"}, 0);
        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(markBtn, BorderLayout.SOUTH);

        markBtn.addActionListener(e -> {
            model.addRow(new Object[]{
                    model.getRowCount() + 1,
                    studentField.getText(),
                    java.time.LocalDate.now(),
                    statusBox.getSelectedItem()
            });
            studentField.setText("");
        });

        setVisible(true);
    }
}
