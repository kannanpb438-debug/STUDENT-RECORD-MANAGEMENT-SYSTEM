-- ============================================================
-- Student Record Management System (SRMS) Database Schema
-- Compatible with MySQL 8.x and H2 Database (3NF Normalized)
-- ============================================================

-- 1. USERS / LOGIN TABLE
CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('ADMIN', 'FACULTY', 'STUDENT', 'PARENT')),
    failed_attempts INT DEFAULT 0,
    is_locked BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. DEPARTMENT TABLE
CREATE TABLE IF NOT EXISTS department (
    dept_id INT PRIMARY KEY AUTO_INCREMENT,
    dept_code VARCHAR(10) NOT NULL UNIQUE,
    dept_name VARCHAR(100) NOT NULL
);

-- 3. FACULTY TABLE
CREATE TABLE IF NOT EXISTS faculty (
    faculty_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT UNIQUE,
    employee_id VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    dept_id INT,
    designation VARCHAR(50),
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(15),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (dept_id) REFERENCES department(dept_id) ON DELETE SET NULL
);

-- 4. STUDENT TABLE
CREATE TABLE IF NOT EXISTS student (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT UNIQUE,
    roll_no VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    dept_id INT,
    semester INT NOT NULL DEFAULT 1,
    batch_year INT NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(15),
    address VARCHAR(255),
    dob DATE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (dept_id) REFERENCES department(dept_id) ON DELETE SET NULL
);

-- 5. PARENT TABLE
CREATE TABLE IF NOT EXISTS parent (
    parent_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT UNIQUE,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(15),
    occupation VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 6. PARENT_STUDENT MAPPING TABLE
CREATE TABLE IF NOT EXISTS parent_student (
    parent_id INT NOT NULL,
    student_id INT NOT NULL,
    relationship VARCHAR(20) DEFAULT 'PARENT',
    PRIMARY KEY (parent_id, student_id),
    FOREIGN KEY (parent_id) REFERENCES parent(parent_id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES student(student_id) ON DELETE CASCADE
);

-- 7. SUBJECT / COURSE TABLE
CREATE TABLE IF NOT EXISTS subject (
    subject_id INT PRIMARY KEY AUTO_INCREMENT,
    subject_code VARCHAR(20) NOT NULL UNIQUE,
    subject_name VARCHAR(100) NOT NULL,
    semester INT NOT NULL,
    dept_id INT,
    faculty_id INT,
    credits INT DEFAULT 3,
    FOREIGN KEY (dept_id) REFERENCES department(dept_id) ON DELETE CASCADE,
    FOREIGN KEY (faculty_id) REFERENCES faculty(faculty_id) ON DELETE SET NULL
);

-- 8. ATTENDANCE TABLE
CREATE TABLE IF NOT EXISTS attendance (
    attendance_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    subject_id INT NOT NULL,
    attendance_date DATE NOT NULL,
    status VARCHAR(10) NOT NULL CHECK (status IN ('PRESENT', 'ABSENT')),
    marked_by INT,
    period_no INT DEFAULT 1,
    FOREIGN KEY (student_id) REFERENCES student(student_id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subject(subject_id) ON DELETE CASCADE,
    FOREIGN KEY (marked_by) REFERENCES faculty(faculty_id) ON DELETE SET NULL,
    CONSTRAINT unique_student_subject_date_period UNIQUE (student_id, subject_id, attendance_date, period_no)
);

-- 9. MARKS TABLE
CREATE TABLE IF NOT EXISTS marks (
    mark_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    subject_id INT NOT NULL,
    exam_type VARCHAR(20) NOT NULL CHECK (exam_type IN ('SERIES1', 'SERIES2', 'ASSIGNMENT', 'SEMESTER')),
    marks_obtained DECIMAL(5,2) NOT NULL,
    max_marks DECIMAL(5,2) NOT NULL DEFAULT 50.00,
    FOREIGN KEY (student_id) REFERENCES student(student_id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subject(subject_id) ON DELETE CASCADE,
    CONSTRAINT unique_student_subject_exam UNIQUE (student_id, subject_id, exam_type)
);

-- 10. FEE RECORD TABLE
CREATE TABLE IF NOT EXISTS fee_record (
    fee_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    semester INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(10) NOT NULL CHECK (status IN ('PAID', 'PENDING')),
    due_date DATE,
    payment_date DATE,
    receipt_no VARCHAR(50) UNIQUE,
    FOREIGN KEY (student_id) REFERENCES student(student_id) ON DELETE CASCADE
);

-- 11. ANNOUNCEMENT TABLE
CREATE TABLE IF NOT EXISTS announcement (
    announcement_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(150) NOT NULL,
    message TEXT NOT NULL,
    posted_by INT,
    target_role VARCHAR(20) DEFAULT 'ALL',
    target_dept INT DEFAULT 0,
    target_batch VARCHAR(20) DEFAULT 'ALL',
    posted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (posted_by) REFERENCES users(user_id) ON DELETE SET NULL
);

-- 12. TIMETABLE TABLE
CREATE TABLE IF NOT EXISTS timetable (
    timetable_id INT PRIMARY KEY AUTO_INCREMENT,
    dept_id INT NOT NULL,
    semester INT NOT NULL,
    day_of_week VARCHAR(15) NOT NULL CHECK (day_of_week IN ('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY')),
    period_no INT NOT NULL,
    subject_id INT NOT NULL,
    faculty_id INT,
    room_no VARCHAR(20) DEFAULT 'A-101',
    FOREIGN KEY (dept_id) REFERENCES department(dept_id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subject(subject_id) ON DELETE CASCADE,
    FOREIGN KEY (faculty_id) REFERENCES faculty(faculty_id) ON DELETE SET NULL,
    CONSTRAINT unique_dept_sem_day_period UNIQUE (dept_id, semester, day_of_week, period_no)
);

-- INDEXES FOR FAST SEARCH AND CONCURRENT PERFORMANCE
CREATE INDEX IF NOT EXISTS idx_student_roll ON student(roll_no);
CREATE INDEX IF NOT EXISTS idx_student_dept ON student(dept_id);
CREATE INDEX IF NOT EXISTS idx_attendance_student ON attendance(student_id);
CREATE INDEX IF NOT EXISTS idx_attendance_date ON attendance(attendance_date);
CREATE INDEX IF NOT EXISTS idx_marks_student ON marks(student_id);
CREATE INDEX IF NOT EXISTS idx_fee_student ON fee_record(student_id);

