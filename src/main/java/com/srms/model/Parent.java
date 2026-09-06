package com.srms.model;

/**
 * Concrete Subclass: Parent
 * Extends User, demonstrating Polymorphism & Encapsulation.
 */
public class Parent extends User {
    private static final long serialVersionUID = 1L;

    private int parentId;
    private String name;
    private String email;
    private String phone;
    private String occupation;

    public Parent() {
        super();
        setRole("PARENT");
    }

    public Parent(int userId, String username, String passwordHash, int parentId, String name, String email, String phone, String occupation) {
        super(userId, username, passwordHash, "PARENT");
        this.parentId = parentId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.occupation = occupation;
    }

    @Override
    public String getDashboardSummary() {
        return "Parent Portal - Welcome " + name + ". Monitor academic performance, attendance records, and exam results of your linked student(s).";
    }

    // Getters & Setters
    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
}
