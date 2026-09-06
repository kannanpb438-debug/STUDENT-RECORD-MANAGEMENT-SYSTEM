package com.srms.controller;

import com.srms.dao.AcademicDAO;
import com.srms.dao.AnnouncementDAO;
import com.srms.dao.FacultyDAO;
import com.srms.dao.impl.AcademicDAOImpl;
import com.srms.dao.impl.AnnouncementDAOImpl;
import com.srms.dao.impl.FacultyDAOImpl;
import com.srms.model.Announcement;
import com.srms.model.Department;
import com.srms.model.Faculty;
import com.srms.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/faculty/announcements")
public class FacultyAnnouncementsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private AnnouncementDAO announcementDAO;
    private AcademicDAO academicDAO;
    private FacultyDAO facultyDAO;

    @Override
    public void init() throws ServletException {
        this.announcementDAO = new AnnouncementDAOImpl();
        this.academicDAO = new AcademicDAOImpl();
        this.facultyDAO = new FacultyDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("loggedUser");
        Faculty faculty = facultyDAO.findByUserId(user.getUserId());

        String action = request.getParameter("action");
        if ("delete".equalsIgnoreCase(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                try {
                    int announcementId = Integer.parseInt(idStr);
                    Announcement existing = announcementDAO.getAnnouncementById(announcementId);
                    if (existing != null) {
                        if (user != null && !"ADMIN".equalsIgnoreCase(user.getRole()) && existing.getPostedBy() != user.getUserId()) {
                            response.sendRedirect(request.getContextPath() + "/faculty/announcements?error=Permission+Denied:+You+can+only+delete+announcements+posted+by+you");
                            return;
                        }
                        announcementDAO.deleteAnnouncement(announcementId);
                        response.sendRedirect(request.getContextPath() + "/faculty/announcements?msg=Announcement+deleted+successfully");
                        return;
                    }
                } catch (NumberFormatException e) {
                    response.sendRedirect(request.getContextPath() + "/faculty/announcements?error=Invalid+Announcement+ID");
                    return;
                }
            }
        }

        List<Announcement> announcements = announcementDAO.getAllAnnouncements();
        List<Department> departments = academicDAO.getAllDepartments();

        request.setAttribute("faculty", faculty);
        request.setAttribute("announcements", announcements);
        request.setAttribute("departments", departments);
        request.getRequestDispatcher("/faculty-announcements.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("loggedUser") : null;

        String action = request.getParameter("action");
        String title = request.getParameter("title");
        String message = request.getParameter("message");

        if (title == null || title.trim().isEmpty() || message == null || message.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/faculty/announcements?error=Title+and+Message+cannot+be+empty");
            return;
        }

        Announcement a = new Announcement();
        a.setTitle(title.trim());
        a.setMessage(message.trim());
        a.setPostedBy(user != null ? user.getUserId() : 1);
        a.setTargetRole(request.getParameter("targetRole"));
        
        String deptIdStr = request.getParameter("targetDept");
        a.setTargetDept(deptIdStr != null && !deptIdStr.isEmpty() ? Integer.parseInt(deptIdStr) : 0);
        a.setTargetBatch(request.getParameter("targetBatch"));

        try {
            if ("edit".equalsIgnoreCase(action)) {
                String annIdStr = request.getParameter("announcementId");
                if (annIdStr != null && !annIdStr.isEmpty()) {
                    int targetId = Integer.parseInt(annIdStr);
                    Announcement existing = announcementDAO.getAnnouncementById(targetId);
                    if (existing != null && user != null && !"ADMIN".equalsIgnoreCase(user.getRole()) && existing.getPostedBy() != user.getUserId()) {
                        response.sendRedirect(request.getContextPath() + "/faculty/announcements?error=Permission+Denied:+You+can+only+edit+announcements+posted+by+you");
                        return;
                    }
                    a.setAnnouncementId(targetId);
                    boolean success = announcementDAO.updateAnnouncement(a);
                    if (success) {
                        response.sendRedirect(request.getContextPath() + "/faculty/announcements?msg=Announcement+updated+successfully");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/faculty/announcements?error=Failed+to+update+announcement");
                    }
                    return;
                }
            }

            boolean success = announcementDAO.createAnnouncement(a);
            if (success) {
                response.sendRedirect(request.getContextPath() + "/faculty/announcements?msg=Announcement+posted+successfully");
            } else {
                response.sendRedirect(request.getContextPath() + "/faculty/announcements?error=Failed+to+publish+announcement");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/faculty/announcements?error=Error+processing+announcement:+" + e.getMessage());
        }
    }
}
