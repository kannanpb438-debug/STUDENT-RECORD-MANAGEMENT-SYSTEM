package com.srms.service;

import com.srms.dao.UserDAO;
import com.srms.dao.impl.UserDAOImpl;
import com.srms.exception.InvalidLoginException;
import com.srms.model.User;
import com.srms.util.PasswordUtil;

public class AuthService {
    private final UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAOImpl();
    }

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User authenticate(String username, String password) throws InvalidLoginException {
        return authenticate(username, password, null);
    }

    public User authenticate(String username, String password, String expectedRole) throws InvalidLoginException {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            throw new InvalidLoginException("Username and Password cannot be empty.");
        }

        User user = userDAO.findByUsername(username.trim());
        if (user == null) {
            throw new InvalidLoginException("Invalid username or password.");
        }

        if (user.isLocked()) {
            throw new InvalidLoginException("Account is locked due to 5 consecutive failed login attempts. Please contact Admin.");
        }

        boolean valid = PasswordUtil.checkPassword(password, user.getPasswordHash());
        if (!valid) {
            int attempts = user.getFailedAttempts() + 1;
            userDAO.updateFailedAttempts(user.getUsername(), attempts);
            if (attempts >= 5) {
                userDAO.setLockStatus(user.getUsername(), true);
                throw new InvalidLoginException("5 failed login attempts reached. Your account has been locked!");
            }
            throw new InvalidLoginException("Invalid username or password. Attempt " + attempts + " of 5.");
        }

        // Reset failed attempts on clean login
        if (user.getFailedAttempts() > 0) {
            userDAO.updateFailedAttempts(user.getUsername(), 0);
        }

        return user;
    }

    public boolean resetPassword(int userId, String newPassword) {
        String hash = PasswordUtil.hashPassword(newPassword);
        return userDAO.updatePassword(userId, hash);
    }
}
