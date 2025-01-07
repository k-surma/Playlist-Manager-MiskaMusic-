package com.example.playlistmanager.repositories;

import com.example.playlistmanager.models.Playlist;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PlaylistRepository {

    private static final String DB_URL = "jdbc:sqlite:playlists.db"; // Updated database name

    public void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            System.out.println("Połączono z bazą danych playlists.db.");

            // Tworzy tabelę "playlists" tylko jeśli jeszcze nie istnieje
            String sql = """
            CREATE TABLE IF NOT EXISTS playlists (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                userId INTEGER NOT NULL
            )
        """;
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
                System.out.println("Tabela 'playlists' została utworzona (lub już istnieje).");
            }
        } catch (SQLException e) {
            System.err.println("Błąd inicjalizacji bazy danych playlists.db: " + e.getMessage());
            throw new RuntimeException("Failed to initialize playlists database", e);
        }
    }


    public void save(Playlist playlist) {
        String sql = "INSERT INTO playlists (name, userId) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, playlist.getName());
            pstmt.setInt(2, playlist.getUserId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save playlist", e);
        }
    }

    public List<Playlist> findByUserId(int userId) {
        String sql = "SELECT * FROM playlists WHERE userId = ?";
        List<Playlist> playlists = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Playlist playlist = new Playlist(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("userId")
                );
                playlists.add(playlist);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve playlists", e);
        }
        return playlists;
    }

    public void delete(int playlistId) {
        String sql = "DELETE FROM playlists WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, playlistId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete playlist", e);
        }
    }
}
