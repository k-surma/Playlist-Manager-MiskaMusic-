package com.example.playlistmanager.repositories;

import com.example.playlistmanager.models.SharedPlaylist;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SharedPlaylistRepository {
    private static final String DB_URL = "jdbc:sqlite:shared_playlists.db";

    public void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = """
                CREATE TABLE IF NOT EXISTS shared_playlists (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    playlistId INTEGER NOT NULL,
                    sharedWithUserId INTEGER NOT NULL,
                    FOREIGN KEY (playlistId) REFERENCES playlists(id),
                    FOREIGN KEY (sharedWithUserId) REFERENCES users(id)
                )
            """;
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
                System.out.println("Tabela shared_playlists została utworzona (lub już istnieje).");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize shared_playlists database", e);
        }
    }

    public void save(SharedPlaylist sharedPlaylist) {
        String sql = "INSERT INTO shared_playlists (playlistId, sharedWithUserId) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, sharedPlaylist.getPlaylistId());
            pstmt.setInt(2, sharedPlaylist.getSharedWithUserId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save shared playlist", e);
        }
    }

    public List<SharedPlaylist> findByUserId(int userId) {
        String sql = "SELECT * FROM shared_playlists WHERE sharedWithUserId = ?";
        List<SharedPlaylist> sharedPlaylists = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                SharedPlaylist sharedPlaylist = new SharedPlaylist(
                        rs.getInt("id"),
                        rs.getInt("playlistId"),
                        rs.getInt("sharedWithUserId")
                );
                sharedPlaylists.add(sharedPlaylist);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve shared playlists", e);
        }
        return sharedPlaylists;
    }

    public void delete(int sharedPlaylistId) {
        String sql = "DELETE FROM shared_playlists WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, sharedPlaylistId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete shared playlist", e);
        }
    }
}
