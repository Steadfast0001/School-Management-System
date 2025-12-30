package ui;

import javax.swing.*;

public class StudentFrame extends JFrame {

    public StudentFrame() {
        setTitle("Student Management");
        setSize(700, 400);

        JTable table = new JTable();
        JScrollPane pane = new JScrollPane(table);

        JButton addBtn = new JButton("Add Student");
        JButton delBtn = new JButton("Delete Student");

        JPanel top = new JPanel();
        top.add(addBtn);
        top.add(delBtn);

        add(top, "North");
        add(pane);

        setVisible(true);
    }
}
