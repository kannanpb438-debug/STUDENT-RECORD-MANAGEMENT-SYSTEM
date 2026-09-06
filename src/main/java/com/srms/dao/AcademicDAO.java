package com.srms.dao;

import com.srms.model.Department;
import com.srms.model.Subject;
import com.srms.model.Timetable;

import java.util.List;

public interface AcademicDAO {
    List<Department> getAllDepartments();
    Department getDepartmentById(int deptId);
    boolean addDepartment(Department dept);

    List<Subject> getAllSubjects();
    List<Subject> getSubjectsByFaculty(int facultyId);
    List<Subject> getSubjectsByDeptAndSemester(int deptId, int semester);
    Subject getSubjectById(int subjectId);
    boolean addSubject(Subject subject);
    boolean updateSubject(Subject subject);
    boolean deleteSubject(int subjectId);

    List<Timetable> getTimetableByDeptAndSemester(int deptId, int semester);
    List<Timetable> getTimetableByFaculty(int facultyId);
    boolean addTimetableEntry(Timetable entry);
}
