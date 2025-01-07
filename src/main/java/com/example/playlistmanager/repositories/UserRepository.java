package com.example.playlistmanager.repositories;

import com.example.playlistmanager.models.User;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class UserRepository {
    private static final String DB_URL = "jdbc:sqlite:users.db";

    public void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            // Create the table if it does not exist
            String createTableSql = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                email TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL
            )
        """;
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createTableSql);
            }

            // Check if the "name" column exists, and add it if it does not
            String checkColumnSql = "PRAGMA table_info(users);";
            boolean hasNameColumn = false;

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(checkColumnSql)) {
                while (rs.next()) {
                    String columnName = rs.getString("name");
                    if ("name".equalsIgnoreCase(columnName)) {
                        hasNameColumn = true;
                        break;
                    }
                }
            }

            if (!hasNameColumn) {
                String alterTableSql = "ALTER TABLE users ADD COLUMN name TEXT DEFAULT '';";
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(alterTableSql);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }


    public void save(User user) {
        String sql = "INSERT INTO users (email, password, name) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName()); // Save the name
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save user", e);
        }
    }

    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setName(rs.getString("name"));
                return user;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find user", e);
        }
    }

    // Add the missing findById method
    public User findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setName(rs.getString("name"));
                return user;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find user by ID", e);
        }
    }
}
