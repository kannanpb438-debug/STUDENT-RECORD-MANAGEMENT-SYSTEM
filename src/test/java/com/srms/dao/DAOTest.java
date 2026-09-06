package com.srms.dao;

import com.srms.dao.impl.*;
import com.srms.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DAOTest {

    private static StudentDAO studentDAO;
    private static FacultyDAO facultyDAO;
    private static AcademicDAO academicDAO;
    private static AttendanceDAO attendanceDAO;
    private static MarksDAO marksDAO;
    private static FeeDAO feeDAO;
    private static AnnouncementDAO announcementDAO;

    @BeforeAll
    public static void setUp() {
        studentDAO = new StudentDAOImpl();
        facultyDAO = new FacultyDAOImpl();
        academicDAO = new AcademicDAOImpl();
        attendanceDAO = new AttendanceDAOImpl();
        marksDAO = new MarksDAOImpl();
        feeDAO = new FeeDAOImpl();
        announcementDAO = new AnnouncementDAOImpl();
    }

    @Test
    public void testGetAllDepartments() {
        List<Department> depts = academicDAO.getAllDepartments();
        assertNotNull(depts);
        assertFalse(depts.isEmpty());
    }

    @Test
    public void testStudentOperations() {
        int initialCount = studentDAO.getTotalStudentCount();
        assertTrue(initialCount >= 0);

        Student s = studentDAO.findByRollNo("CS2023001");
        if (s != null) {
            assertNotNull(s.getName());
            assertEquals("CS2023001", s.getRollNo());
        }
    }

    @Test
    public void testFacultyOperations() {
        int count = facultyDAO.getTotalFacultyCount();
        assertTrue(count >= 0);

        List<Faculty> facultyList = facultyDAO.getAllFaculty();
        assertNotNull(facultyList);
    }

    @Test
    public void testAnnouncementOperations() {
        Announcement a = new Announcement();
        a.setTitle("Test Announcement");
        a.setMessage("This is a test message for JUnit");
        a.setPostedBy(1);
        a.setTargetRole("ALL");
        a.setTargetDept(0);
        a.setTargetBatch("ALL");

        boolean created = announcementDAO.createAnnouncement(a);
        assertTrue(created);

        List<Announcement> list = announcementDAO.getAllAnnouncements();
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }
}
