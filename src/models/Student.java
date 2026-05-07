package models;

public class Student {
     // Properties (attributes of a student)
    private int id;
    private String name;
    private String email;
    private String phone;

    // Constructor — called when creating a new Student object
    public Student(int id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // Getters — used to READ each property
    public int getId()       { return id; }
    public String getName()  { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }

    // Setters — used to UPDATE each property
    public void setName(String name)   { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }

    // toString — used to display student info as text
    @Override
    public String toString() {
        return "Student[id=" + id + ", name=" + name + ", email=" + email + "]";
    }
}
