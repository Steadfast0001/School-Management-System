package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ResultFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public ResultFrame() {
        setTitle("Result Management");
        setSize(700, 400);
        setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        JTextField studentField = new JTextField();
        JTextField subjectField = new JTextField();
        JTextField scoreField = new JTextField();

        formPanel.add(new JLabel("Student Name:"));
        formPanel.add(studentField);
        formPanel.add(new JLabel("Subject:"));
        formPanel.add(subjectField);
        formPanel.add(new JLabel("Score:"));
        formPanel.add(scoreField);

        JButton addBtn = new JButton("Add Result");

        model = new DefaultTableModel(
                new String[]{"ID", "Student", "Subject", "Score"}, 0);
        table = new JTable(model);

        add(formPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(addBtn, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> {
            model.addRow(new Object[]{
                    model.getRowCount() + 1,
                    studentField.getText(),
                    subjectField.getText(),
                    scoreField.getText()
            });
            studentField.setText("");
            subjectField.setText("");
            scoreField.setText("");
        });

        setVisible(true);
    }
}
