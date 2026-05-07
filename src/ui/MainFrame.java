package ui;

import database.DatabaseManager;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    // Database manager — shared across all panels
    private DatabaseManager db;

    // Constructor — builds the main window
    public MainFrame() {

    // Initialize database and create tables
    db = new DatabaseManager();
    db.createTables();

    // Force button text visibility on Windows
    try {
       UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.select", Color.DARK_GRAY);
    } catch (Exception e) {
        System.out.println("UI error: " + e.getMessage());
    }

    // Window settings
    setTitle("EduTrack — Student Management System");
    setSize(900, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setResizable(true);

    // Build the UI
    buildUI();
}

    private void buildUI() {

        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));

        // ===== HEADER =====
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(26, 172, 172));
        header.setPreferredSize(new Dimension(900, 65));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("EduTrack");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Student Management System");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(220, 245, 245));

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);

        header.add(titlePanel, BorderLayout.WEST);

        // ===== TABS =====
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tabs.setBackground(new Color(245, 247, 250));

        // Add panels to tabs
        tabs.addTab("👨‍🎓  Students", new StudentPanel(db));
        tabs.addTab("📚  Courses",  new CoursePanel(db));
        tabs.addTab("📊  Grades",   new GradePanel(db));

        // ===== FOOTER =====
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(new Color(230, 235, 240));
        footer.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel footerLabel = new JLabel("EduTrack v1.0  |  Built with Java Swing");
        footerLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        footerLabel.setForeground(new Color(150, 160, 170));
        footer.add(footerLabel);

        // ===== ASSEMBLE =====
        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(tabs,   BorderLayout.CENTER);
        mainPanel.add(footer, BorderLayout.SOUTH);

        add(mainPanel);
    }
}