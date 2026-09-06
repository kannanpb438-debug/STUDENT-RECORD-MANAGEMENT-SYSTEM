package com.srms.model;

/**
 * Concrete Subclass: Admin
 * Extends User, demonstrating Polymorphism & Encapsulation.
 */
public class Admin extends User {
    private static final long serialVersionUID = 1L;

    public Admin() {
        super();
        setRole("ADMIN");
    }

    public Admin(int userId, String username, String passwordHash) {
        super(userId, username, passwordHash, "ADMIN");
    }

    @Override
    public String getDashboardSummary() {
        return "Administrator Dashboard - Full CRUD access to Students, Faculty, Courses, Fees, and System Controls.";
    }
}
