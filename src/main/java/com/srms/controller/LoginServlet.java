package com.srms.controller;

import com.srms.exception.InvalidLoginException;
import com.srms.model.User;
import com.srms.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AuthService authService;

    @Override
    public void init() throws ServletException {
        this.authService = new AuthService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("loggedUser") != null) {
            User user = (User) session.getAttribute("loggedUser");
            redirectDashboard(user.getRole(), response, request.getContextPath());
            return;
        }
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            User user = authService.authenticate(username, password);
            HttpSession session = request.getSession(true);
            session.setAttribute("loggedUser", user);
            session.setMaxInactiveInterval(1800); // 30 minutes session timeout

            redirectDashboard(user.getRole(), response, request.getContextPath());

        } catch (InvalidLoginException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    private void redirectDashboard(String role, HttpServletResponse response, String contextPath) throws IOException {
        if ("ADMIN".equalsIgnoreCase(role)) {
            response.sendRedirect(contextPath + "/admin/dashboard");
        } else if ("FACULTY".equalsIgnoreCase(role)) {
            response.sendRedirect(contextPath + "/faculty/dashboard");
        } else if ("STUDENT".equalsIgnoreCase(role)) {
            response.sendRedirect(contextPath + "/student/dashboard");
        } else if ("PARENT".equalsIgnoreCase(role)) {
            response.sendRedirect(contextPath + "/parent/dashboard");
        } else {
            response.sendRedirect(contextPath + "/login");
        }
    }
}
