package com.srms.controller;

import com.srms.dao.AcademicDAO;
import com.srms.dao.AnnouncementDAO;
import com.srms.dao.impl.AcademicDAOImpl;
import com.srms.dao.impl.AnnouncementDAOImpl;
import com.srms.model.Announcement;
import com.srms.model.Department;
import com.srms.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/announcements")
public class ManageAnnouncementsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private AnnouncementDAO announcementDAO;
    private AcademicDAO academicDAO;

    @Override
    public void init() throws ServletException {
        this.announcementDAO = new AnnouncementDAOImpl();
        this.academicDAO = new AcademicDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("delete".equalsIgnoreCase(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                try {
                    int announcementId = Integer.parseInt(idStr);
                    announcementDAO.deleteAnnouncement(announcementId);
                    response.sendRedirect(request.getContextPath() + "/admin/announcements?msg=Announcement+deleted");
                    return;
                } catch (NumberFormatException e) {
                    response.sendRedirect(request.getContextPath() + "/admin/announcements?error=Invalid+Announcement+ID");
                    return;
                }
            }
        }

        List<Announcement> announcements = announcementDAO.getAllAnnouncements();
        List<Department> departments = academicDAO.getAllDepartments();

        request.setAttribute("announcements", announcements);
        request.setAttribute("departments", departments);
        request.getRequestDispatcher("/announcements.jsp").forward(request, response);
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
            response.sendRedirect(request.getContextPath() + "/admin/announcements?error=Title+and+Message+cannot+be+empty");
            return;
        }

        try {
            Announcement a = new Announcement();
            a.setTitle(title.trim());
            a.setMessage(message.trim());
            a.setPostedBy(user != null ? user.getUserId() : 1);
            a.setTargetRole(request.getParameter("targetRole"));
            
            String deptIdStr = request.getParameter("targetDept");
            a.setTargetDept(deptIdStr != null && !deptIdStr.isEmpty() ? Integer.parseInt(deptIdStr) : 0);
            a.setTargetBatch(request.getParameter("targetBatch"));

            if ("edit".equalsIgnoreCase(action)) {
                String annIdStr = request.getParameter("announcementId");
                if (annIdStr != null && !annIdStr.isEmpty()) {
                    a.setAnnouncementId(Integer.parseInt(annIdStr));
                    announcementDAO.updateAnnouncement(a);
                    response.sendRedirect(request.getContextPath() + "/admin/announcements?msg=Announcement+updated+successfully");
                    return;
                }
            }

            announcementDAO.createAnnouncement(a);
            response.sendRedirect(request.getContextPath() + "/admin/announcements?msg=Announcement+posted+successfully");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/announcements?error=Error+posting+announcement:+" + e.getMessage());
        }
    }
}
