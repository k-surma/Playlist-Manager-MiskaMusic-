package com.example.playlistmanager.repositories;

import com.example.playlistmanager.models.Notification;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class NotificationRepository {
    private static final String DB_URL = "jdbc:sqlite:notifications.db";

    public void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            System.out.println("Połączono z bazą danych notifications.db.");

            String notificationsSql = """
                CREATE TABLE IF NOT EXISTS notifications (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    userId INTEGER NOT NULL,
                    senderId INTEGER NOT NULL,
                    playlistId INTEGER NOT NULL,
                    message TEXT NOT NULL,
                    status TEXT NOT NULL DEFAULT 'PENDING',
                    FOREIGN KEY (userId) REFERENCES users(id),
                    FOREIGN KEY (senderId) REFERENCES users(id),
                    FOREIGN KEY (playlistId) REFERENCES playlists(id)
                )
            """;

            try (Statement stmt = conn.createStatement()) {
                stmt.execute(notificationsSql);
                System.out.println("Tabela notifications została utworzona (lub już istnieje).");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Nie udało się zainicjalizować bazy danych do powiadomień.", e);
        }
    }

    public void addNotification(Notification notification) {
        String sql = "INSERT INTO notifications (userId, senderId, playlistId, message, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, notification.getUserId());
            pstmt.setInt(2, notification.getSenderId());
            pstmt.setInt(3, notification.getPlaylistId());
            pstmt.setString(4, notification.getMessage());
            pstmt.setString(5, notification.getStatus());
            pstmt.executeUpdate();

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) {
                if (rs.next()) {
                    notification.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add notification", e);
        }
    }

    public List<Notification> getNotificationsForUser(int userId) {
        String sql = "SELECT * FROM notifications WHERE userId = ? AND status = 'PENDING'";
        List<Notification> notifications = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            System.out.println("Executing query: " + pstmt);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Notification notification = new Notification();
                notification.setId(rs.getInt("id"));
                notification.setUserId(rs.getInt("userId"));
                notification.setSenderId(rs.getInt("senderId"));
                notification.setPlaylistId(rs.getInt("playlistId"));
                notification.setMessage(rs.getString("message"));
                notification.setStatus(rs.getString("status"));
                notifications.add(notification);
            }
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
            throw new RuntimeException("Nie udało się pobrać powiadomień.", e);
        }
        return notifications;
    }

    public void updateNotificationStatus(int notificationId, String status) {
        String sql = "UPDATE notifications SET status = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, notificationId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Nie udało się zaktualizować statusu powiadomienia.", e);
        }
    }

}
