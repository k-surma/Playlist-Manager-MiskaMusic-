package com.example.playlistmanager.service;

import com.example.playlistmanager.models.Notification;
import com.example.playlistmanager.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
        this.notificationRepository.initializeDatabase();
    }

    // Usunięto parametr accessLevel
    public void addNotification(int userId, int senderId, int playlistId, String message) {
        Notification notification = new Notification(0, userId, senderId, playlistId, message, "PENDING");
        notificationRepository.addNotification(notification);
    }

    public List<Notification> getPendingNotificationsForUser(int userId) {
        return notificationRepository.getNotificationsForUser(userId);
    }

    public void updateNotificationStatus(int notificationId, String status) {
        notificationRepository.updateNotificationStatus(notificationId, status);
    }
}
