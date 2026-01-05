package ui;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import model.User;

public class AcademicCalendarFrame extends JFrame {
    private User currentUser;
    private YearMonth currentYearMonth;
    private JPanel calendarPanel;
    private Map<LocalDate, String> events;

    public AcademicCalendarFrame(User user) {
        this.currentUser = user;
        this.currentYearMonth = YearMonth.now();
        this.events = new HashMap<>();

        // Sample events
        loadSampleEvents();

        setTitle("Academic Calendar Management");
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        updateCalendar();

        setVisible(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Header with navigation
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton prevBtn = new JButton("◀");
        JButton nextBtn = new JButton("▶");
        JLabel monthYearLabel = new JLabel("", JLabel.CENTER);
        monthYearLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel navPanel = new JPanel(new FlowLayout());
        navPanel.add(prevBtn);
        navPanel.add(monthYearLabel);
        navPanel.add(nextBtn);

        headerPanel.add(navPanel, BorderLayout.CENTER);

        // Control panel
        JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBorder(BorderFactory.createTitledBorder("Event Management"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        controlPanel.add(new JLabel("Date:"), gbc);
        gbc.gridx = 1;
        JTextField dateField = new JTextField(10);
        dateField.setText(LocalDate.now().toString());
        controlPanel.add(dateField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        controlPanel.add(new JLabel("Event:"), gbc);
        gbc.gridx = 1;
        JTextField eventField = new JTextField(20);
        controlPanel.add(eventField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JButton addEventBtn = new JButton("Add Event");
        controlPanel.add(addEventBtn, gbc);
        gbc.gridx = 1;
        JButton removeEventBtn = new JButton("Remove Event");
        controlPanel.add(removeEventBtn, gbc);

        // Calendar panel
        calendarPanel = new JPanel(new GridLayout(0, 7, 2, 2));
        calendarPanel.setBorder(BorderFactory.createTitledBorder("Calendar"));
        JScrollPane scrollPane = new JScrollPane(calendarPanel);

        // Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(controlPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Button actions
        prevBtn.addActionListener(e -> {
            currentYearMonth = currentYearMonth.minusMonths(1);
            updateCalendar();
        });

        nextBtn.addActionListener(e -> {
            currentYearMonth = currentYearMonth.plusMonths(1);
            updateCalendar();
        });

        addEventBtn.addActionListener(e -> {
            try {
                LocalDate date = LocalDate.parse(dateField.getText());
                String event = eventField.getText().trim();
                if (!event.isEmpty()) {
                    events.put(date, event);
                    updateCalendar();
                    eventField.setText("");
                    JOptionPane.showMessageDialog(this, "Event added successfully!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD");
            }
        });

        removeEventBtn.addActionListener(e -> {
            try {
                LocalDate date = LocalDate.parse(dateField.getText());
                if (events.containsKey(date)) {
                    events.remove(date);
                    updateCalendar();
                    JOptionPane.showMessageDialog(this, "Event removed successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "No event found for this date");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD");
            }
        });

        updateCalendar();
    }

    private void updateCalendar() {
        calendarPanel.removeAll();

        // Day headers
        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : days) {
            JLabel label = new JLabel(day, JLabel.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 12));
            label.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            label.setOpaque(true);
            label.setBackground(Color.LIGHT_GRAY);
            calendarPanel.add(label);
        }

        // Get first day of month and day of week
        LocalDate firstOfMonth = currentYearMonth.atDay(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue() % 7; // 0 = Sunday

        // Empty cells for days before first of month
        for (int i = 0; i < dayOfWeek; i++) {
            calendarPanel.add(new JLabel(""));
        }

        // Days of month
        LocalDate date = firstOfMonth;
        while (date.getMonth() == currentYearMonth.getMonth()) {
            JButton dayButton = new JButton(String.valueOf(date.getDayOfMonth()));
            dayButton.setPreferredSize(new Dimension(80, 60));
            dayButton.setVerticalAlignment(SwingConstants.TOP);
            dayButton.setHorizontalAlignment(SwingConstants.LEFT);

            // Check if there's an event
            if (events.containsKey(date)) {
                dayButton.setText("<html><b>" + date.getDayOfMonth() + "</b><br><small>" + events.get(date) + "</small></html>");
                dayButton.setBackground(Color.YELLOW);
            }

            // Highlight today
            if (date.equals(LocalDate.now())) {
                dayButton.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            }

            final LocalDate eventDate = date;
            dayButton.addActionListener(e -> {
                // Show events for this date
                String event = events.get(eventDate);
                if (event != null) {
                    JOptionPane.showMessageDialog(this, "Event on " + eventDate + ":\n" + event);
                }
            });

            calendarPanel.add(dayButton);
            date = date.plusDays(1);
        }

        // Update month/year label
        Component[] components = ((JPanel) getContentPane().getComponent(0)).getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                Component[] subComps = ((JPanel) comp).getComponents();
                for (Component subComp : subComps) {
                    if (subComp instanceof JPanel) {
                        Component[] navComps = ((JPanel) subComp).getComponents();
                        for (Component navComp : navComps) {
                            if (navComp instanceof JLabel) {
                                ((JLabel) navComp).setText(currentYearMonth.getMonth() + " " + currentYearMonth.getYear());
                                break;
                            }
                        }
                    }
                }
            }
        }

        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    private void loadSampleEvents() {
        LocalDate today = LocalDate.now();
        events.put(today, "System Maintenance");
        events.put(today.plusDays(7), "Semester Start");
        events.put(today.plusDays(14), "Mid-term Exams");
        events.put(today.plusDays(30), "Project Deadline");
        events.put(today.plusDays(60), "Final Exams");
    }
}