package models;

public class Course {
     // Properties (attributes of a course)
    private int id;
    private String courseName;
    private String courseCode;
    private int credits;

    // Constructor — called when creating a new Course object
    public Course(int id, String courseName, String courseCode, int credits) {
        this.id = id;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.credits = credits;
    }

    // Getters — used to READ each property
    public int getId()             { return id; }
    public String getCourseName()  { return courseName; }
    public String getCourseCode()  { return courseCode; }
    public int getCredits()        { return credits; }

    // Setters — used to UPDATE each property
    public void setCourseName(String courseName)  { this.courseName = courseName; }
    public void setCourseCode(String courseCode)  { this.courseCode = courseCode; }
    public void setCredits(int credits)           { this.credits = credits; }

    // toString — prints course info in readable format
    @Override
    public String toString() {
        return "Course[id=" + id + ", name=" + courseName + ", code=" + courseCode + ", credits=" + credits + "]";
    }
}
