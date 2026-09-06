package com.srms.filter;

import com.srms.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * AuthenticationFilter - Role-Based Access Control (RBAC) Filter.
 * Protects routes based on authenticated user session and role.
 */
@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        httpResponse.setHeader("Pragma", "no-cache");
        httpResponse.setDateHeader("Expires", 0);

        String uri = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String relativePath = uri.substring(contextPath.length());

        // Allow public static assets, login, and registration endpoints
        if (relativePath.startsWith("/css/") || relativePath.startsWith("/js/") ||
            relativePath.startsWith("/images/") || relativePath.equals("/login") ||
            relativePath.equals("/register") || relativePath.equals("/register.jsp") ||
            relativePath.equals("/index.jsp") || relativePath.equals("/login.jsp") ||
            relativePath.equals("/")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = httpRequest.getSession(false);
        User loggedUser = (session != null) ? (User) session.getAttribute("loggedUser") : null;

        if (loggedUser == null) {
            httpResponse.sendRedirect(contextPath + "/login");
            return;
        }

        String role = loggedUser.getRole();

        // Handle direct browser access to raw JSP files by redirecting to their respective Servlets or enforcing role check
        if (relativePath.endsWith(".jsp") && !relativePath.equals("/login.jsp") &&
            !relativePath.equals("/register.jsp") && !relativePath.equals("/index.jsp")) {

            if (relativePath.contains("admin") || relativePath.contains("manage-") || relativePath.equals("/announcements.jsp")) {
                if (!"ADMIN".equalsIgnoreCase(role)) {
                    httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Admin rights required.");
                    return;
                }
                if (relativePath.equals("/admin-dashboard.jsp")) { httpResponse.sendRedirect(contextPath + "/admin/dashboard"); return; }
                if (relativePath.equals("/manage-students.jsp")) { httpResponse.sendRedirect(contextPath + "/admin/students"); return; }
                if (relativePath.equals("/manage-faculty.jsp")) { httpResponse.sendRedirect(contextPath + "/admin/faculty"); return; }
                if (relativePath.equals("/manage-courses.jsp")) { httpResponse.sendRedirect(contextPath + "/admin/courses"); return; }
                if (relativePath.equals("/manage-fees.jsp")) { httpResponse.sendRedirect(contextPath + "/admin/fees"); return; }
                if (relativePath.equals("/announcements.jsp")) { httpResponse.sendRedirect(contextPath + "/admin/announcements"); return; }
            }

            if (relativePath.contains("faculty") || relativePath.equals("/enter-marks.jsp") || relativePath.equals("/mark-attendance.jsp")) {
                if (!"FACULTY".equalsIgnoreCase(role) && !"ADMIN".equalsIgnoreCase(role)) {
                    httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Faculty rights required.");
                    return;
                }
                if (relativePath.equals("/faculty-dashboard.jsp")) { httpResponse.sendRedirect(contextPath + "/faculty/dashboard"); return; }
                if (relativePath.equals("/enter-marks.jsp")) { httpResponse.sendRedirect(contextPath + "/faculty/marks"); return; }
                if (relativePath.equals("/mark-attendance.jsp")) { httpResponse.sendRedirect(contextPath + "/faculty/attendance"); return; }
                if (relativePath.equals("/faculty-timetable.jsp")) { httpResponse.sendRedirect(contextPath + "/faculty/timetable"); return; }
                if (relativePath.equals("/faculty-announcements.jsp")) { httpResponse.sendRedirect(contextPath + "/faculty/announcements"); return; }
            }

            if (relativePath.contains("student") || relativePath.startsWith("/view-")) {
                if (!"STUDENT".equalsIgnoreCase(role) && !"ADMIN".equalsIgnoreCase(role)) {
                    httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Student rights required.");
                    return;
                }
                if (relativePath.equals("/student-dashboard.jsp")) { httpResponse.sendRedirect(contextPath + "/student/dashboard"); return; }
                if (relativePath.equals("/view-attendance.jsp")) { httpResponse.sendRedirect(contextPath + "/student/attendance"); return; }
                if (relativePath.equals("/view-marks.jsp")) { httpResponse.sendRedirect(contextPath + "/student/marks"); return; }
                if (relativePath.equals("/view-fees.jsp")) { httpResponse.sendRedirect(contextPath + "/student/fees"); return; }
                if (relativePath.equals("/view-timetable.jsp")) { httpResponse.sendRedirect(contextPath + "/student/timetable"); return; }
            }

            if (relativePath.contains("parent")) {
                if (!"PARENT".equalsIgnoreCase(role) && !"ADMIN".equalsIgnoreCase(role)) {
                    httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Parent rights required.");
                    return;
                }
                if (relativePath.equals("/parent-dashboard.jsp")) { httpResponse.sendRedirect(contextPath + "/parent/dashboard"); return; }
            }
        }

        // Route Guard checks for URL paths
        if (relativePath.startsWith("/admin") && !"ADMIN".equalsIgnoreCase(role)) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Admin rights required.");
            return;
        }

        if (relativePath.startsWith("/faculty") && !"FACULTY".equalsIgnoreCase(role) && !"ADMIN".equalsIgnoreCase(role)) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Faculty rights required.");
            return;
        }

        if (relativePath.startsWith("/student") && !"STUDENT".equalsIgnoreCase(role) && !"ADMIN".equalsIgnoreCase(role)) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Student rights required.");
            return;
        }

        if (relativePath.startsWith("/parent") && !"PARENT".equalsIgnoreCase(role) && !"ADMIN".equalsIgnoreCase(role)) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Parent rights required.");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
