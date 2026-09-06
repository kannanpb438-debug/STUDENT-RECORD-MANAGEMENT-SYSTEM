package com.srms.dao;

import com.srms.model.Student;
import java.util.List;

public interface StudentDAO {
    Student findById(int studentId);
    Student findByUserId(int userId);
    Student findByRollNo(String rollNo);
    List<Student> getAllStudents();
    List<Student> getStudentsByDeptAndSemester(int deptId, int semester);
    boolean addStudentWithUser(Student student, String initialPassword);
    boolean updateStudent(Student student);
    boolean deleteStudent(int studentId);
    int getTotalStudentCount();
}
