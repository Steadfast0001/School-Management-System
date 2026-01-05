package ui;

import dao.LibraryDAO;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Library;
import model.User;

public class LibraryFrame extends JFrame {
    private User currentUser;
    private LibraryDAO libraryDAO = new LibraryDAO();
    private JTable libraryTable;
    private DefaultTableModel tableModel;
    private JTextField titleField, authorField, isbnField, totalCopiesField, availableCopiesField, locationField;
    private JComboBox<String> categoryCombo;

    public LibraryFrame(User user) {
        this.currentUser = user;

        setTitle("Library Management");
        setSize(900, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        loadBooks();

        setVisible(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Book Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        titleField = new JTextField(20);
        formPanel.add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Author:"), gbc);
        gbc.gridx = 1;
        authorField = new JTextField(20);
        formPanel.add(authorField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("ISBN:"), gbc);
        gbc.gridx = 1;
        isbnField = new JTextField(20);
        formPanel.add(isbnField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        categoryCombo = new JComboBox<>(new String[]{"Fiction", "Non-Fiction", "Science", "Technology", "History", "Biography", "Other"});
        formPanel.add(categoryCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Total Copies:"), gbc);
        gbc.gridx = 1;
        totalCopiesField = new JTextField(20);
        formPanel.add(totalCopiesField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Available Copies:"), gbc);
        gbc.gridx = 1;
        availableCopiesField = new JTextField(20);
        formPanel.add(availableCopiesField, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1;
        locationField = new JTextField(20);
        formPanel.add(locationField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addBtn = new JButton("Add Book");
        JButton updateBtn = new JButton("Update Book");
        JButton deleteBtn = new JButton("Delete Book");
        JButton clearBtn = new JButton("Clear");

        addBtn.addActionListener(e -> addBook());
        updateBtn.addActionListener(e -> updateBook());
        deleteBtn.addActionListener(e -> deleteBook());
        clearBtn.addActionListener(e -> clearForm());

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);

        // Table
        String[] columns = {"ID", "Title", "Author", "ISBN", "Category", "Total", "Available", "Location"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        libraryTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(libraryTable);

        // Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
    }

    private void loadBooks() {
        tableModel.setRowCount(0);
        List<Library> books = libraryDAO.getAllBooks();
        for (Library book : books) {
            tableModel.addRow(new Object[]{
                book.getId(),
                book.getBookTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getCategory(),
                book.getTotalCopies(),
                book.getAvailableCopies(),
                book.getLocation()
            });
        }
    }

    private void addBook() {
        if (validateForm()) {
            Library book = new Library();
            book.setBookTitle(titleField.getText().trim());
            book.setAuthor(authorField.getText().trim());
            book.setIsbn(isbnField.getText().trim());
            book.setCategory((String) categoryCombo.getSelectedItem());
            book.setTotalCopies(Integer.parseInt(totalCopiesField.getText().trim()));
            book.setAvailableCopies(Integer.parseInt(availableCopiesField.getText().trim()));
            book.setLocation(locationField.getText().trim());

            if (libraryDAO.addBook(book)) {
                JOptionPane.showMessageDialog(this, "Book added successfully!");
                loadBooks();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add book.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateBook() {
        JOptionPane.showMessageDialog(this, "Update functionality to be implemented.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteBook() {
        JOptionPane.showMessageDialog(this, "Delete functionality to be implemented.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearForm() {
        titleField.setText("");
        authorField.setText("");
        isbnField.setText("");
        categoryCombo.setSelectedIndex(0);
        totalCopiesField.setText("");
        availableCopiesField.setText("");
        locationField.setText("");
    }

    private boolean validateForm() {
        if (titleField.getText().trim().isEmpty() || authorField.getText().trim().isEmpty() ||
            totalCopiesField.getText().trim().isEmpty() || availableCopiesField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            Integer.parseInt(totalCopiesField.getText().trim());
            Integer.parseInt(availableCopiesField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Copies must be valid numbers.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}