# EduTrack 
# EduTrack — Student Management System

![Java](https://img.shields.io/badge/Java-OOP-orange)
![SQLite](https://img.shields.io/badge/Database-SQLite-blue)
![Swing](https://img.shields.io/badge/UI-Java%20Swing-teal)

A desktop application built with Java Swing and SQLite for managing
students, courses, and grades in an educational environment.

## Features
- Add, update, and delete students
- Manage courses and credit hours
- Assign and track grades with automatic letter grade calculation
- Search across all records in real time
- Export data to CSV (opens in Excel)
- Print reports directly from the app

## Tech Stack
| Technology | Purpose |
|-----------|---------|
| Java | Core programming language |
| OOP | Inheritance, Encapsulation, Polymorphism |
| Java Swing | Desktop UI |
| SQLite | Local database |
| JDBC | Java database connection |

## Project Structure
EduTrack/
├── lib/
│   └── sqlite-jdbc-3.41.2.2.jar
└── src/
├── models/
│   ├── Student.java
│   ├── Course.java
│   └── Grade.java
├── database/
│   └── DatabaseManager.java
├── ui/
│   ├── MainFrame.java
│   ├── StudentPanel.java
│   ├── CoursePanel.java
│   └── GradePanel.java
└── Main.java
## How to Run

### Requirements
- Java JDK 11 or higher

### Steps
1. Clone the repository:
```bash
   git clone https://github.com/fatimaTawir/EduTrack.git
   cd EduTrack
```

2. Compile:
```bash
   javac -cp lib/sqlite-jdbc-3.41.2.2.jar -d out src/models/*.java src/database/*.java src/ui/*.java src/Main.java
```

3. Run:
```bash
   java -cp "out;lib/sqlite-jdbc-3.41.2.2.jar" Main
```

## Screenshots
<img width="1099" height="734" alt="image" src="https://github.com/user-attachments/assets/d5c4f787-89d4-4cc9-a4c0-1cb940cf847e" />
<img width="1100" height="734" alt="image" src="https://github.com/user-attachments/assets/52405acb-15bf-4167-b0e4-f63acd99dca1" />
<img width="1105" height="731" alt="image" src="https://github.com/user-attachments/assets/f14318f1-fc98-4793-9e7e-8a6222a62f1e" />



## Author
Built by [Fatna Tawir] — Software Engineering Student
