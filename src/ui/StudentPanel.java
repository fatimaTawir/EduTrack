package ui;

import database.DatabaseManager;
import models.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class StudentPanel extends JPanel {

    private DatabaseManager db;
    private JTable table;
    private DefaultTableModel tableModel;

    // Input fields
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;

    public StudentPanel(DatabaseManager db) {
        this.db = db;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        buildUI();
        loadStudents();
    }

    private void buildUI() {

        // ===== FORM PANEL (top) =====
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name field
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(15);
        formPanel.add(nameField, gbc);

        // Email field
        gbc.gridx = 2; gbc.gridy = 0;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 3;
        emailField = new JTextField(15);
        formPanel.add(emailField, gbc);

        // Phone field
        gbc.gridx = 4; gbc.gridy = 0;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 5;
        phoneField = new JTextField(12);
        formPanel.add(phoneField, gbc);

        // ===== BUTTONS =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton addBtn    = createButton("Add Student",    new Color(26, 172, 172));
        JButton updateBtn = createButton("Update Student", new Color(52, 120, 200));
        JButton deleteBtn = createButton("Delete Student", new Color(220, 60, 60));
        JButton clearBtn  = createButton("Clear Fields",   new Color(150, 160, 170));
        JButton exportBtn = createButton("Export CSV",     new Color(80, 160, 80));
        JButton printBtn  = createButton("Print Report",   new Color(120, 80, 160));

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(exportBtn);
        buttonPanel.add(printBtn);

        // Combine form + buttons
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

        // Student count label
        JLabel countLabel = new JLabel("Total Students: 0");
        countLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        countLabel.setForeground(new Color(26, 172, 172));

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(clearSearchBtn);
        searchPanel.add(Box.createHorizontalStrut(20));
        searchPanel.add(countLabel);

        // ===== TABLE =====
        String[] columns = {"ID", "Full Name", "Email", "Phone"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
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

        // Search filter logic
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { filterTable(searchField.getText(), countLabel); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { filterTable(searchField.getText(), countLabel); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterTable(searchField.getText(), countLabel); }
        });

        clearSearchBtn.addActionListener(e -> {
            searchField.setText("");
            loadStudents(countLabel);
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230)));

        // Click a row to fill the form fields
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                nameField.setText((String) tableModel.getValueAt(row, 1));
                emailField.setText((String) tableModel.getValueAt(row, 2));
                phoneField.setText((String) tableModel.getValueAt(row, 3));
            }
        });

        // ===== BUTTON ACTIONS =====

        // ADD
        addBtn.addActionListener(e -> {
            String name  = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();

            if (name.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Name and Email are required!", "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            db.addStudent(name, email, phone);
            loadStudents(countLabel);
            clearFields();
            JOptionPane.showMessageDialog(this, "Student added successfully!");
        });

        // UPDATE
        updateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this,
                    "Please select a student to update!", "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            int id       = (int) tableModel.getValueAt(row, 0);
            String name  = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();

            db.updateStudent(id, name, email, phone);
            loadStudents(countLabel);
            clearFields();
            JOptionPane.showMessageDialog(this, "Student updated successfully!");
        });

        // DELETE
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this,
                    "Please select a student to delete!", "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this student?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                int id = (int) tableModel.getValueAt(row, 0);
                db.deleteStudent(id);
                loadStudents(countLabel);
                clearFields();
                JOptionPane.showMessageDialog(this, "Student deleted successfully!");
            }
        });

        // CLEAR
        clearBtn.addActionListener(e -> clearFields());

        // EXPORT CSV
        exportBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new java.io.File("students.csv"));
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                java.io.File file = fileChooser.getSelectedFile();
                try (java.io.PrintWriter pw = new java.io.PrintWriter(file)) {
                    pw.println("ID,Full Name,Email,Phone");
                    List<Student> students = db.getAllStudents();
                    for (Student s : students) {
                        pw.println(s.getId() + "," + s.getName() + "," + s.getEmail() + "," + s.getPhone());
                    }
                    JOptionPane.showMessageDialog(this, "Exported successfully to:\n" + file.getAbsolutePath());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Export failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // PRINT REPORT
        printBtn.addActionListener(e -> {
            try {
                table.print(JTable.PrintMode.FIT_WIDTH,
                    new java.text.MessageFormat("EduTrack — Student Report"),
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

    // Load all students into the table
    private void loadStudents() {
        tableModel.setRowCount(0);
        List<Student> students = db.getAllStudents();
        for (Student s : students) {
            tableModel.addRow(new Object[]{
                s.getId(), s.getName(), s.getEmail(), s.getPhone()
            });
        }
    }

    // Load students and update count label
    private void loadStudents(JLabel countLabel) {
        tableModel.setRowCount(0);
        List<Student> students = db.getAllStudents();
        for (Student s : students) {
            tableModel.addRow(new Object[]{
                s.getId(), s.getName(), s.getEmail(), s.getPhone()
            });
        }
        countLabel.setText("Total Students: " + students.size());
    }

    // Filter table by search text
    private void filterTable(String query, JLabel countLabel) {
        tableModel.setRowCount(0);
        List<Student> students = db.getAllStudents();
        int count = 0;
        for (Student s : students) {
            if (s.getName().toLowerCase().contains(query.toLowerCase()) ||
                s.getEmail().toLowerCase().contains(query.toLowerCase()) ||
                s.getPhone().toLowerCase().contains(query.toLowerCase())) {
                tableModel.addRow(new Object[]{
                    s.getId(), s.getName(), s.getEmail(), s.getPhone()
                });
                count++;
            }
        }
        countLabel.setText("Total Students: " + count);
    }

    // Clear all input fields
    private void clearFields() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        table.clearSelection();
    }

    // Helper to create styled buttons
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
