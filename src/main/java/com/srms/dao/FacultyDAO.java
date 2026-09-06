package com.srms.dao;

import com.srms.model.Faculty;
import java.util.List;

public interface FacultyDAO {
    Faculty findById(int facultyId);
    Faculty findByUserId(int userId);
    List<Faculty> getAllFaculty();
    boolean addFacultyWithUser(Faculty faculty, String initialPassword);
    boolean updateFaculty(Faculty faculty);
    boolean deleteFaculty(int facultyId);
    int getTotalFacultyCount();
}
