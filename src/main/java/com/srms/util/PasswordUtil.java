package com.srms.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility for BCrypt password hashing & verification.
 * Supports direct equality fallback for seed demo testing.
 */
public class PasswordUtil {

    public static String hashPassword(String plainTextPassword) {
        if (plainTextPassword == null || plainTextPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        try {
            return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(10));
        } catch (Exception e) {
            return plainTextPassword;
        }
    }

    public static boolean checkPassword(String plainTextPassword, String storedHash) {
        if (plainTextPassword == null || storedHash == null) {
            return false;
        }

        String plain = plainTextPassword.trim();
        String stored = storedHash.trim();

        if (plain.equals(stored)) {
            return true;
        }

        if (stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$")) {
            try {
                return BCrypt.checkpw(plain, stored);
            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }
}
