package ui;

import dao.AnnouncementDAO;
import model.Announcement;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class AnnouncementFrame extends JFrame {
    private User currentUser;
    private AnnouncementDAO announcementDAO = new AnnouncementDAO();
    private JTable announcementTable;
    private DefaultTableModel tableModel;
    private JTextField titleField;
    private JTextArea contentArea;
    private JComboBox<String> audienceCombo;

    public AnnouncementFrame(User user) {
        this.currentUser = user;

        setTitle("Announcements Management");
        setSize(900, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        loadAnnouncements();

        setVisible(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Announcement Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        titleField = new JTextField(30);
        formPanel.add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Content:"), gbc);
        gbc.gridx = 1;
        contentArea = new JTextArea(5, 30);
        contentArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(contentArea);
        formPanel.add(scrollPane, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Target Audience:"), gbc);
        gbc.gridx = 1;
        audienceCombo = new JComboBox<>(new String[]{"ALL", "STUDENTS", "TEACHERS", "ADMIN"});
        audienceCombo.setSelectedItem("ALL");
        formPanel.add(audienceCombo, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addBtn = new JButton("Add Announcement");
        JButton updateBtn = new JButton("Update Announcement");
        JButton deleteBtn = new JButton("Delete Announcement");
        JButton clearBtn = new JButton("Clear");

        addBtn.addActionListener(e -> addAnnouncement());
        updateBtn.addActionListener(e -> updateAnnouncement());
        deleteBtn.addActionListener(e -> deleteAnnouncement());
        clearBtn.addActionListener(e -> clearForm());

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);

        // Table
        String[] columns = {"ID", "Title", "Content", "Author", "Publish Date", "Audience", "Active"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        announcementTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(announcementTable);

        // Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
    }

    private void loadAnnouncements() {
        tableModel.setRowCount(0);
        List<Announcement> announcements = announcementDAO.getAllAnnouncements();
        for (Announcement announcement : announcements) {
            tableModel.addRow(new Object[]{
                announcement.getId(),
                announcement.getTitle(),
                announcement.getContent().length() > 50 ? announcement.getContent().substring(0, 50) + "..." : announcement.getContent(),
                announcement.getAuthor(),
                announcement.getPublishDate(),
                announcement.getTargetAudience(),
                announcement.isActive() ? "Yes" : "No"
            });
        }
    }

    private void addAnnouncement() {
        if (validateForm()) {
            Announcement announcement = new Announcement();
            announcement.setTitle(titleField.getText().trim());
            announcement.setContent(contentArea.getText().trim());
            announcement.setAuthor(currentUser.getName());
            announcement.setPublishDate(LocalDate.now());
            announcement.setExpiryDate(LocalDate.now().plusDays(30)); // Default expiry
            announcement.setTargetAudience((String) audienceCombo.getSelectedItem());
            announcement.setActive(true);

            if (announcementDAO.addAnnouncement(announcement)) {
                JOptionPane.showMessageDialog(this, "Announcement added successfully!");
                loadAnnouncements();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add announcement.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateAnnouncement() {
        JOptionPane.showMessageDialog(this, "Update functionality to be implemented.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteAnnouncement() {
        int selectedRow = announcementTable.getSelectedRow();
        if (selectedRow >= 0) {
            int announcementId = (Integer) tableModel.getValueAt(selectedRow, 0);
            int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this announcement?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                if (announcementDAO.deleteAnnouncement(announcementId)) {
                    JOptionPane.showMessageDialog(this, "Announcement deleted successfully!");
                    loadAnnouncements();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete announcement.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an announcement to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        titleField.setText("");
        contentArea.setText("");
        audienceCombo.setSelectedItem("ALL");
    }

    private boolean validateForm() {
        if (titleField.getText().trim().isEmpty() || contentArea.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}