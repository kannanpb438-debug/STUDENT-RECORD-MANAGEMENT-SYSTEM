package com.srms.dao;

import com.srms.model.Parent;
import com.srms.model.Student;

import java.util.List;

public interface ParentDAO {
    Parent findByUserId(int userId);
    Parent findById(int parentId);
    List<Student> getLinkedStudents(int parentId);
    boolean isStudentLinkedToParent(int parentId, int studentId);
    boolean addParentWithUser(Parent parent, String initialPassword, String studentRollNo);
    boolean linkParentToStudent(int parentId, int studentId);
}
