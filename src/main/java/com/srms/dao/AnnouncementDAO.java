package com.srms.dao;

import com.srms.model.Announcement;
import java.util.List;

public interface AnnouncementDAO {
    List<Announcement> getAnnouncementsForUser(String role, int deptId, String batchYear);
    List<Announcement> getAllAnnouncements();
    boolean createAnnouncement(Announcement a);
    boolean updateAnnouncement(Announcement a);
    boolean deleteAnnouncement(int announcementId);
    Announcement getAnnouncementById(int announcementId);
}
