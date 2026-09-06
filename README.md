# Student Record Management System (SRMS)
### Campus7-Style ERP Web Application for KTU B.Tech S3 OOP Mini Project

---

## 📌 Project Overview
The **Student Record Management System (SRMS)** is a web-based ERP portal designed for college academic administration. It provides role-based access for **Administrators**, **Faculty/Staff**, and **Students**, demonstrating core Object-Oriented Programming (OOP) concepts, robust database design (3NF), HikariCP connection pooling, and clean responsive UI design.

---

## 🛠️ Technology Stack
- **Backend:** Java (Servlets + JSP), Java 17 / 21
- **Database:** MySQL 8.x / H2 Embedded Database (Zero-config default)
- **Database Connectivity:** JDBC PreparedStatement with **HikariCP Connection Pool**
- **Security:** BCrypt password hashing & role-based HTTP session guards
- **PDF Export:** OpenPDF / iText library
- **Frontend:** HTML5, CSS3 (Campus7 Deep Blue Palette), JavaScript
- **Server:** Embedded Jetty / Apache Tomcat
- **Build Tool:** Apache Maven

---

## 🔑 Demo Credentials

| Portal | Role | Username | Password |
|---|---|---|---|
| **Admin Portal** | Administrator | `admin` | `admin123` |
| **Faculty Portal** | HOD / Professor | `faculty1` | `faculty123` |
| **Faculty Portal** | Associate Professor | `faculty2` | `faculty123` |
| **Student Portal** | Student (S3 CSE) | `student1` | `student123` |
| **Student Portal** | Student (S3 CSE) | `student2` | `student123` |
| **Student Portal** | Student (S3 ECE) | `student3` | `student123` |

---

## 🎯 Key OOP Concepts Demonstrated (For Viva)

1. **Encapsulation:** Model fields in `com.srms.model` are strictly `private`, accessed only through getters and setters.
2. **Inheritance:** `User` abstract base class extended by `Admin`, `Faculty`, and `Student`.
3. **Polymorphism:** Abstract `getDashboardSummary()` method implemented uniquely across `Admin`, `Faculty`, and `Student`.
4. **Abstraction:** Interfaces (`UserDAO`, `StudentDAO`, `FacultyDAO`, `AcademicDAO`, `AttendanceDAO`, `MarksDAO`, `FeeDAO`, `AnnouncementDAO`) hide SQL queries and database connections behind clean contracts.
5. **Association & Aggregation:** Relationships between `Student`, `Department`, `Faculty`, `Subject`, and `Timetable`.
6. **Exception Handling:** Custom exception classes (`InvalidLoginException`, `RecordNotFoundException`, `DatabaseException`) for graceful error handling.
7. **Database Transactions:** Multi-step operations like student registration execute inside atomic transactions (`conn.setAutoCommit(false)`).

---

## 🚀 How to Run the Application

### Option 1: Standalone Maven / Java Execution
Run the embedded server directly using Maven:
```bash
mvn compile exec:java
```
Or execute `com.srms.ServerRunner` in your Java IDE (Eclipse / IntelliJ IDEA).

Open your browser and navigate to:
**`http://localhost:8080/srms/`**

### Option 2: Deploy to Apache Tomcat
Build the `.war` package:
```bash
mvn clean package
```
Copy `target/srms.war` to your Tomcat `webapps/` folder and start Tomcat.

---

## 📁 Project Directory Structure
```
STUEDENT MANAGEMNET RECEORD SYSTEM/
├── pom.xml
├── README.md
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/srms/
│   │   │       ├── model/          # OOP Models (User, Admin, Faculty, Student, etc.)
│   │   │       ├── dao/            # DAO Interfaces & Implementations
│   │   │       ├── service/        # AuthService, ReportPdfService
│   │   │       ├── controller/     # Servlets (Login, Admin, Faculty, Student, PDF)
│   │   │       ├── filter/         # AuthenticationFilter (RBAC)
│   │   │       ├── util/           # DBConnection (HikariCP), PasswordUtil
│   │   │       ├── exception/      # Custom Exception classes
│   │   │       └── ServerRunner.java
│   │   ├── resources/
│   │   │   ├── schema.sql          # 3NF Database Schema
│   │   │   └── seed_data.sql       # Initial Seed Data
│   │   └── webapp/
│   │       ├── WEB-INF/web.xml
│   │       ├── css/style.css       # Campus7 Master ERP Theme
│   │       ├── js/main.js          # Interactive Tab & Filter Scripts
│   │       └── *.jsp               # Web View Templates
```
