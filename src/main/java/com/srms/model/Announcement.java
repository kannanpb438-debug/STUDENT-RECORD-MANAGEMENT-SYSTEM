package com.srms.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Announcement implements Serializable {
    private static final long serialVersionUID = 1L;

    private int announcementId;
    private String title;
    private String message;
    private int postedBy;
    private String posterName;
    private String targetRole; // ALL, STUDENT, FACULTY
    private int targetDept;
    private String targetBatch;
    private Timestamp postedAt;

    public Announcement() {}

    public Announcement(int announcementId, String title, String message, int postedBy, String targetRole, int targetDept, String targetBatch, Timestamp postedAt) {
        this.announcementId = announcementId;
        this.title = title;
        this.message = message;
        this.postedBy = postedBy;
        this.targetRole = targetRole;
        this.targetDept = targetDept;
        this.targetBatch = targetBatch;
        this.postedAt = postedAt;
    }

    public int getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(int announcementId) {
        this.announcementId = announcementId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(int postedBy) {
        this.postedBy = postedBy;
    }

    public String getPosterName() {
        return posterName;
    }

    public void setPosterName(String posterName) {
        this.posterName = posterName;
    }

    public String getTargetRole() {
        return targetRole;
    }

    public void setTargetRole(String targetRole) {
        this.targetRole = targetRole;
    }

    public int getTargetDept() {
        return targetDept;
    }

    public void setTargetDept(int targetDept) {
        this.targetDept = targetDept;
    }

    public String getTargetBatch() {
        return targetBatch;
    }

    public void setTargetBatch(String targetBatch) {
        this.targetBatch = targetBatch;
    }

    public Timestamp getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(Timestamp postedAt) {
        this.postedAt = postedAt;
    }
}
