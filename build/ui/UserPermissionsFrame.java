package ui;

import dao.UserDAO;
import model.User;
import service.AuthService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserPermissionsFrame extends JFrame {
    private User currentUser;
    private UserDAO userDAO = new UserDAO();
    private AuthService authService = new AuthService();
    private JTable userTable;
    private DefaultTableModel tableModel;

    public UserPermissionsFrame(User user) {
        this.currentUser = user;

        if (!authService.isAdmin(currentUser)) {
            JOptionPane.showMessageDialog(this, "Access denied", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        setTitle("User Permissions & Roles Management");
        setSize(900, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        loadUsers();

        setVisible(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // User table
        tableModel = new DefaultTableModel(new String[]{"ID", "Username", "Name", "Email", "Role", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Only role column is editable
            }
        };
        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);

        // Role editor
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"USER", "STUDENT", "TEACHER", "ADMIN"});
        userTable.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(roleCombo));

        // Control panel
        JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBorder(BorderFactory.createTitledBorder("Permission Controls"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        controlPanel.add(new JLabel("Selected User:"), gbc);
        gbc.gridx = 1;
        JTextField selectedUserField = new JTextField(20);
        selectedUserField.setEditable(false);
        controlPanel.add(selectedUserField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        controlPanel.add(new JLabel("New Role:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> newRoleCombo = new JComboBox<>(new String[]{"USER", "STUDENT", "TEACHER", "ADMIN"});
        controlPanel.add(newRoleCombo, gbc);

        // Permission checkboxes
        gbc.gridx = 0; gbc.gridy = 2;
        controlPanel.add(new JLabel("Permissions:"), gbc);
        gbc.gridx = 1;
        JPanel permPanel = new JPanel(new GridLayout(2, 2));
        JCheckBox readBox = new JCheckBox("Read Access", true);
        JCheckBox writeBox = new JCheckBox("Write Access", true);
        JCheckBox adminBox = new JCheckBox("Admin Access", false);
        JCheckBox superBox = new JCheckBox("Super Admin", false);
        permPanel.add(readBox);
        permPanel.add(writeBox);
        permPanel.add(adminBox);
        permPanel.add(superBox);
        controlPanel.add(permPanel, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton updateRoleBtn = new JButton("Update Role");
        JButton grantAdminBtn = new JButton("Grant Admin Rights");
        JButton revokeAdminBtn = new JButton("Revoke Admin Rights");
        JButton deactivateBtn = new JButton("Deactivate User");
        JButton activateBtn = new JButton("Activate User");
        JButton refreshBtn = new JButton("Refresh");

        buttonPanel.add(updateRoleBtn);
        buttonPanel.add(grantAdminBtn);
        buttonPanel.add(revokeAdminBtn);
        buttonPanel.add(deactivateBtn);
        buttonPanel.add(activateBtn);
        buttonPanel.add(refreshBtn);

        // Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(controlPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Table selection listener
        userTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow != -1) {
                    String username = (String) tableModel.getValueAt(selectedRow, 1);
                    String role = (String) tableModel.getValueAt(selectedRow, 4);
                    selectedUserField.setText(username);
                    newRoleCombo.setSelectedItem(role);

                    // Set permission checkboxes based on role
                    boolean isAdmin = "ADMIN".equals(role);
                    adminBox.setSelected(isAdmin);
                    superBox.setSelected("SUPERADMIN".equals(role));
                }
            }
        });

        // Button actions
        updateRoleBtn.addActionListener(e -> updateUserRole(selectedUserField.getText(), (String) newRoleCombo.getSelectedItem()));
        grantAdminBtn.addActionListener(e -> grantAdminRights(selectedUserField.getText()));
        revokeAdminBtn.addActionListener(e -> revokeAdminRights(selectedUserField.getText()));
        deactivateBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "User deactivation feature coming soon"));
        activateBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "User activation feature coming soon"));
        refreshBtn.addActionListener(e -> loadUsers());
    }

    private void loadUsers() {
        List<User> users = userDAO.getAllUsers();
        tableModel.setRowCount(0);

        for (User user : users) {
            tableModel.addRow(new Object[]{
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                "Active" // Mock status
            });
        }
    }

    private void updateUserRole(String username, String newRole) {
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a user first");
            return;
        }

        // Prevent self-demotion for security
        if (username.equals(currentUser.getUsername()) && !"ADMIN".equals(newRole)) {
            JOptionPane.showMessageDialog(this, "You cannot change your own admin privileges", "Security Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = userDAO.findByUsername(username);
        if (user != null) {
            user.setRole(newRole);
            if (userDAO.updateUser(user)) {
                JOptionPane.showMessageDialog(this, "User role updated successfully!");
                loadUsers();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update user role");
            }
        }
    }

    private void grantAdminRights(String username) {
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a user first");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to grant admin rights to " + username + "?\nThis will give them full system access.",
            "Confirm Admin Rights", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            updateUserRole(username, "ADMIN");
        }
    }

    private void revokeAdminRights(String username) {
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a user first");
            return;
        }

        // Prevent self-revocation
        if (username.equals(currentUser.getUsername())) {
            JOptionPane.showMessageDialog(this, "You cannot revoke your own admin rights", "Security Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to revoke admin rights from " + username + "?",
            "Confirm Revocation", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            updateUserRole(username, "USER");
        }
    }
}