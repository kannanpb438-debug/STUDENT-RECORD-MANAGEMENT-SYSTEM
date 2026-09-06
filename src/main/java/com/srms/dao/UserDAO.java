package com.srms.dao;

import com.srms.model.User;
import java.util.List;

/**
 * Interface UserDAO
 * Demonstrates Abstraction in OOP Architecture.
 */
public interface UserDAO {
    User findByUsername(String username);
    User findById(int userId);
    boolean createUser(User user);
    boolean updatePassword(int userId, String newPasswordHash);
    void updateFailedAttempts(String username, int attempts);
    void setLockStatus(String username, boolean locked);
    List<User> getAllUsers();
}
