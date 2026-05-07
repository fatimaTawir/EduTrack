package models;

public class Grade {
    // Properties (attributes of a grade)
    private int id;
    private int studentId;
    private int courseId;
    private double score;
    private String letterGrade;

    // Constructor — called when creating a new Grade object
    public Grade(int id, int studentId, int courseId, double score) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.score = score;
        this.letterGrade = calculateLetterGrade(score);
    }

    // Automatically calculate letter grade from score
    private String calculateLetterGrade(double score) {
        if (score >= 90) return "A";
        else if (score >= 80) return "B";
        else if (score >= 70) return "C";
        else if (score >= 60) return "D";
        else return "F";
    }

    // Getters — used to READ each property
    public int getId()            { return id; }
    public int getStudentId()     { return studentId; }
    public int getCourseId()      { return courseId; }
    public double getScore()      { return score; }
    public String getLetterGrade(){ return letterGrade; }

    // Setters — used to UPDATE each property
    public void setScore(double score) {
        this.score = score;
        this.letterGrade = calculateLetterGrade(score); // recalculate when score changes
    }

    // toString — prints grade info in readable format
    @Override
    public String toString() {
        return "Grade[studentId=" + studentId + ", courseId=" + courseId + 
               ", score=" + score + ", grade=" + letterGrade + "]";
    }
}
