package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import model.User;
import model.*;
import dao.*;
import service.*;

public class StudentFrame extends JFrame {

    private User currentUser;
    private int studentId;

    public StudentFrame(User user) {
        this.currentUser = user;

        if ("STUDENT".equalsIgnoreCase(user.getRole())) {
            buildStudentDashboard();
        } else {
            buildManagementPanel();
        }
    }

    private void buildStudentDashboard() {
        setTitle("Student Dashboard - " + currentUser.getName());
        setSize(900, 700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Tab 1: Registered Courses
        tabbedPane.addTab("My Courses", createCoursesPanel());

        // Tab 2: Academic Results
        tabbedPane.addTab("Grades", createGradesPanel());

        // Tab 3: Transcript
        tabbedPane.addTab("Transcript", createTranscriptPanel());

        // Tab 4: Attendance
        tabbedPane.addTab("Attendance", createAttendancePanel());

        // Tab 5: Fees
        tabbedPane.addTab("Fees", createFeesPanel());

        // Tab 6: Announcements
        tabbedPane.addTab("Announcements", createAnnouncementsPanel());

        // Tab 7: Timetable
        tabbedPane.addTab("Timetable", createTimetablePanel());

        add(tabbedPane);
        setVisible(true);
    }

    private void buildManagementPanel() {
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

    private JPanel createCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        EnrollmentDAO dao = new EnrollmentDAO();
        List<Enrollment> enrollments = dao.getEnrollmentsByStudent(studentId);

        DefaultTableModel model = new DefaultTableModel(new String[]{"Course Name", "Status", "Grade"}, 0);
        for (Enrollment e : enrollments) {
            model.addRow(new Object[]{e.getCourseName(), e.getStatus(), e.getGrade()});
        }
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createGradesPanel() {
        return createCoursesPanel(); // Same as courses for now
    }

    private JPanel createTranscriptPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        // Build transcript text
        StringBuilder sb = new StringBuilder();
        sb.append("Transcript for ").append(currentUser.getName()).append("\n\n");

        EnrollmentDAO eDao = new EnrollmentDAO();
        List<Enrollment> enrollments = eDao.getEnrollmentsByStudent(studentId);
        sb.append("Courses Taken:\n");
        for (Enrollment e : enrollments) {
            sb.append(e.getCourseName()).append(" - Grade: ").append(e.getGrade()).append("\n");
        }

        // Attendance summary
        AttendanceDAO aDao = new AttendanceDAO();
        List<Attendance> attendances = aDao.getAttendanceByStudent(studentId);
        int total = attendances.size();
        long present = attendances.stream().filter(a -> "Present".equals(a.getStatus())).count();
        sb.append("\nAttendance Summary: ").append(present).append("/").append(total).append(" days present\n");

        // Fee history
        FeeDAO fDao = new FeeDAO();
        List<Fee> fees = fDao.getFeesByStudent(studentId);
        sb.append("\nFee Payment History:\n");
        for (Fee f : fees) {
            sb.append("Type: ").append(f.getFeeType()).append(", Amount: ").append(f.getAmount()).append(", Status: ").append(f.getStatus()).append("\n");
        }

        textArea.setText(sb.toString());
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);

        JButton downloadBtn = new JButton("Download Transcript");
        downloadBtn.addActionListener(e -> {
            try (FileWriter fw = new FileWriter("transcript.txt")) {
                fw.write(sb.toString());
                JOptionPane.showMessageDialog(this, "Transcript saved to transcript.txt");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        panel.add(downloadBtn, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createAttendancePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        AttendanceDAO dao = new AttendanceDAO();
        List<Attendance> attendances = dao.getAttendanceByStudent(studentId);

        DefaultTableModel model = new DefaultTableModel(new String[]{"Date", "Present"}, 0);
        for (Attendance a : attendances) {
            model.addRow(new Object[]{a.getDate(), "Present".equals(a.getStatus()) ? "Yes" : "No"});
        }
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFeesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        FeeDAO dao = new FeeDAO();
        List<Fee> fees = dao.getFeesByStudent(studentId);

        DefaultTableModel model = new DefaultTableModel(new String[]{"Type", "Amount", "Paid", "Status", "Due Date"}, 0);
        for (Fee f : fees) {
            model.addRow(new Object[]{f.getFeeType(), f.getAmount(), f.getPaidAmount(), f.getStatus(), f.getDueDate()});
        }
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createAnnouncementsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        AnnouncementDAO dao = new AnnouncementDAO();
        List<Announcement> announcements = dao.getAnnouncementsForAudience("STUDENT");

        DefaultTableModel model = new DefaultTableModel(new String[]{"Title", "Content", "Date"}, 0);
        for (Announcement a : announcements) {
            model.addRow(new Object[]{a.getTitle(), a.getContent(), a.getPublishDate()});
        }
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createTimetablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        TimetableDAO dao = new TimetableDAO();
        List<Timetable> timetables = dao.getAllTimetables(); // Placeholder: filter by student's courses

        DefaultTableModel model = new DefaultTableModel(new String[]{"Day", "Time", "Course", "Room"}, 0);
        for (Timetable t : timetables) {
            model.addRow(new Object[]{t.getDayOfWeek(), t.getStartTime() + "-" + t.getEndTime(), t.getCourseName(), t.getRoom()});
        }
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }
}
