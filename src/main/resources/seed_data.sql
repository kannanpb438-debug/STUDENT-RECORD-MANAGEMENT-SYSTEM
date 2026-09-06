-- ============================================================
-- Student Record Management System (SRMS) Seed Data
-- Standard passwords for quick test login:
-- Admin: admin / admin123
-- Faculty: faculty1 / faculty123, faculty2 / faculty123
-- Student: student1 / student123, student2 / student123, student3 / student123
-- ============================================================

-- 1. DEPARTMENTS
INSERT INTO department (dept_id, dept_code, dept_name) VALUES
(1, 'CSE', 'Computer Science and Engineering'),
(2, 'ECE', 'Electronics and Communication Engineering'),
(3, 'ME', 'Mechanical Engineering'),
(4, 'EEE', 'Electrical and Electronics Engineering'),
(5, 'CE', 'Civil Engineering');

-- 2. USERS
INSERT INTO users (user_id, username, password_hash, role) VALUES
(1, 'admin', 'admin123', 'ADMIN'),
(2, 'faculty1', 'faculty123', 'FACULTY'),
(3, 'faculty2', 'faculty123', 'FACULTY'),
(4, 'student1', 'student123', 'STUDENT'),
(5, 'student2', 'student123', 'STUDENT'),
(6, 'student3', 'student123', 'STUDENT'),
(7, 'parent1', 'parent123', 'PARENT'),
(8, 'parent2', 'parent123', 'PARENT');

-- 3. FACULTY DETAILS
INSERT INTO faculty (faculty_id, user_id, employee_id, name, dept_id, designation, email, phone) VALUES
(1, 2, 'FAC-CSE-001', 'Dr. Ramesh Kumar', 1, 'Professor & HOD', 'ramesh.kumar@college.edu', '9876543210'),
(2, 3, 'FAC-ECE-002', 'Prof. Anitha Sharma', 2, 'Associate Professor', 'anitha.sharma@college.edu', '9876543211');

-- 4. STUDENT DETAILS (S3 KTU B.Tech)
INSERT INTO student (student_id, user_id, roll_no, name, dept_id, semester, batch_year, email, phone, address, dob) VALUES
(1, 4, 'TVE23CS001', 'Adarsh V', 1, 3, 2023, 'adarsh.cs23@student.edu', '9123456780', 'MGM Nagar, Trivandrum', '2004-05-15'),
(2, 5, 'TVE23CS002', 'Bhavya Nair', 1, 3, 2023, 'bhavya.cs23@student.edu', '9123456781', 'Kaloor, Ernakulam', '2004-08-22'),
(3, 6, 'TVE23EC001', 'Cyril Mathew', 2, 3, 2023, 'cyril.ec23@student.edu', '9123456782', 'East Fort, Thrissur', '2004-11-10');

-- 4B. PARENT DETAILS
INSERT INTO parent (parent_id, user_id, name, email, phone, occupation) VALUES
(1, 7, 'Vijay Nair', 'vijay.nair@parent.com', '9876500001', 'Engineer'),
(2, 8, 'Mathew Joseph', 'mathew.j@parent.com', '9876500002', 'Business');

-- 4C. PARENT_STUDENT MAPPING
INSERT INTO parent_student (parent_id, student_id, relationship) VALUES
(1, 1, 'FATHER'),
(1, 2, 'FATHER'),
(2, 3, 'FATHER');


-- 5. SUBJECTS (S3 KTU B.Tech OOP & Core Subjects)
INSERT INTO subject (subject_id, subject_code, subject_name, semester, dept_id, faculty_id, credits) VALUES
(1, 'CST205', 'Object Oriented Programming using Java', 3, 1, 1, 4),
(2, 'CST201', 'Data Structures', 3, 1, 1, 4),
(3, 'MAT203', 'Discrete Mathematical Structures', 3, 1, 1, 4),
(4, 'ECT201', 'Solid State Devices', 3, 2, 2, 4),
(5, 'ECT203', 'Logic System Design', 3, 2, 2, 4);

-- 6. ATTENDANCE RECORDS
INSERT INTO attendance (attendance_id, student_id, subject_id, attendance_date, status, marked_by, period_no) VALUES
(1, 1, 1, '2026-09-01', 'PRESENT', 1, 1),
(2, 1, 1, '2026-09-02', 'PRESENT', 1, 2),
(3, 1, 1, '2026-09-03', 'ABSENT', 1, 1),
(4, 1, 2, '2026-09-01', 'PRESENT', 1, 3),
(5, 1, 2, '2026-09-02', 'PRESENT', 1, 4),
(6, 2, 1, '2026-09-01', 'PRESENT', 1, 1),
(7, 2, 1, '2026-09-02', 'PRESENT', 1, 2),
(8, 2, 1, '2026-09-03', 'PRESENT', 1, 1),
(9, 3, 4, '2026-09-01', 'PRESENT', 2, 1),
(10, 3, 4, '2026-09-02', 'ABSENT', 2, 2);

-- 7. MARKS RECORDS
INSERT INTO marks (mark_id, student_id, subject_id, exam_type, marks_obtained, max_marks) VALUES
(1, 1, 1, 'SERIES1', 45.00, 50.00),
(2, 1, 1, 'ASSIGNMENT', 10.00, 10.00),
(3, 1, 2, 'SERIES1', 42.50, 50.00),
(4, 2, 1, 'SERIES1', 48.00, 50.00),
(5, 2, 1, 'ASSIGNMENT', 9.50, 10.00),
(6, 3, 4, 'SERIES1', 38.00, 50.00);

-- 8. FEE RECORDS
INSERT INTO fee_record (fee_id, student_id, semester, amount, status, due_date, payment_date, receipt_no) VALUES
(1, 1, 3, 45000.00, 'PAID', '2026-08-30', '2026-08-25', 'REC-2026-001'),
(2, 2, 3, 45000.00, 'PAID', '2026-08-30', '2026-08-28', 'REC-2026-002'),
(3, 3, 3, 45000.00, 'PENDING', '2026-09-15', NULL, NULL);

-- 9. ANNOUNCEMENTS
INSERT INTO announcement (announcement_id, title, message, posted_by, target_role, target_dept, target_batch, posted_at) VALUES
(1, 'S3 B.Tech Series Test 1 Schedule Published', 'The Series Test 1 for S3 B.Tech students will commence from September 15th. Detailed timetable is available on the portal.', 1, 'ALL', 0, 'ALL', CURRENT_TIMESTAMP),
(2, 'KTU OOP Mini Project Evaluation', 'All CSE S3 students must submit their OOP Mini Project documentation and source code by October 5th.', 1, 'STUDENT', 1, '2023', CURRENT_TIMESTAMP);

-- 10. TIMETABLE
INSERT INTO timetable (timetable_id, dept_id, semester, day_of_week, period_no, subject_id, faculty_id, room_no) VALUES
(1, 1, 3, 'MONDAY', 1, 1, 1, 'CS-LH1'),
(2, 1, 3, 'MONDAY', 2, 2, 1, 'CS-LH1'),
(3, 1, 3, 'TUESDAY', 1, 1, 1, 'CS-LH1'),
(4, 1, 3, 'WEDNESDAY', 3, 3, 1, 'CS-LH1'),
(5, 2, 3, 'MONDAY', 1, 4, 2, 'EC-LH2');
