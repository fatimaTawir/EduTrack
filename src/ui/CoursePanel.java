package ui;

import database.DatabaseManager;
import models.Course;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class CoursePanel extends JPanel {

    private DatabaseManager db;
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField courseNameField;
    private JTextField courseCodeField;
    private JTextField creditsField;

    public CoursePanel(DatabaseManager db) {
        this.db = db;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        buildUI();
        loadCourses();
    }

    private void buildUI() {

        // ===== FORM PANEL =====
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Course Name:"), gbc);
        gbc.gridx = 1;
        courseNameField = new JTextField(15);
        formPanel.add(courseNameField, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Course Code:"), gbc);
        gbc.gridx = 3;
        courseCodeField = new JTextField(10);
        formPanel.add(courseCodeField, gbc);

        gbc.gridx = 4;
        formPanel.add(new JLabel("Credits:"), gbc);
        gbc.gridx = 5;
        creditsField = new JTextField(5);
        formPanel.add(creditsField, gbc);

        // ===== BUTTONS =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton addBtn    = createButton("Add Course",    new Color(26, 172, 172));
        JButton updateBtn = createButton("Update Course", new Color(52, 120, 200));
        JButton deleteBtn = createButton("Delete Course", new Color(220, 60, 60));
        JButton clearBtn  = createButton("Clear Fields",  new Color(150, 160, 170));
        JButton exportBtn = createButton("Export CSV",    new Color(80, 160, 80));
        JButton printBtn  = createButton("Print Report",  new Color(120, 80, 160));

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(exportBtn);
        buttonPanel.add(printBtn);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(formPanel,   BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ===== SEARCH BAR =====
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        searchPanel.setBackground(new Color(245, 247, 250));

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(26, 172, 172)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        JButton clearSearchBtn = createButton("Clear Search", new Color(150, 160, 170));

        JLabel countLabel = new JLabel("Total Courses: 0");
        countLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        countLabel.setForeground(new Color(26, 172, 172));

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(clearSearchBtn);
        searchPanel.add(Box.createHorizontalStrut(20));
        searchPanel.add(countLabel);

        // ===== TABLE =====
        String[] columns = {"ID", "Course Name", "Course Code", "Credits"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(210, 240, 240));
        table.setGridColor(new Color(220, 225, 230));

        // ===== FIX TABLE HEADER =====
        table.getTableHeader().setPreferredSize(new Dimension(0, 35));
        table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                JLabel label = new JLabel(value != null ? value.toString() : "");
                label.setBackground(new Color(26, 172, 172));
                label.setForeground(Color.WHITE);
                label.setFont(new Font("SansSerif", Font.BOLD, 13));
                label.setOpaque(true);
                label.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                label.setHorizontalAlignment(JLabel.LEFT);
                return label;
            }
        });

        // Search filter
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { filterTable(searchField.getText(), countLabel); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { filterTable(searchField.getText(), countLabel); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterTable(searchField.getText(), countLabel); }
        });

        clearSearchBtn.addActionListener(e -> {
            searchField.setText("");
            loadCourses(countLabel);
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230)));

        // Click row to fill fields
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                courseNameField.setText((String) tableModel.getValueAt(row, 1));
                courseCodeField.setText((String) tableModel.getValueAt(row, 2));
                creditsField.setText(String.valueOf(tableModel.getValueAt(row, 3)));
            }
        });

        // ===== BUTTON ACTIONS =====

        addBtn.addActionListener(e -> {
            String name = courseNameField.getText().trim();
            String code = courseCodeField.getText().trim();
            String creditsText = creditsField.getText().trim();
            if (name.isEmpty() || code.isEmpty() || creditsText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                int credits = Integer.parseInt(creditsText);
                db.addCourse(name, code, credits);
                loadCourses(countLabel);
                clearFields();
                JOptionPane.showMessageDialog(this, "Course added successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Credits must be a number!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        updateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a course to update!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                int id      = (int) tableModel.getValueAt(row, 0);
                String name = courseNameField.getText().trim();
                String code = courseCodeField.getText().trim();
                int credits = Integer.parseInt(creditsField.getText().trim());
                db.updateCourse(id, name, code, credits);
                loadCourses(countLabel);
                clearFields();
                JOptionPane.showMessageDialog(this, "Course updated successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Credits must be a number!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a course to delete!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this course?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int id = (int) tableModel.getValueAt(row, 0);
                db.deleteCourse(id);
                loadCourses(countLabel);
                clearFields();
                JOptionPane.showMessageDialog(this, "Course deleted successfully!");
            }
        });

        clearBtn.addActionListener(e -> clearFields());

        exportBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new java.io.File("courses.csv"));
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                java.io.File file = fileChooser.getSelectedFile();
                try (java.io.PrintWriter pw = new java.io.PrintWriter(file)) {
                    pw.println("ID,Course Name,Course Code,Credits");
                    List<Course> courses = db.getAllCourses();
                    for (Course c : courses) {
                        pw.println(c.getId() + "," + c.getCourseName() + "," + c.getCourseCode() + "," + c.getCredits());
                    }
                    JOptionPane.showMessageDialog(this, "Exported successfully to:\n" + file.getAbsolutePath());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Export failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        printBtn.addActionListener(e -> {
            try {
                table.print(JTable.PrintMode.FIT_WIDTH,
                    new java.text.MessageFormat("EduTrack — Course Report"),
                    new java.text.MessageFormat("Page {0}"));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Print failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // ===== ASSEMBLE =====
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane,  BorderLayout.CENTER);

        add(topPanel,    BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void loadCourses() {
        tableModel.setRowCount(0);
        List<Course> courses = db.getAllCourses();
        for (Course c : courses) {
            tableModel.addRow(new Object[]{c.getId(), c.getCourseName(), c.getCourseCode(), c.getCredits()});
        }
    }

    private void loadCourses(JLabel countLabel) {
        tableModel.setRowCount(0);
        List<Course> courses = db.getAllCourses();
        for (Course c : courses) {
            tableModel.addRow(new Object[]{c.getId(), c.getCourseName(), c.getCourseCode(), c.getCredits()});
        }
        countLabel.setText("Total Courses: " + courses.size());
    }

    private void filterTable(String query, JLabel countLabel) {
        tableModel.setRowCount(0);
        List<Course> courses = db.getAllCourses();
        int count = 0;
        for (Course c : courses) {
            if (c.getCourseName().toLowerCase().contains(query.toLowerCase()) ||
                c.getCourseCode().toLowerCase().contains(query.toLowerCase())) {
                tableModel.addRow(new Object[]{c.getId(), c.getCourseName(), c.getCourseCode(), c.getCredits()});
                count++;
            }
        }
        countLabel.setText("Total Courses: " + count);
    }

    private void clearFields() {
        courseNameField.setText("");
        courseCodeField.setText("");
        creditsField.setText("");
        table.clearSelection();
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton("<html><b>" + text + "</b></html>");
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        return btn;
    }
}