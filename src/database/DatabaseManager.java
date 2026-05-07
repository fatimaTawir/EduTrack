package database;

import models.Student;
import models.Course;
import models.Grade;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    // Path to the SQLite database file
    private static final String URL = "jdbc:sqlite:edutrack.db";

    // ==================== CONNECTION ====================

    // Opens a connection to the database
  private Connection connect() {
    try {
        return DriverManager.getConnection(URL);
    } catch (SQLException e) {
        System.out.println("Connection error: " + e.getMessage());
        return null;
    }
}

    // ==================== CREATE TABLES ====================

    // Creates all tables if they don't exist yet
    public void createTables() {
        String studentTable = """
            CREATE TABLE IF NOT EXISTS students (
                id      INTEGER PRIMARY KEY AUTOINCREMENT,
                name    TEXT NOT NULL,
                email   TEXT NOT NULL,
                phone   TEXT
            );
        """;

        String courseTable = """
            CREATE TABLE IF NOT EXISTS courses (
                id          INTEGER PRIMARY KEY AUTOINCREMENT,
                courseName  TEXT NOT NULL,
                courseCode  TEXT NOT NULL,
                credits     INTEGER
            );
        """;

        String gradeTable = """
            CREATE TABLE IF NOT EXISTS grades (
                id          INTEGER PRIMARY KEY AUTOINCREMENT,
                studentId   INTEGER NOT NULL,
                courseId    INTEGER NOT NULL,
                score       REAL,
                FOREIGN KEY (studentId) REFERENCES students(id),
                FOREIGN KEY (courseId)  REFERENCES courses(id)
            );
        """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(studentTable);
            stmt.execute(courseTable);
            stmt.execute(gradeTable);
            System.out.println("Tables created successfully.");
        } catch (SQLException e) {
            System.out.println("Table creation error: " + e.getMessage());
        }
    }

    // ==================== STUDENT METHODS ====================

    // Add a new student
    public void addStudent(String name, String email, String phone) {
        String sql = "INSERT INTO students(name, email, phone) VALUES(?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, phone);
            pstmt.executeUpdate();
            System.out.println("Student added: " + name);
        } catch (SQLException e) {
            System.out.println("Add student error: " + e.getMessage());
        }
    }

    // Get all students
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                students.add(new Student(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Get students error: " + e.getMessage());
        }
        return students;
    }

    // Update a student
    public void updateStudent(int id, String name, String email, String phone) {
        String sql = "UPDATE students SET name=?, email=?, phone=? WHERE id=?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, phone);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
            System.out.println("Student updated: " + name);
        } catch (SQLException e) {
            System.out.println("Update student error: " + e.getMessage());
        }
    }

    // Delete a student
    public void deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id=?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Student deleted: id=" + id);
        } catch (SQLException e) {
            System.out.println("Delete student error: " + e.getMessage());
        }
    }

    // ==================== COURSE METHODS ====================

    // Add a new course
    public void addCourse(String courseName, String courseCode, int credits) {
        String sql = "INSERT INTO courses(courseName, courseCode, credits) VALUES(?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseName);
            pstmt.setString(2, courseCode);
            pstmt.setInt(3, credits);
            pstmt.executeUpdate();
            System.out.println("Course added: " + courseName);
        } catch (SQLException e) {
            System.out.println("Add course error: " + e.getMessage());
        }
    }

    // Get all courses
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                courses.add(new Course(
                    rs.getInt("id"),
                    rs.getString("courseName"),
                    rs.getString("courseCode"),
                    rs.getInt("credits")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Get courses error: " + e.getMessage());
        }
        return courses;
    }

    // Update a course
    public void updateCourse(int id, String courseName, String courseCode, int credits) {
        String sql = "UPDATE courses SET courseName=?, courseCode=?, credits=? WHERE id=?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseName);
            pstmt.setString(2, courseCode);
            pstmt.setInt(3, credits);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
            System.out.println("Course updated: " + courseName);
        } catch (SQLException e) {
            System.out.println("Update course error: " + e.getMessage());
        }
    }

    // Delete a course
    public void deleteCourse(int id) {
        String sql = "DELETE FROM courses WHERE id=?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Course deleted: id=" + id);
        } catch (SQLException e) {
            System.out.println("Delete course error: " + e.getMessage());
        }
    }

    // ==================== GRADE METHODS ====================

    // Add a new grade
    public void addGrade(int studentId, int courseId, double score) {
        String sql = "INSERT INTO grades(studentId, courseId, score) VALUES(?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, courseId);
            pstmt.setDouble(3, score);
            pstmt.executeUpdate();
            System.out.println("Grade added for studentId=" + studentId);
        } catch (SQLException e) {
            System.out.println("Add grade error: " + e.getMessage());
        }
    }

    // Get all grades
    public List<Grade> getAllGrades() {
        List<Grade> grades = new ArrayList<>();
        String sql = "SELECT * FROM grades";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                grades.add(new Grade(
                    rs.getInt("id"),
                    rs.getInt("studentId"),
                    rs.getInt("courseId"),
                    rs.getDouble("score")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Get grades error: " + e.getMessage());
        }
        return grades;
    }

    // Delete a grade
    public void deleteGrade(int id) {
        String sql = "DELETE FROM grades WHERE id=?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Grade deleted: id=" + id);
        } catch (SQLException e) {
            System.out.println("Delete grade error: " + e.getMessage());
        }
    }
}