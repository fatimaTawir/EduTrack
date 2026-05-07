package ui;

import database.DatabaseManager;
import models.Grade;
import models.Student;
import models.Course;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class GradePanel extends JPanel {

    private DatabaseManager db;
    private JTable table;
    private DefaultTableModel tableModel;

    private JComboBox<String> studentCombo;
    private JComboBox<String> courseCombo;
    private JTextField scoreField;

    private List<Student> studentList;
    private List<Course>  courseList;

    public GradePanel(DatabaseManager db) {
        this.db = db;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        buildUI();
        loadGrades();
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
        formPanel.add(new JLabel("Student:"), gbc);
        gbc.gridx = 1;
        studentCombo = new JComboBox<>();
        loadStudentCombo();
        formPanel.add(studentCombo, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Course:"), gbc);
        gbc.gridx = 3;
        courseCombo = new JComboBox<>();
        loadCourseCombo();
        formPanel.add(courseCombo, gbc);

        gbc.gridx = 4;
        formPanel.add(new JLabel("Score (0-100):"), gbc);
        gbc.gridx = 5;
        scoreField = new JTextField(8);
        formPanel.add(scoreField, gbc);

        // ===== BUTTONS =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton addBtn     = createButton("Add Grade",     new Color(26, 172, 172));
        JButton deleteBtn  = createButton("Delete Grade",  new Color(220, 60, 60));
        JButton refreshBtn = createButton("Refresh Lists", new Color(52, 120, 200));
        JButton clearBtn   = createButton("Clear Fields",  new Color(150, 160, 170));
        JButton exportBtn  = createButton("Export CSV",    new Color(80, 160, 80));
        JButton printBtn   = createButton("Print Report",  new Color(120, 80, 160));

        buttonPanel.add(addBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);
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

        JLabel countLabel = new JLabel("Total Grades: 0");
        countLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        countLabel.setForeground(new Color(26, 172, 172));

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(clearSearchBtn);
        searchPanel.add(Box.createHorizontalStrut(20));
        searchPanel.add(countLabel);

        // ===== TABLE =====
        String[] columns = {"ID", "Student Name", "Course Name", "Score", "Grade"};
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
            loadGrades(countLabel);
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230)));

        // ===== BUTTON ACTIONS =====

        addBtn.addActionListener(e -> {
            if (studentCombo.getItemCount() == 0 || courseCombo.getItemCount() == 0) {
                JOptionPane.showMessageDialog(this, "Please add students and courses first!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String scoreText = scoreField.getText().trim();
            if (scoreText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a score!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                double score = Double.parseDouble(scoreText);
                if (score < 0 || score > 100) {
                    JOptionPane.showMessageDialog(this, "Score must be between 0 and 100!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int studentId = studentList.get(studentCombo.getSelectedIndex()).getId();
                int courseId  = courseList.get(courseCombo.getSelectedIndex()).getId();
                db.addGrade(studentId, courseId, score);
                loadGrades(countLabel);
                clearFields();
                JOptionPane.showMessageDialog(this, "Grade added successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Score must be a number!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a grade to delete!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this grade?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int id = (int) tableModel.getValueAt(row, 0);
                db.deleteGrade(id);
                loadGrades(countLabel);
                JOptionPane.showMessageDialog(this, "Grade deleted successfully!");
            }
        });

        refreshBtn.addActionListener(e -> {
            loadStudentCombo();
            loadCourseCombo();
            JOptionPane.showMessageDialog(this, "Lists refreshed!");
        });

        clearBtn.addActionListener(e -> clearFields());

        exportBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new java.io.File("grades.csv"));
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                java.io.File file = fileChooser.getSelectedFile();
                try (java.io.PrintWriter pw = new java.io.PrintWriter(file)) {
                    pw.println("ID,Student Name,Course Name,Score,Grade");
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        pw.println(
                            tableModel.getValueAt(i, 0) + "," +
                            tableModel.getValueAt(i, 1) + "," +
                            tableModel.getValueAt(i, 2) + "," +
                            tableModel.getValueAt(i, 3) + "," +
                            tableModel.getValueAt(i, 4)
                        );
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
                    new java.text.MessageFormat("EduTrack — Grade Report"),
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

    private void loadStudentCombo() {
        studentList = db.getAllStudents();
        studentCombo.removeAllItems();
        for (Student s : studentList) {
            studentCombo.addItem(s.getId() + " — " + s.getName());
        }
    }

    private void loadCourseCombo() {
        courseList = db.getAllCourses();
        courseCombo.removeAllItems();
        for (Course c : courseList) {
            courseCombo.addItem(c.getId() + " — " + c.getCourseName());
        }
    }

    private void loadGrades() {
        tableModel.setRowCount(0);
        List<Grade> grades = db.getAllGrades();
        List<Student> students = db.getAllStudents();
        List<Course> courses = db.getAllCourses();
        for (Grade g : grades) {
            String studentName = students.stream().filter(s -> s.getId() == g.getStudentId()).map(Student::getName).findFirst().orElse("Unknown");
            String courseName  = courses.stream().filter(c -> c.getId() == g.getCourseId()).map(Course::getCourseName).findFirst().orElse("Unknown");
            tableModel.addRow(new Object[]{g.getId(), studentName, courseName, g.getScore(), g.getLetterGrade()});
        }
    }

    private void loadGrades(JLabel countLabel) {
        tableModel.setRowCount(0);
        List<Grade> grades = db.getAllGrades();
        List<Student> students = db.getAllStudents();
        List<Course> courses = db.getAllCourses();
        for (Grade g : grades) {
            String studentName = students.stream().filter(s -> s.getId() == g.getStudentId()).map(Student::getName).findFirst().orElse("Unknown");
            String courseName  = courses.stream().filter(c -> c.getId() == g.getCourseId()).map(Course::getCourseName).findFirst().orElse("Unknown");
            tableModel.addRow(new Object[]{g.getId(), studentName, courseName, g.getScore(), g.getLetterGrade()});
        }
        countLabel.setText("Total Grades: " + grades.size());
    }

    private void filterTable(String query, JLabel countLabel) {
        tableModel.setRowCount(0);
        List<Grade> grades = db.getAllGrades();
        List<Student> students = db.getAllStudents();
        List<Course> courses = db.getAllCourses();
        int count = 0;
        for (Grade g : grades) {
            String studentName = students.stream().filter(s -> s.getId() == g.getStudentId()).map(Student::getName).findFirst().orElse("Unknown");
            String courseName  = courses.stream().filter(c -> c.getId() == g.getCourseId()).map(Course::getCourseName).findFirst().orElse("Unknown");
            if (studentName.toLowerCase().contains(query.toLowerCase()) ||
                courseName.toLowerCase().contains(query.toLowerCase())) {
                tableModel.addRow(new Object[]{g.getId(), studentName, courseName, g.getScore(), g.getLetterGrade()});
                count++;
            }
        }
        countLabel.setText("Total Grades: " + count);
    }

    private void clearFields() {
        scoreField.setText("");
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
