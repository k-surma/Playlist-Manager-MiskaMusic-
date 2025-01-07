package com.example.playlistmanager.repositories;

import com.example.playlistmanager.models.Song;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SongRepository {

    public void initializeDatabase(String playlistName) {
        String dbUrl = "jdbc:sqlite:" + playlistName + ".db";
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            String sql = """
            CREATE TABLE IF NOT EXISTS songs (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                artist TEXT NOT NULL,
                path TEXT NOT NULL
            )
        """;
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
                System.out.println("Tabela 'songs' została utworzona lub już istnieje w bazie: " + playlistName + ".db");
            }
        } catch (SQLException e) {
            System.err.println("Błąd inicjalizacji bazy danych piosenek: " + e.getMessage());
            throw new RuntimeException("Failed to initialize songs database", e);
        }
    }


    public void save(String playlistName, Song song) {
        String dbUrl = "jdbc:sqlite:" + playlistName + ".db";
        String sql = "INSERT INTO songs (title, artist, path) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, song.getTitle());
            pstmt.setString(2, song.getArtist());
            pstmt.setString(3, song.getPath());
            pstmt.executeUpdate();
            System.out.println("Dodano piosenkę: " + song.getTitle() + " do playlisty: " + playlistName);
        } catch (SQLException e) {
            System.err.println("Błąd zapisu piosenki do bazy danych: " + e.getMessage());
            throw new RuntimeException("Failed to save song", e);
        }
    }



    public List<Song> findAll(String playlistName) {
        String dbUrl = "jdbc:sqlite:" + playlistName + ".db";
        String sql = "SELECT * FROM songs";
        List<Song> songs = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Song song = new Song(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("artist"),
                        rs.getString("path")
                );
                songs.add(song);
            }
            System.out.println("Znaleziono piosenki w playliście: " + playlistName + ", liczba: " + songs.size());
        } catch (SQLException e) {
            System.err.println("Błąd pobierania piosenek: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve songs", e);
        }
        return songs;
    }



    public void delete(String playlistName, int songId) {
        String dbUrl = "jdbc:sqlite:" + playlistName + ".db";
        String sql = "DELETE FROM songs WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, songId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete song", e);
        }
    }
}
