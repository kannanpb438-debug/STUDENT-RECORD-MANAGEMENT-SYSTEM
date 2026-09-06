package com.srms.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordUtilTest {

    @Test
    public void testHashPasswordAndCheckPassword() {
        String rawPassword = "securePassword123";
        String hashedPassword = PasswordUtil.hashPassword(rawPassword);

        assertNotNull(hashedPassword);
        assertNotEquals(rawPassword, hashedPassword);
        assertTrue(PasswordUtil.checkPassword(rawPassword, hashedPassword));
        assertFalse(PasswordUtil.checkPassword("wrongPassword", hashedPassword));
    }

    @Test
    public void testNullOrEmptyInputs() {
        assertFalse(PasswordUtil.checkPassword(null, "somehash"));
        assertFalse(PasswordUtil.checkPassword("pass", null));
    }
}
